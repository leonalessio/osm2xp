package com.osm2xp.translators.airfield;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.generation.collections.PointMap;

import math.geom2d.Point2D;

public class GetElevationCallable implements Callable<Map<Point2D, Double>> {

	private List<Point2D> points;

	public GetElevationCallable(List<Point2D> points) {
		this.points = points;
	}

	private String getPointStr(Point2D point2d) {
		return String.format(Locale.ROOT, "(%1.5f,%2.5f)", point2d.y(), point2d.x());
	}

	public List<Point2D> getPoints() {
		return points;
	}

	@Override
	public Map<Point2D, Double> call() throws Exception {
		Map<Point2D, Double> elevMap = new PointMap<Double>();
		try {
			String pointsStr = points.stream().map(point -> getPointStr(point)).collect(Collectors.joining(","));
			URL obj = new URL("https://elevation-api.io/api/elevation?points=" + pointsStr);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			// optional default is GET
			con.setRequestMethod("GET");
			// add request header
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			int responseCode = con.getResponseCode();
			if (responseCode != 200) {
				Osm2xpLogger.error("Error getting airfield elevations from elevation-api.io - code " + 
				responseCode + " returned with message " + con.getResponseMessage());
				return null;
			}
			
			try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				JSONParser parser = new JSONParser();
				JSONObject root = (JSONObject) parser.parse(response.toString());
				JSONArray array = (JSONArray) root.get("elevations");
				if (array.size() == points.size()) {
					for (int i = 0; i < array.size(); i++) {
						JSONObject current = (JSONObject) array.get(i);
						Double ele = (Double) current.get("elevation");
						if (ele > -9999) {
							elevMap.put(points.get(i), ele);
						}
					}
				}
			} catch (IOException e) {
				Osm2xpLogger.log(e);
			}
		} catch (Exception e) {
			Osm2xpLogger.error("Error getting airfield elevations from elevation-api.io - ");
			Osm2xpLogger.log(e);
		} 
		return elevMap;
	}

}
