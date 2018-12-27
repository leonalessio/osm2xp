package com.osm2xp.parsers.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;

import com.bbn.openmap.layer.shape.ESRIPoly.ESRIFloatPoly;
import com.bbn.openmap.layer.shape.ESRIPolygonRecord;
import com.bbn.openmap.layer.shape.ShapeFile;
import com.osm2xp.gui.Activator;
import com.osm2xp.model.osm.Nd;
import com.osm2xp.model.osm.Node;
import com.osm2xp.model.osm.Way;
import com.osm2xp.parsers.IOSMDataVisitor;
import com.osm2xp.parsers.IVisitingParser;
import com.osm2xp.utils.helpers.GuiOptionsHelper;

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
	
	public ShapefileParserImpl(File file,IOSMDataVisitor visitor) {
		this.visitor = visitor;
		this.visitor = visitor;
		try {
			this.shapeFile = new ShapeFile(file);
		} catch (IOException e) {
			Activator.log(IStatus.ERROR, "Error parsing shapefile " + file.getAbsolutePath());
		}
	}

	@Override
	public void process()  {
		try {
			ESRIPolygonRecord esriPolygonRecord = (ESRIPolygonRecord) shapeFile
					.getNextRecord();

			while (esriPolygonRecord != null) {

				ESRIFloatPoly poly = (ESRIFloatPoly) esriPolygonRecord.polygons[0];
				Way way = new Way();
				way.getTags().add(GuiOptionsHelper.getShapefileTag());
				way.setId(++wayIndex);

				for (int i = 0; i < poly.getRadians().length - 1; i = i + 2) {

					Node node = new Node(null,
							Math.toDegrees(poly.getRadians()[i]),
							Math.toDegrees(poly.getRadians()[i + 1]), nodeIndex);

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
			Activator.log(IStatus.ERROR, "Error processing shapefile");
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
