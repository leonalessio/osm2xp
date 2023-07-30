package com.osm2xp.core.parsers.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bbn.openmap.layer.shape.ESRIPoly.ESRIFloatPoly;
import com.bbn.openmap.layer.shape.ESRIPolygonRecord;
import com.bbn.openmap.layer.shape.ShapeFile;
import com.bbn.openmap.proj.Length;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Nd;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.core.model.osm.Way;
import com.osm2xp.core.parsers.IOSMDataVisitor;
import com.osm2xp.core.parsers.IVisitingParser;

/**
 * Shapefile parser implementation.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class ShapefileParserImpl implements IVisitingParser {
	private ShapeFile shapeFile;
	private int nodeIndex = 1;
	private int wayIndex = 1;
	private IOSMDataVisitor visitor;
	private Tag shapefileTag;
	
	public ShapefileParserImpl(File file,IOSMDataVisitor visitor, Tag shapefileTag) {
		this.visitor = visitor;
		this.visitor = visitor;
		this.shapefileTag = shapefileTag;
		try {
			this.shapeFile = new ShapeFile(file);
		} catch (IOException e) {
			Osm2xpLogger.error("Error parsing shapefile " + file.getAbsolutePath());
		}
	}

	@Override
	public void process()  {
		try {
			ESRIPolygonRecord esriPolygonRecord = (ESRIPolygonRecord) shapeFile
					.getNextRecord();

			while (esriPolygonRecord != null) {
//				ESRIPoint minPt = esriPolygonRecord.bounds.min; //TODO handle bounding box
//				ESRIPoint maxPt = esriPolygonRecord.bounds.max;

				ESRIFloatPoly poly = (ESRIFloatPoly) esriPolygonRecord.polygons[0];
				Way way = new Way();
				if (shapefileTag != null) {
					way.getTags().add(shapefileTag);
				}
				way.setId(++wayIndex);

				for (int i = 0; i < poly.getRadians().length - 1; i = i + 2) {

					Node node = new Node(Collections.emptyList(),
							Length.DECIMAL_DEGREE.fromRadians(poly.getRadians()[i]),
							Length.DECIMAL_DEGREE.fromRadians(poly.getRadians()[i + 1]), nodeIndex);

					// add the node ref to the way
					way.getNd().add(new Nd(nodeIndex));
					// send the node to the translator for storage
					visitor.visit(node);
					// increment node index
					nodeIndex++;

				}
				List<Long> ids = new ArrayList<Long>();
				for (Nd nd : way.getNd()) {
					ids.add(nd.getRef());
				}
				visitor.visit(way);
				esriPolygonRecord = (ESRIPolygonRecord) shapeFile
						.getNextRecord();
			}
			complete();

		} catch (Exception e) {
			Osm2xpLogger.error("Error processing shapefile");
		}

	}

	@Override
	public void complete() {
		visitor.complete();
	}

	@Override
	public IOSMDataVisitor getVisitor() {
		return visitor;
	}

}
