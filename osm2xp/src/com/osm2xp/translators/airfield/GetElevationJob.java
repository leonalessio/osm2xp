package com.osm2xp.translators.airfield;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.osm2xp.gui.Activator;

import math.geom2d.Point2D;

public class GetElevationJob extends Job {

	private List<Point2D> points;
	private List<Double> result;

	public GetElevationJob(List<Point2D> points) {
		super("Getting elevation - " + points.size() + " points");
		this.points = points;
		result = new ArrayList<>(points.size());
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		// https://elevation-api.io/api/elevation?points=(39.90974,-106.17188),(62.52417,10.02487)
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
				Activator.log(IStatus.ERROR, "Error getting airfield elevations from elevation-api.io - code " + 
				responseCode + " returned with message " + con.getResponseMessage());
				return Status.OK_STATUS;
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
				for (int i = 0; i < array.size(); i++) {
					JSONObject current = (JSONObject) array.get(i);
					Double ele = (Double) current.get("elevation");
				}
			} catch (IOException e) {
				Activator.log(e);
			}
		} catch (Exception e) {
			Activator.log(IStatus.ERROR, "Error getting airfield elevations from elevation-api.io - ");
			Activator.log(e);
		} 
		return Status.OK_STATUS;
	}

	private String getPointStr(Point2D point2d) {
		return String.format("(%1.5f,%2.5f)", point2d.y, point2d.x);
	}

	public List<Point2D> getPoints() {
		return points;
	}

	public List<Double> getElevations() {
		return result;
	}


}
