package com.osm2xp.translators.xplane;

import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.utils.DsfObjectsProvider;
import com.osm2xp.writers.IHeaderedWriter;

import math.geom2d.Point2D;

/**
 * Xplane 10/11 translator implementation. Generates XPlane scenery from osm data.
 * 
 * @author Benjamin Blanchet, Dmitry Karpenko
 * 
 */
public class Xplane10TranslatorImpl extends XPlaneTranslatorImpl {

	/**
	 * Constructor.
	 * 
	 * @param stats
	 *            stats object.
	 * @param writer
	 *            file writer.
	 * @param currentTile
	 *            current lat/lon tile.
	 * @param folderPath
	 *            generated scenery folder path.
	 * @param dsfObjectsProvider
	 *            dsf object provider.
	 */
	public Xplane10TranslatorImpl(IHeaderedWriter writer,
			Point2D currentTile, String folderPath,
			DsfObjectsProvider dsfObjectsProvider) {
		super(writer, currentTile, folderPath, dsfObjectsProvider);
	}
	
	@Override
	protected XPOutputFormat createOutputFormat() {
		return new XP10OutputFormat(XPlaneOptionsProvider.getOptions().getObjectRenderLevel(), XPlaneOptionsProvider.getOptions().getFacadeRenderLevel());
	}
	
}
