package com.osm2xp.utils;

import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.Math.log;
import static java.lang.Math.sinh;
import static java.lang.Math.tan;
import math.geom2d.Point2D;

/**
 * Mercator.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class Mercator {

	public static Point2D mercToPlanar(Point2D pt) {
		return new Point2D(pt.y(), log(tan(PI / 4 + pt.x() / 2)));
	}

	public static Point2D planarToMerc(Point2D pt) {
		return new Point2D(atan(sinh(pt.x())), pt.y());
	}
}