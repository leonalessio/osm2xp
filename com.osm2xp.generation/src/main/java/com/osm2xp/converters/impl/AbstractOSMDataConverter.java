package com.osm2xp.converters.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;

import com.osm2xp.core.exceptions.DataSinkException;
import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Member;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Relation;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.core.model.osm.Way;
import com.osm2xp.core.parsers.IOSMDataVisitor;
import com.osm2xp.datastore.IDataSink;
import com.osm2xp.utils.geometry.GeomUtils;
import com.osm2xp.utils.geometry.NodeCoordinate;
import com.osm2xp.utils.geometry.Osm2XPGeometryFactory;
import com.osm2xp.utils.osm.OsmUtils;

import math.geom2d.Box2D;

public abstract class AbstractOSMDataConverter implements IOSMDataVisitor {

	protected IDataSink dataSink;
	private int nodeCnt = 0;
	private long wayCnt = 0;
	
	public AbstractOSMDataConverter(IDataSink processor) {
		super();
		this.dataSink = processor;
	}
	
	@Override
	public void visit(Box2D box) {
		dataSink.processBoundingBox(box);
	}

	@Override
	public void visit(Node node) {
		try {
			// ask translator if we have to store this node if we
			// aren't on a single pass mode
			//pointParsed(node.getLon(), node.getLat());
			if (mustStoreNode(node)) {
				dataSink.storeNode(node);
				nodeCnt ++;
				if (nodeCnt % 1000000 == 0) {
					Osm2xpLogger.info(nodeCnt + " nodes processed");		
				}
			}
		} 
		catch (DataSinkException e) {
			Osm2xpLogger.error("Error processing node.", e);
		}
	}

	@Override
	public void visit(Way way) {
		try {
			dataSink.storeWayPoints(way.getId(), way.getNodesArray());

			if (!mustProcessPolyline(way.getTags())) {
				return;
			}

			List<Long> ids = way.getNd().stream().map(nd -> nd.getRef()).collect(Collectors.toList());
			translateWay(way, ids);

			wayCnt++;
			if (wayCnt % 100000 == 0) {
				Osm2xpLogger.info(wayCnt + " ways processed");
			}
		} catch (Exception e) {
			Osm2xpLogger.error(
					"Error processing " + OsmUtils.getReadableType(way.getTags()) + " way, id=" + way.getId(), e);
		}

	}
	
	protected boolean mustStoreWay(Way way) {
		return !dataSink.isReadOnly();
	}
	
	protected abstract boolean mustStoreNode(Node node);
	
	
	protected abstract boolean mustProcessPolyline(List<Tag> tagsModel);
	
	protected abstract void translateWay(Way way, List<Long> ids) throws Osm2xpBusinessException;	

	protected abstract void translatePolys(long id, List<Tag> tagsModel, List<Polygon> cleanedPolys) throws Osm2xpBusinessException;

	@Override
	public void visit(Relation relation) {
 		List<Tag> tagsModel = relation.getTags();
		if ("multipolygon".equals(relation.getTagValue("type")) && mustProcessPolyline(tagsModel)) {
			try {
				List<List<Long>> outer = new ArrayList<>();
				List<List<Long>> inner = new ArrayList<>();
				for (Member member : relation.getMember()) {
					String role = member.getRole();
					if ("outer".equals(role)) {
						long[] wayPoints = dataSink.getWayPoints(member.getId());
						if (wayPoints != null) {
							outer.add(Arrays.stream(wayPoints).boxed().collect(Collectors.toList()));
						} else {
							Osm2xpLogger.error("Invalid way id: " + member.getId());
						}
					}
					if ("inner".equals(role)) {
						long[] wayPoints = dataSink.getWayPoints(member.getId());
						if (wayPoints != null) {
							inner.add(Arrays.stream(wayPoints).boxed().collect(Collectors.toList()));
						} else {
							Osm2xpLogger.error("Invalid way id: " + member.getId());
						}
					}
				}
				List<List<Long>> polygons = getPolygonsFrom(outer);
				if (!polygons.isEmpty()) {
					List<List<Long>> innerPolygons = getPolygonsFrom(inner);
					List<Polygon> cleanedPolys = doCleanup(polygons, innerPolygons);
					translatePolys(relation.getId(), tagsModel, cleanedPolys);
				} else {
					Osm2xpLogger.error("Problem processing " + OsmUtils.getReadableType(tagsModel) + " relation, id=" + relation.getId() + ": Outer ring is invalid, possibly not closed");						
				}
			} catch (Exception e) {
				Osm2xpLogger.error("Error processing " + OsmUtils.getReadableType(tagsModel) + " relation, id=" + relation.getId(), e);
			}
		}		

	}
	
	protected List<com.osm2xp.core.model.osm.Node> getNodes(List<Long> polyIds) {
		try {
			return dataSink.getNodes(polyIds);
		} catch (DataSinkException e) {
			Osm2xpLogger.error(e);
		}
		return null;
	}
	
	protected List<List<Long>> getPolygonsFrom(List<List<Long>> input) {
		List<List<Long>> curves = new ArrayList<>(input);
		List<List<Long>> result = new ArrayList<List<Long>>();
		for (Iterator<List<Long>> iterator = curves.iterator(); iterator.hasNext();) {
			List<Long> curList = (List<Long>) iterator.next();
			if (isClosed(curList)) { // If some way forms closed contour - remove it without further analysis
				result.add(curList);
				iterator.remove();
			}
		}
		while (!curves.isEmpty()) {
			List<Long> segment = curves.remove(0);
			List<Long> current = new ArrayList<>();
			while (segment != null) {
				if (!current.isEmpty()) {
					segment = segment.subList(1, segment.size());
				}
				current.addAll(segment);
				segment = null;
				Long lastNodeId = current.get(current.size() - 1);
				for (int i = 0; i < curves.size(); i++) {
					List<Long> curve = curves.get(i);
					if (!curve.isEmpty()) { 
						if (curve.get(0).equals(lastNodeId)) {
							segment = curves.remove(i);
							break;
						} else if (curve.get(curve.size() - 1).equals(lastNodeId)) {
							segment = curves.remove(i);							
							Collections.reverse(segment);
							break;
						}
					}
				}
			}
			if (current.get(0).equals(current.get(current.size() - 1))) {
				result.add(current);
			}
	
		}
		return result;
	}
	
	protected boolean isClosed(List<Long> curList) {
		if (curList.size() > 1) {
			return curList.get(0).equals(curList.get(curList.size() - 1));
		}
		return false;
	}
	
	protected List<Polygon> doCleanup(List<List<Long>> outer, List<List<Long>> inner) {
		GeometryFactory factory = Osm2XPGeometryFactory.getInstance();
		if (outer.size() == 1) { // If we have only one outer ring - assign all inner rings to it and return
			LinearRing[] innerRings = inner.stream().map(ids -> getRing(ids)).filter(ring -> ring != null)
					.toArray(LinearRing[]::new);
			return Collections.singletonList(factory.createPolygon(getRing(outer.get(0)), innerRings));
		} else if (inner.isEmpty()) { // If we have no inner rings - create poly for each outer ring and return these
										// polys
			return outer.stream().map(ids -> getPolygon(ids)).filter(poly -> poly != null).collect(Collectors.toList());
		}
		List<Polygon> outerPolysList = outer.stream().map(ids -> getPolygon(ids)).filter(poly -> poly != null)
				.collect(Collectors.toList());
		List<LinearRing> innerRingsList = inner.stream().map(ids -> getRing(ids)).filter(ring -> ring != null)
				.collect(Collectors.toList());
		List<Polygon> resultList = new ArrayList<Polygon>();
		for (Polygon outerPoly : outerPolysList) {
			List<LinearRing> innerRingList = new ArrayList<LinearRing>();
			for (Iterator<LinearRing> iterator = innerRingsList.iterator(); iterator.hasNext();) {
				LinearRing innerRing = iterator.next();
				if (outerPoly.covers(innerRing)) {
					innerRingList.add(innerRing);
					iterator.remove();
				}
			}
			resultList.add(factory.createPolygon(factory.createLinearRing(outerPoly.getCoordinates()),
					innerRingList.toArray(new LinearRing[0])));
		}
		return resultList;
	}

	protected Geometry getGeometry(List<Long> nodeIds) {
		if (isClosed(nodeIds)) {
			if (nodeIds.size() == 3) { //This can be a result of mistake when editing OSM - "closing" 2-point line by clicking first point after adding second one 
				nodeIds = nodeIds.subList(0,2);
			} else {
				return getPolygon(nodeIds);
			}
		}
		Coordinate[] points = getCoords(nodeIds);
		if (points != null && points.length >= 2) {
			GeometryFactory factory = Osm2XPGeometryFactory.getInstance();
			return factory.createLineString(points);
		}
		return null;	
	}

	protected Polygon getPolygon(List<Long> polyNodeIds) {
		Coordinate[] points = getCoords(polyNodeIds);
		if (points != null && points.length >= 4 && points.length == polyNodeIds.size()) {
			GeometryFactory factory = Osm2XPGeometryFactory.getInstance();
			return factory.createPolygon(points);
		}
		return null;
	}

	protected LinearRing getRing(List<Long> nodeIds) {
		Coordinate[] points = getCoords(nodeIds);
		if (points != null && points.length >= 4) {
			GeometryFactory factory = Osm2XPGeometryFactory.getInstance();
			return factory.createLinearRing(points);
		}
		return null;
	}

	protected Coordinate[] getCoords(List<Long> nodeIds) {
		List<com.osm2xp.core.model.osm.Node> nodes = getNodes(nodeIds);
		if (nodes == null) {
			return null;
		}
		NodeCoordinate[] points = nodes.stream().map(node -> new NodeCoordinate(node.getLon(), node.getLat(), node.getId()))
				.toArray(NodeCoordinate[]::new);
		if (points.length < 1) {
			return null;
		}
		return points;
	}

	protected List<Geometry> fix(List<? extends Geometry> geometries) {
		return geometries.stream().map(geom -> GeomUtils.fix(geom)).filter(geom -> geom != null).collect(Collectors.toList());
	}
	
	@Override
	public void complete() {
		try {
			dataSink.complete();
		} catch (DataSinkException e) {
			Osm2xpLogger.error(e);
		}
	}
}
