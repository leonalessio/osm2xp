package com.osm2xp.translators.xplane;

import static com.osm2xp.translators.xplane.XPlaneTranslatorImpl.LINE_SEP;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import com.osm2xp.core.constants.CoreConstants;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.model.osm.polygon.OsmMultiPolygon;
import com.osm2xp.model.osm.polygon.OsmPolygon;
import com.osm2xp.utils.DsfObjectsProvider;
import com.osm2xp.utils.geometry.GeomUtils;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.polygon.LinearCurve2D;
import math.geom2d.polygon.LinearRing2D;

public class XPOutputFormat {
	
	public static final String EXCLUSION_PLACEHOLDER = "{EXCLUSIONS}";

	/**
	 * Please see <a href=
	 * "https://forums.x-plane.org/index.php?/forums/topic/151582-osm2xp-30/&do=findComment&comment=1447720">thi
	 * thread for details</a> For now no cutting into parts, we just allow max 255
	 * windings - 1 outer and up to 254 inner
	 */
	private static final int MAX_INNER_POLYS = 254;

	public String getHeaderString(Point2D tileCoordinates, Box2D exclusionBox, DsfObjectsProvider dsfObjectsProvider) {

		StringBuilder sb = new StringBuilder();
		int latitude = (int) tileCoordinates.y();
		int longitude = (int) tileCoordinates.x();

		sb.append("I\n");
		sb.append("800\n");
		sb.append("DSF2TEXT\n\n");
		sb.append("PROPERTY sim/planet earth\n");
		sb.append("PROPERTY sim/overlay 1\n");
		// if (XPlaneOptionsProvider.getOptions().isGenerateStreetLights()) {
		// // we set the require index so only streetlights objects will
		// // disappear when using the objects number slider in xplane
		// int requireIndex = dsfObjectsProvider.getStreetLightObjectsList()
		// .size();
		// sb.append("PROPERTY sim/require_object 1/" + requireIndex + "\n");
		// } else {
		sb.append("PROPERTY sim/require_object 1/0\n");
		// }
		sb.append("PROPERTY sim/require_facade 6/0\n");
		sb.append("PROPERTY sim/creation_agent Osm2Xp " + CoreConstants.OSM2XP_VERSION + " by Benjamin Blanchet, Dmitry Karpenko \n");
		// Exclusions
		String exclusionCoordinate = exclusionBox != null
				? formatDsfCoord(exclusionBox.getMinX()) + "/" + formatDsfCoord(exclusionBox.getMinY()) + "/"
						+ formatDsfCoord(exclusionBox.getMaxX()) + "/" + formatDsfCoord(exclusionBox.getMaxY()) + "\n"
				: longitude + ".000000/" + latitude + ".000000/" + (longitude + 1) + ".000000/" + (latitude + 1)
						+ ".000000\n";
		sb.append(getDsfExclusions(exclusionCoordinate));
		sb.append("PROPERTY sim/west " + longitude + "\n");
		sb.append("PROPERTY sim/east " + (longitude + 1) + "\n");
		sb.append("PROPERTY sim/north " + (latitude + 1) + "\n");
		sb.append("PROPERTY sim/south " + latitude + "\n\n");

		// forests files
		if (dsfObjectsProvider.getForestsList() != null) {
			for (String forest : dsfObjectsProvider.getForestsList()) {
				sb.append("POLYGON_DEF " + forest + "\n");
			}
		}

		// facades rules
		if (dsfObjectsProvider.getSinglesFacadesList() != null) {
			for (String facadeTagRule : dsfObjectsProvider.getSinglesFacadesList()) {
				sb.append("POLYGON_DEF " + facadeTagRule + "\n");
			}
		}

		// facades files
		if (dsfObjectsProvider.getFacadesList() != null) {
			for (String facade : dsfObjectsProvider.getFacadesList()) {
				String facadeDeclaration = null;
				if (!XPlaneOptionsProvider.getOptions().isPackageFacades()) {

					facadeDeclaration = "POLYGON_DEF /lib/osm2xp/facades/" + facade + "\n";
				} else {
					facadeDeclaration = "POLYGON_DEF facades/" + facade + "\n";
				}
				sb.append(facadeDeclaration);
			}
			sb.append("\n");
		}

		if (dsfObjectsProvider.getObjectsList() != null) {
			for (String objectPath : dsfObjectsProvider.getObjectsList()) {
				sb.append("OBJECT_DEF " + objectPath + "\n");
			}
		}

		sb.append("NETWORK_DEF lib/g10/roads.net\n");

		return sb.toString();
	}

	public String getPolygonString(LinearCurve2D poly, String arg1, String arg2) {
		return getPolygonString(poly, null, arg1, arg2);
	}

	public String getPolygonString(LinearCurve2D poly, List<? extends LinearCurve2D> innerPolys, String arg1, String arg2) {
		StringBuilder sb = new StringBuilder();
		sb.append("BEGIN_POLYGON " + arg1 + " " + arg2 + " 2");
		sb.append(XPlaneTranslatorImpl.LINE_SEP);
		sb.append(getWindingStr(poly.vertices()));

		if (innerPolys != null && !innerPolys.isEmpty()) {
			if (innerPolys.size() > MAX_INNER_POLYS) {
				Osm2xpLogger.warning( "255 windings at most supported for polygon, current polygon has "
						+ innerPolys.size() + ". Only first 255 would be used.");
				innerPolys = innerPolys.subList(0, MAX_INNER_POLYS);
			}
			for (LinearCurve2D polyline2d : innerPolys) {
				sb.append(getWindingStr(GeomUtils.forceCW((LinearRing2D) polyline2d).vertices()));
			}
		}

		sb.append("END_POLYGON");
		sb.append(XPlaneTranslatorImpl.LINE_SEP);
		return sb.toString();
	}

	protected String getWindingStr(Collection<Point2D> vertices) {
		if (vertices instanceof List && vertices.size() > 1
				&& ((List<Point2D>) vertices).get(0).equals(((List<Point2D>) vertices).get(vertices.size() - 1))) {
			((List<Point2D>) vertices).remove(vertices.size() - 1);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("BEGIN_WINDING");
		sb.append(XPlaneTranslatorImpl.LINE_SEP);
		for (Point2D loc : vertices) {
			sb.append(String.format(Locale.ROOT, "POLYGON_POINT %1.9f %2.9f", loc.x(), loc.y()));
			sb.append(XPlaneTranslatorImpl.LINE_SEP);
		}
		sb.append("END_WINDING");
		sb.append(XPlaneTranslatorImpl.LINE_SEP);
		return sb.toString();
	}

	public String getPolygonString(OsmPolygon osmPolygon, String arg1, String arg2) {
		if (osmPolygon instanceof OsmMultiPolygon) {
			return getPolygonString(osmPolygon.getPolygon(), ((OsmMultiPolygon) osmPolygon).getInnerPolys(), arg1,
					arg2);
		}
		return getPolygonString(osmPolygon.getPolygon(), arg1, arg2);
	}

	public String getPathStr(XPPathSegment pathSegment) {
		StringBuilder builder = new StringBuilder();
		if (XPlaneOptionsProvider.getOptions().isGenerateComments() && pathSegment.getComment() != null) {
			builder.append("#");
			builder.append(pathSegment.getComment());
			builder.append(LINE_SEP);
		}
		Point2D[] points = pathSegment.getPoints();
		builder.append(String.format(Locale.ROOT, "BEGIN_SEGMENT 0 %d %d %3.9f %4.9f %5.9f", pathSegment.getType(),
				pathSegment.getStartId(), points[0].x(), points[0].y(), pathSegment.getStartHeight()));
		builder.append(LINE_SEP);
		for (int i = 1; i < points.length - 1; i++) {
			builder.append(String.format(Locale.ROOT, "SHAPE_POINT %1.9f %2.9f 0.000000000", points[i].x(), points[i].y()));
			builder.append(LINE_SEP);
		}
		builder.append(String.format(Locale.ROOT, "END_SEGMENT %d %2.9f %3.9f %4.9f", pathSegment.getEndId(),
				points[points.length - 1].x(), points[points.length - 1].y(), pathSegment.getEndHeight()));
		builder.append(LINE_SEP);
		return builder.toString();
	}

	
	/**
	 * @param tileCoordinate
	 * @return
	 */
	protected String getDsfExclusions(String tileCoordinate) {
		StringBuilder sb = new StringBuilder();
		// Exclusions
		if (XPlaneOptionsProvider.getOptions().isExcludeObj()) {
//			// smart OBJ exclusion
//			if (XPlaneOptionsProvider.getOptions().isSmartExclusions()) {
//				sb.append(EXCLUSION_PLACEHOLDER + "\n");
//			} else {
			sb.append("PROPERTY sim/exclude_obj " + tileCoordinate);
//			}
		}
		if (XPlaneOptionsProvider.getOptions().isExcludeFac()) {
			sb.append("PROPERTY sim/exclude_fac " + tileCoordinate);
		}
		if (XPlaneOptionsProvider.getOptions().isExcludeFor()) {
			sb.append("PROPERTY sim/exclude_for " + tileCoordinate);
		}
		if (XPlaneOptionsProvider.getOptions().isExcludeNet()) {
			sb.append("PROPERTY sim/exclude_net " + tileCoordinate);
		}
		if (XPlaneOptionsProvider.getOptions().isExcludeLin()) {
			sb.append("PROPERTY sim/exclude_lin " + tileCoordinate);
		}
		if (XPlaneOptionsProvider.getOptions().isExcludeStr()) {
			sb.append("PROPERTY sim/exclude_str " + tileCoordinate);
		}
	
		return sb.toString();
	}

	/**
	 * Formats lat/long to string with 9 chars after "."
	 * @param coord coord value to format
	 * @return formatted string
	 */
	protected static String formatDsfCoord(double coord) {
		return String.format(Locale.ROOT, "%.9f", coord);
	}

}
