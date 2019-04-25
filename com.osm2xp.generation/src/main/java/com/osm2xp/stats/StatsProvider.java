package com.osm2xp.stats;

import java.util.HashMap;
import java.util.Map;

import math.geom2d.Point2D;

public class StatsProvider {
	
	private static CountStats commonStats = new CountStats();
	
	private static Map<Point2D, CountStats> tileStatsMap = new HashMap<Point2D, CountStats>();

	public static void reinit() {
		commonStats = new CountStats();
		tileStatsMap.clear();
	}
	
	public static CountStats getCommonStats() {
		if (commonStats == null) {
			throw new IllegalStateException("StatsProvider should be re-initialized first!");
		}
		return commonStats;
	}
	
	public static CountStats getTileStats(Point2D tile, boolean createIfAbsent) {
		CountStats countStats = tileStatsMap.get(tile);
		if (countStats == null && createIfAbsent) {
			countStats = new CountStats();
			tileStatsMap.put(tile, countStats);
		}
		return countStats;
				
	}
	
}
