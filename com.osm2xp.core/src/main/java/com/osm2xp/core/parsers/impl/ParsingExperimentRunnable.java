package com.osm2xp.core.parsers.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.container.v0_6.RelationContainer;
import org.openstreetmap.osmosis.core.container.v0_6.WayContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

import com.osm2xp.core.logging.Osm2xpLogger;

import crosby.binary.osmosis.OsmosisReader;
import math.geom2d.Point2D;

public class ParsingExperimentRunnable implements Runnable{

	private Map<Long, Node> nodeMap = new HashMap<>();
	private List<Way> ways = new ArrayList<Way>();
	private File inputFile;

	public ParsingExperimentRunnable(File inputFile) {
		this.inputFile = inputFile;
	}


	public void run() {
		try (FileInputStream inputStream = new FileInputStream(inputFile)) {
			OsmosisReader reader = new OsmosisReader(inputStream);
			reader.setSink(new Sink() {
	
				public void process(EntityContainer entityContainer) {
					if (entityContainer instanceof NodeContainer) {
						process((NodeContainer) entityContainer);
					} else if (entityContainer instanceof WayContainer) {
						process((WayContainer) entityContainer);
	
					} else if (entityContainer instanceof RelationContainer) {
						process((RelationContainer) entityContainer);
					}
				}
	
				public void process(RelationContainer rel) {
					System.out.println(rel.getEntity());
					// relations.add(rel.getEntity());
				}
	
				public void process(WayContainer way) {
//					Way entity = way.getEntity();
//					System.out.println(entity);
//					Collection<Tag> tags = entity.getTags();
//					System.out.println(tags);
//					Map<String, Object> metaTags = entity.getMetaTags();
//					System.out.println(metaTags);
//					List<WayNode> wayNodes = entity.getWayNodes();
//					System.out.println(wayNodes);
					 ways.add(way.getEntity());
				}
	
				public void process(NodeContainer node) {
					nodeMap.put(node.getEntity().getId(), node.getEntity()); 
				}
	
				@Override
				public void initialize(Map<String, Object> map) {
					//Do nothing
				}
	
				@Override
				public void complete() {
					for (Way way : ways) {
						List<Point2D> coordsList = new ArrayList<Point2D>();
						List<WayNode> wayNodes = way.getWayNodes();
						for (WayNode wayNode : wayNodes) {
							Node coordsNode = nodeMap.get(wayNode.getNodeId());
							if (coordsNode == null) {
								break;
							}
							coordsList.add(new Point2D(coordsNode.getLongitude(), coordsNode.getLatitude()));
						}
						if (coordsList.size() == wayNodes.size()) {
							System.out.println(way + " " + coordsList);
							System.out.println(way.getTags().stream().map(tag -> tag.getKey() + "=" + tag.getValue()).collect(Collectors.joining(", ")));
						}
					}
	
				}
	
				@Override
				public void close() {
					// TODO Auto-generated method stub
	
				}
			});
			reader.run();
		} catch (Exception e) {
			Osm2xpLogger.log(e);
		}
		
	}

}
