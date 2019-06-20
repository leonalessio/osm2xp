package com.osm2xp.translators.xplane;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.options.GlobalOptionsProvider;
import com.osm2xp.generation.options.ObjectFile;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.generation.options.rules.XplaneObjectTagRule;
import com.osm2xp.model.osm.polygon.OsmPolygon;
import com.osm2xp.model.osm.polygon.OsmPolyline;
import com.osm2xp.model.xplane.XplaneDsf3DObject;
import com.osm2xp.utils.DsfObjectsProvider;
import com.osm2xp.utils.MiscUtils;
import com.osm2xp.utils.geometry.GeomUtils;
import com.osm2xp.utils.osm.OsmUtils;
import com.osm2xp.writers.IWriter;

import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.polygon.LinearRing2D;

public class XP3DObjectByRuleTranslator extends XPWritingTranslator {

	private DsfObjectsProvider dsfObjectsProvider;
	private XPOutputFormat outputFormat;

	public XP3DObjectByRuleTranslator(IWriter writer, DsfObjectsProvider dsfObjectsProvider, XPOutputFormat outputFormat) {
		super(writer);
		this.dsfObjectsProvider = dsfObjectsProvider;
		this.outputFormat = outputFormat;
	}

	@Override
	public boolean handlePoly(OsmPolyline osmPolyline) {
		if (!(osmPolyline instanceof OsmPolygon) || osmPolyline.isPart() || !((OsmPolygon) osmPolyline).getPolygon().isClosed()) {
			return false;
		}
		
		if (XPlaneOptionsProvider.getOptions().isGenerateObj()) {
			// simplify shape if checked and if necessary
			if (GlobalOptionsProvider.getOptions().isSimplifyShapes()
					&& !((OsmPolygon) osmPolyline).isSimplePolygon()) {
				osmPolyline = ((OsmPolygon) osmPolyline).toSimplifiedPoly();
			}
			XplaneDsf3DObject object = getRandomDsfObject((OsmPolygon) osmPolyline);
			if (object != null) {
				writer.write(outputFormat.getObjectString(object));
				return true;
			}
		}		
		return false;
	}
	
	/**
	 * return a random object index and the angle for the first matching rule
	 * 
	 * @param tags
	 * @return
	 */
	protected XplaneDsf3DObject getRandomDsfObject(OsmPolygon osmPolygon) {
		LinearRing2D polygon = osmPolygon.getPolygon();
		XplaneDsf3DObject result = null;
		// shuffle rules
		List<XplaneObjectTagRule> matchingRules = new ArrayList<XplaneObjectTagRule>();
		List<XplaneObjectTagRule> rules = XPlaneOptionsProvider.getOptions().getObjectsRules().getRules();
		for (XplaneObjectTagRule rule : rules) {
			for (Tag tag : osmPolygon.getTags()) {
				// check Tag matching
				if ((rule.getTag().getKey().equalsIgnoreCase("id")
						&& rule.getTag().getValue().equalsIgnoreCase(String.valueOf(osmPolygon.getId())))
						|| (OsmUtils.compareTags(rule.getTag(), tag))) {
					// check rule options

					Boolean areaOK = !rule.isAreaCheck() || (rule.isAreaCheck()
							&& (osmPolygon.getArea() > rule.getMinArea() && osmPolygon.getArea() < rule.getMaxArea()));

					Boolean sizeOK = !rule.isSizeCheck() || GeomUtils.isRectangleBigEnoughForObject(
							rule.getxVectorMinLength(), rule.getxVectorMaxLength(), rule.getyVectorMinLength(),
							rule.getyVectorMaxLength(), polygon);

					Boolean checkSimplePoly = !rule.isSimplePolygonOnly()
							|| (rule.isSimplePolygonOnly() && osmPolygon.isSimplePolygon());

					if (areaOK && sizeOK && checkSimplePoly) {
						matchingRules.add(rule);
					}
				}
			}
		}
		if (matchingRules.size() > 0) {
			int height = osmPolygon.getHeight();
			XplaneObjectTagRule rule = null;
			if (height > 0) {
				for (XplaneObjectTagRule curRule : matchingRules) {
					if (isMultiHeight(curRule)) {
						rule = curRule;
						break;
					}
				}
			}
			if (rule == null) {
				rule = matchingRules.get(new Random().nextInt(matchingRules.size()));
			}
			Point2D origin = GeomUtils.getPolylineCenter(osmPolygon.getPolygon());
			double angle = rule.getAngle();
			if (rule.isUsePolygonAngle()) {
				angle = calculateAngle(polygon, rule);
			} else if (rule.isRandomAngle()) {
				angle = Double.valueOf(MiscUtils.getRandomInt(0, 360));
			}

			result = new XplaneDsf3DObject(osmPolygon, getObjectFromRule(rule, osmPolygon), angle, origin);
		}
		// compute object index
		return result;
	}
	
	protected List<ObjectFile> getNoHeightFiles(XplaneObjectTagRule objectTagRule) {
		List<ObjectFile> resList = new ArrayList<ObjectFile>();
		for (ObjectFile objectFile : objectTagRule.getObjectsFiles()) {
			String path = objectFile.getPath();
			if (extractHeight(path) > -1) {
				resList.add(objectFile);
			}
		}
		return resList;
	}

	protected boolean isMultiHeight(XplaneObjectTagRule objectTagRule) {
		List<ObjectFile> objectsFiles = objectTagRule.getObjectsFiles();
		if (objectsFiles.size() < 2) {
			return false;
		}
		int heightCnt = 0;  
		for (ObjectFile objectFile : objectsFiles) {
			String path = objectFile.getPath();
			if (extractHeight(path) > -1) {
				heightCnt++;
			}
		}
		return heightCnt >= 2; //We need at leaset two objects with specified height to use this rule as heigh-supporting
	}
	
	protected double extractHeight(String fileName) {
		if (!fileName.endsWith(DsfObjectsProvider.OBJ_EXT)) {
			return -1;			
		}
		fileName = fileName.substring(0, fileName.length() - DsfObjectsProvider.OBJ_EXT.length());
		if (!fileName.endsWith("m")) {
			return -1;
		}
		int idx = Math.max(fileName.lastIndexOf('-'), fileName.lastIndexOf('_'));
		if (idx > 0) {
			String numStr = fileName.substring(idx+1, fileName.length() - 1);
			try {
				return Double.parseDouble(numStr);
			} catch (NumberFormatException e) {
				// Best effort
			}
		}
		return -1;
	}
	
	protected int getObjectFromRule(XplaneObjectTagRule rule, OsmPolygon osmPolygon) {
		int height = osmPolygon.getHeight();
		String choosedPath = null;
		if (isMultiHeight(rule)) {
			List<ObjectFile> noHeightFiles = getNoHeightFiles(rule);
			if (height > 0 || (height == 0 && noHeightFiles.isEmpty())) {
				double minDelta = Double.MAX_VALUE;
				for (ObjectFile objectFile : rule.getObjectsFiles()) {
					double delta = Math.abs(extractHeight(objectFile.getPath()) - height);
					if (delta < minDelta) {
						minDelta = delta;
						choosedPath = objectFile.getPath();
					}
				}
			} else {
				int i = new Random().nextInt(noHeightFiles.size());
				choosedPath = noHeightFiles.get(i).getPath();
			}
			int objectIndex = dsfObjectsProvider.getObjectIndex(choosedPath);
			if (objectIndex == -1) {
				Osm2xpLogger.error("Object " + choosedPath
						+ " is specified in rules, but can't be found in objects folder! Please check your rules against actually present objects");
			}
			return objectIndex;
		} else {
			return dsfObjectsProvider.getRandomObject(rule);
		}
	}

	private double calculateAngle(LinearRing2D polygon, XplaneObjectTagRule rule) {

		if (polygon.edges().size() == 4) { 
			
			if (rule.getxVectorMaxLength() > 0 && rule.getyVectorMaxLength() == 0) { //Simplified mode, we take only one interval into account and choose random edge to get 1 of 4 possible directions for model
				int edgeIdx = new Random().nextInt(polygon.edgeNumber());
				LineSegment2D edge = polygon.edge(edgeIdx);
				return GeomUtils.getTrueBearing(edge.lastPoint(), edge.firstPoint());
			}

			for (int i = 0; i < polygon.vertices().size() - 2; i++) {
				Point2D ptX = polygon.vertex(i);
				Point2D ptOrigin = polygon.vertex(i + 1);
				Point2D ptY = polygon.vertex(i + 2);

				double segmentX = GeomUtils.latLonDistance(ptX.y(), ptX.x(),
						ptOrigin.y(), ptOrigin.x());
				double segmentY = GeomUtils.latLonDistance(ptOrigin.y(),
						ptOrigin.x(), ptY.y(), ptY.x());
				// check if the rule x/y segments "fits" the current osm
				// polygon
				Boolean xVectorCheck = segmentX > rule.getxVectorMinLength()
						&& segmentX < rule.getxVectorMaxLength();
				Boolean yVectorCheck = segmentY > rule.getyVectorMinLength()
						&& segmentY < rule.getyVectorMaxLength();
				Boolean dimensionsCheck = xVectorCheck && yVectorCheck;

				// if that's the case, compute the rotation point (origin)
				// of
				// the object
				if (dimensionsCheck) {
					return GeomUtils.getTrueBearing(ptOrigin, ptY);
				}

			}

		}
		
		double maxLength = 0;
		LineSegment2D maxEdge = null;
		Collection<LineSegment2D> edges = polygon.edges();
		for (LineSegment2D lineSegment2D : edges) {
			double length = GeomUtils.computeLengthInMeters(lineSegment2D);
			if (length > maxLength) {
				maxLength = length;
				maxEdge = lineSegment2D;
			}
		}
		if (maxEdge != null) {
			return (GeomUtils.getTrueBearing(maxEdge.firstPoint(), maxEdge.lastPoint()) + 90) % 360;
		}
		return 0;
	}

	@Override
	public void translationComplete() {
		// Do nothing
	}
	
	@Override
	public String getId() {
		return "object";
	}

	@Override
	public boolean isTerminating() {
		return false;
	}
}
