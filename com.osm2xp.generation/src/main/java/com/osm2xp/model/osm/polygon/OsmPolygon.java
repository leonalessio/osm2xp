package com.osm2xp.model.osm.polygon;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.utils.geometry.GeomUtils;
import com.osm2xp.utils.osm.OsmUtils;

import math.geom2d.polygon.LinearRing2D;

/**
 * OsmPolygon.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class OsmPolygon extends OsmPolyline {

	private Double area;
	public OsmPolygon(long id, List<Tag> tags, List<Node> nodes, boolean part) {
		super(id, tags, nodes, part);
		this.height = OsmUtils.getHeightFromTags(tags);
	}

	public Double getArea() {
		if (nodes.size() > 2) {
			this.area = ((LinearRing2D) polyline).area();
		} else {
			this.area = 0D;
		}
		return area;
	}
	
	@Override
	protected void initCurve() {
		this.polyline = GeomUtils.getPolygonFromOsmNodes(nodes);
	}

	public LinearRing2D getPolygon() {
		return (LinearRing2D) getPolyline();
	}

	public void setPolygon(LinearRing2D polygon) {
		this.polyline = polygon;
	}

	public Color getRoofColor() {
		return OsmUtils.getRoofColorFromTags(this.tags);
	}

	public OsmPolygon toSimplifiedPoly() { //Made this mutable since otherwise shapes was simplified even when it's not necessary - e.g. for forest
		if (this.polyline != null && !part) {
			OsmPolygon simplified = new OsmPolygon(id, Collections.unmodifiableList(tags), Collections.unmodifiableList(nodes), part); 
			LinearRing2D result = GeomUtils.simplifyPolygon((LinearRing2D) this.polyline);
			simplified.polyline = result;
			return simplified;
		} else {
			return this;
		}
	}
	
	public Boolean isSimplePolygon() {
		Boolean result = false;
		if (this.getPolyline() != null) {
			result = (polyline.edges().size() == 4 &&  
					GeomUtils.areParallelsSegmentsIdentics((LinearRing2D) polyline));
		}
	
		return result;
	}
	
	@Override
	public String toString() {
		return "OsmPolygon [id=" + id + ", tags:" + tags + "]";
	}
}
