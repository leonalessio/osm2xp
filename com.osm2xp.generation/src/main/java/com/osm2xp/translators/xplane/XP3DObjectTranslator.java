package com.osm2xp.translators.xplane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.osm2xp.model.osm.polygon.OsmPolygon;
import com.osm2xp.model.osm.polygon.OsmPolyline;
import com.osm2xp.model.xplane.XplaneDsf3DObject;
import com.osm2xp.model.xplane.XplaneDsfObject;
import com.osm2xp.stats.StatsProvider;
import com.osm2xp.utils.DsfObjectsProvider;
import com.osm2xp.utils.geometry.GeomUtils;
import com.osm2xp.utils.osm.OsmUtils;
import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.options.GlobalOptionsProvider;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.generation.options.rules.XplaneObjectTagRule;
import com.osm2xp.writers.IWriter;

import math.geom2d.polygon.LinearRing2D;

public class XP3DObjectTranslator extends XPWritingTranslator {

	private DsfObjectsProvider dsfObjectsProvider;
	private XPOutputFormat outputFormat;

	public XP3DObjectTranslator(IWriter writer, DsfObjectsProvider dsfObjectsProvider, XPOutputFormat outputFormat) {
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
			XplaneDsfObject object = getRandomDsfObject((OsmPolygon) osmPolyline);
			if (object != null) {
				object.setPolygon((OsmPolygon) osmPolyline);
				try {
					writeObjectToDsf(object);
					return true;
				} catch (Osm2xpBusinessException e) {
					Osm2xpLogger.error(e.getMessage(), e);
				}

			}
		}
		if (osmPolyline instanceof OsmPolygon && XPlaneOptionsProvider.getOptions().isGenerateFor()) {
			Integer[] forestIndexAndDensity = dsfObjectsProvider
					.getRandomForestIndexAndDensity(osmPolyline.getTags());
			if (forestIndexAndDensity != null) {
				if (!osmPolyline.isValid()) {
					List<LinearRing2D> fixed = GeomUtils.fix((LinearRing2D)osmPolyline.getPolyline());
					for (LinearRing2D linearRing2D : fixed) {
						writer.write(outputFormat.getPolygonString(linearRing2D, forestIndexAndDensity[0] + "", forestIndexAndDensity[1] + ""));
					}
				} else {
					writeObjectToDsf((OsmPolygon) osmPolyline, forestIndexAndDensity);
				}
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
	public XplaneDsfObject getRandomDsfObject(OsmPolygon osmPolygon) {
		LinearRing2D polygon = osmPolygon.getPolygon();
		XplaneDsfObject result = null;
		// shuffle rules
		List<XplaneObjectTagRule> tagsRules = new ArrayList<XplaneObjectTagRule>();
		tagsRules.addAll(XPlaneOptionsProvider.getOptions().getObjectsRules()
				.getRules());
		Collections.shuffle(tagsRules);
		for (Tag tag : osmPolygon.getTags()) {
			for (XplaneObjectTagRule rule : tagsRules) {
				// check Tag matching
				if ((rule.getTag().getKey().equalsIgnoreCase("id") && rule
						.getTag().getValue()
						.equalsIgnoreCase(String.valueOf(osmPolygon.getId())))
						|| (OsmUtils.compareTags(rule.getTag(), tag))) {
					// check rule options

					Boolean areaOK = !rule.isAreaCheck()
							|| (rule.isAreaCheck() && (osmPolygon.getArea() > rule
									.getMinArea() && osmPolygon.getArea() < rule
									.getMaxArea()));

					Boolean sizeOK = !rule.isSizeCheck()
							|| GeomUtils.isRectangleBigEnoughForObject(
									rule.getxVectorMaxLength(),
									rule.getyVectorMaxLength(),
									rule.getxVectorMinLength(),
									rule.getyVectorMinLength(), polygon);

					Boolean checkSimplePoly = !rule.isSimplePolygonOnly()
							|| (rule.isSimplePolygonOnly() && osmPolygon
									.isSimplePolygon());

					if (areaOK && sizeOK && checkSimplePoly) {
						result = new XplaneDsf3DObject(osmPolygon, rule);
						// compute object index
						result.setDsfIndex(dsfObjectsProvider.getRandomObject(rule));

					}
				}
			}
		}
		return result;
	}

	@Override
	public void translationComplete() {
		// Do nothing
	}
	
	/**
	 * @param polygon
	 *            the forest polygon
	 * @param forestIndexAndDensity
	 *            index and density of the forest rule
	 */
	private void writeObjectToDsf(OsmPolygon osmPolygon, Integer[] forestIndexAndDensity) {
	
		writer.write(outputFormat.getPolygonString(osmPolygon, forestIndexAndDensity[0] + "", forestIndexAndDensity[1] + ""));
			
	}
	
	@Override
	public String getId() {
		return "forest";
	}

	@Override
	public boolean isTerminating() {
		return false;
	}
}
