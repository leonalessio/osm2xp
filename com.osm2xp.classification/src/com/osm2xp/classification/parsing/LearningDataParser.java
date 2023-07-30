package com.osm2xp.classification.parsing;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.osm2xp.classification.model.RelationEntity;
import com.osm2xp.classification.model.WayEntity;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.core.parsers.impl.TranslatingBinaryParser;

import math.geom2d.Point2D;
import math.geom2d.ShapeArray2D;

public class LearningDataParser {
	
	private List<WayEntity> collectedWays;
	private LearningNodesCollector typeNdCollector;

	public LearningDataParser(File inputFile, Predicate<List<Tag>> wayCollectionPredicate) {
		this(inputFile,wayCollectionPredicate,false);
	}
	
	public LearningDataParser(File inputFile, Predicate<List<Tag>> wayCollectionPredicate, boolean needsClosedWays) {
		LearningRelationsCollector typeRelationsCollector = new LearningRelationsCollector(wayCollectionPredicate);
		TranslatingBinaryParser binaryParser = new TranslatingBinaryParser(inputFile, typeRelationsCollector);
		binaryParser.process();
		
		List<RelationEntity> typeRelationsList = typeRelationsCollector.getCollectedRelationData();
		Set<Long> necessaryTypeWays = typeRelationsList.stream().flatMap(data -> data.getOuterWayIds().stream()).collect(Collectors.toSet());
		necessaryTypeWays.addAll(typeRelationsList.stream().flatMap(data -> data.getInnerWayIds().stream()).collect(Collectors.toSet()));
		LearningWaysCollector typeWaysCollector = new LearningWaysCollector(wayCollectionPredicate, necessaryTypeWays, needsClosedWays);
		binaryParser = new TranslatingBinaryParser(inputFile, typeWaysCollector);
		binaryParser.process();
		
		collectedWays = typeWaysCollector.getCollectedWayData();
		collectedWays.addAll(createBuildings(typeRelationsList, typeWaysCollector.getWayNds()));
		
		Set<Long> typeNds = collectedWays.stream().flatMap(way -> way.getNodes().stream()).collect(Collectors.toSet());
		typeNdCollector = new LearningNodesCollector(wayCollectionPredicate, typeNds);
		binaryParser = new TranslatingBinaryParser(inputFile, typeNdCollector);
		binaryParser.process();
	}
	
	private List<WayEntity> createBuildings(List<RelationEntity> relationBuildings, Map<Long, List<Long>> wayNds) {
		List<WayEntity> resultList = new ArrayList<WayEntity>();
		for (RelationEntity relationBuildingData : relationBuildings) {
			List<Long> outerWayIds = relationBuildingData.getOuterWayIds();
			List<List<Long>> outerNdsList = outerWayIds.stream().map(id -> wayNds.get(id)).filter(lst -> lst != null).collect(Collectors.toList());
			List<List<Long>> polygonLists = getPolygonsFrom(outerNdsList);
			List<List<Long>> innerNdsList = relationBuildingData.getInnerWayIds().stream().map(id -> wayNds.get(id)).filter(lst -> lst != null).collect(Collectors.toList());
			if (polygonLists.size() > 1) {
				if (innerNdsList.size() > 0) {
					continue; //No deep analysis for now
				}
				for (List<Long> list : polygonLists) {
					if (!isClosed(list)) {
						continue;
					}
					WayEntity buildingData = new WayEntity(relationBuildingData.getId(), relationBuildingData.getTags(), list);
					resultList.add(buildingData);
				}
			}
			else if (polygonLists.size() == 1) {
				boolean hasHoles = getPolygonsFrom(innerNdsList).size() > 0;
				WayEntity buildingData = new WayEntity(relationBuildingData.getId(), relationBuildingData.getTags(), polygonLists.get(0));
				buildingData.setHasHoles(hasHoles);
				resultList.add(buildingData);
			}
		}
		
		return resultList;
		
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
	
	protected boolean isClosed(List<Long> nodeList) {
		if (nodeList.size() > 3) {
			return nodeList.get(0).equals(nodeList.get(nodeList.size() - 1));
		}
		return false;
	}

	public List<WayEntity> getCollectedWays(boolean computeBasicGeometry) {
		if (computeBasicGeometry) {
			for (WayEntity wayEntity : collectedWays) {
				computeGeometryData(wayEntity, typeNdCollector.getCollectedNodes());
			}
		}
		return collectedWays;
	}
	
	public List<WayEntity> getCollectedWays() {
		return getCollectedWays(false);
	}

	public Map<Long, Node> getCollectedNodes() {
		return typeNdCollector.getCollectedNodes();
	}
	
	private static void computeGeometryData(WayEntity way, Map<Long, Node> collectedNodes) {
		List<Point2D> points = way.getNodes().stream()
			.map(id -> collectedNodes.get(id))
			.filter(node -> node != null)
			.map(node -> new Point2D(node.getLon(), node.getLat()))
			.collect(Collectors.toList());
		int n = points.size();
		ShapeArray2D<Point2D> shapeArray2D = new ShapeArray2D<Point2D>(points);
		way.setBoundingBox(shapeArray2D.boundingBox());
		if (n > 0 && n == way.getNodes().size()) {
			Point2D base = points.get(0);
			List<Point2D> resList = new ArrayList<>();
			resList.add(new Point2D(0,0));
			double centerLat = base.y() / n;
			double centerLon = base.x() / n;
			for (int i = 1; i < n; i++) {
				Point2D curPt = points.get(i);
				centerLat += curPt.y() / n;
				centerLon += curPt.x() / n;
			}
			way.setCenter(centerLat,centerLon);
		}
	}

}
