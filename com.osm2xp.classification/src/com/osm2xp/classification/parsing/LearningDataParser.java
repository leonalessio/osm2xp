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

import com.osm2xp.classification.HeightProvider;
import com.osm2xp.classification.RelationBuildingData;
import com.osm2xp.classification.TypeProvider;
import com.osm2xp.classification.WayBuildingData;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.core.parsers.impl.TranslatingBinaryParser;

import math.geom2d.Point2D;
import math.geom2d.polygon.Polyline2D;

public class LearningDataParser {
	
	public LearningDataParser(File inputFile) {
		LearningRelationsCollector typeRelationsCollector = new LearningRelationsCollector(getTypePredicate());
		LearningRelationsCollector heightRelationsCollector = new LearningRelationsCollector(getHeightPredicate());
		TranslatingBinaryParser binaryParser = new TranslatingBinaryParser(inputFile, new CompositeVisitor(typeRelationsCollector, heightRelationsCollector));
		binaryParser.process();
		
		List<RelationBuildingData> typeRelationsList = typeRelationsCollector.getCollectedRelationData();
		Set<Long> necessaryTypeWays = typeRelationsList.stream().flatMap(data -> data.getOuterWayIds().stream()).collect(Collectors.toSet());
		necessaryTypeWays.addAll(typeRelationsList.stream().flatMap(data -> data.getInnerWayIds().stream()).collect(Collectors.toSet()));
		List<RelationBuildingData> heightRelationsList = heightRelationsCollector.getCollectedRelationData();
		Set<Long> necessaryHeightWays = heightRelationsList.stream().flatMap(data -> data.getOuterWayIds().stream()).collect(Collectors.toSet());
		necessaryHeightWays.addAll(heightRelationsList.stream().flatMap(data -> data.getInnerWayIds().stream()).collect(Collectors.toSet()));
		
		LearningWaysCollector typeWaysCollector = new LearningWaysCollector(getTypePredicate(), necessaryTypeWays);
		LearningWaysCollector heightWaysCollector = new LearningWaysCollector(getHeightPredicate(), necessaryHeightWays);
		new TranslatingBinaryParser(inputFile, new CompositeVisitor(typeWaysCollector, heightWaysCollector)).process();
		
		List<WayBuildingData> typeWays = typeWaysCollector.getCollectedWayData();
		typeWays.addAll(createBuildings(typeRelationsList, typeWaysCollector.getWayNds()));
		
		List<WayBuildingData> heightWays = heightWaysCollector.getCollectedWayData();
		heightWays.addAll(createBuildings(heightRelationsList, heightWaysCollector.getWayNds()));
		
		Set<Long> typeNds = typeWays.stream().flatMap(way -> way.getNodes().stream()).collect(Collectors.toSet());
		Set<Long> heightNds = heightWays.stream().flatMap(way -> way.getNodes().stream()).collect(Collectors.toSet());
		
		LearningNodesCollector typeNdCollector = new LearningNodesCollector(getTypePredicate(), typeNds);
		LearningNodesCollector heightNdCollector = new LearningNodesCollector(getHeightPredicate(), heightNds);		
		new TranslatingBinaryParser(inputFile, new CompositeVisitor(typeNdCollector, heightNdCollector)).process();
		
		computeGeometryData(typeWays, typeNdCollector.getCollectedNodes());
		computeGeometryData(heightWays, heightNdCollector.getCollectedNodes());
	}
	
	private void computeGeometryData(List<WayBuildingData> ways, Map<Long, Node> collectedNodes) {
		int invalid = 0;
		for (WayBuildingData wayData : ways) {
			List<Point2D> points = wayData.getNodes().stream()
				.map(id -> collectedNodes.get(id))
				.filter(node -> node != null)
				.map(node -> new Point2D(node.getLon(), node.getLat()))
				.collect(Collectors.toList());
			if (points.size() > 0 && points.size() == wayData.getNodes().size()) {
				Point2D base = points.get(0);
				double coef = Math.cos(base.y);
				List<Point2D> resList = new ArrayList<>();
				resList.add(new Point2D(0,0));
				for (int i = 1; i < points.size(); i++) {
					Point2D curPt = points.get(i);
					resList.add(new Point2D((curPt.x - base.x) * coef, (curPt.y - base.y)));
				}
				Polyline2D polyline2d = new Polyline2D(resList);
				double length = polyline2d.getLength();
				System.out.println("LearningDataParser.computeGeometryData() " + length); //XXX debug
			} else {
				invalid++;
			}
			
		}
		System.out.println("Missing some nodes for " + invalid + " ways");
		
	}

	private List<WayBuildingData> createBuildings(List<RelationBuildingData> relationBuildings, Map<Long, List<Long>> wayNds) {
		List<WayBuildingData> resultList = new ArrayList<WayBuildingData>();
		for (RelationBuildingData relationBuildingData : relationBuildings) {
			List<Long> outerWayIds = relationBuildingData.getOuterWayIds();
			List<List<Long>> outerNdsList = outerWayIds.stream().map(id -> wayNds.get(id)).filter(lst -> lst != null).collect(Collectors.toList());
			List<List<Long>> polygonLists = getPolygonsFrom(outerNdsList);
			List<List<Long>> innerNdsList = relationBuildingData.getInnerWayIds().stream().map(id -> wayNds.get(id)).filter(lst -> lst != null).collect(Collectors.toList());
			if (polygonLists.size() > 1) {
				if (innerNdsList.size() > 0) {
					continue; //No deep analysis for now
				}
				for (List<Long> list : polygonLists) {
					WayBuildingData buildingData = new WayBuildingData(list);
					buildingData.copyProps(relationBuildingData);
					resultList.add(buildingData);
				}
			}
			else if (polygonLists.size() == 1) {
				boolean hasHoles = getPolygonsFrom(innerNdsList).size() > 0;
				WayBuildingData buildingData = new WayBuildingData(polygonLists.get(0));
				buildingData.copyProps(relationBuildingData);
				buildingData.setHasHoles(hasHoles);
				resultList.add(buildingData);
			}
		}
		
		return resultList;
		
	}
	
	private Predicate<List<Tag>> getTypePredicate() {
		return (tags) -> TypeProvider.getBuildingType(tags) != null;
	}
	private Predicate<List<Tag>> getHeightPredicate() {
		return (tags) ->HeightProvider.getHeight(tags) > 0 && TypeProvider.isBuilding(tags);
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
		if (nodeList.size() > 1) {
			return nodeList.get(0).equals(nodeList.get(nodeList.size() - 1));
		}
		return false;
	}
}
