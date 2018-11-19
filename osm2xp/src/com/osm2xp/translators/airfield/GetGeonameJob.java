package com.osm2xp.translators.airfield;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.geonames.Toponym;
import org.geonames.WebService;

import com.osm2xp.gui.Activator;

import math.geom2d.Point2D;

public class GetGeonameJob extends Job {

	private Point2D coords;
	List<Toponym> result;

	public GetGeonameJob(Point2D coords) {
		super("Get geoname for " + coords);
		this.coords = coords;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		WebService.setUserName("osm2xp");
		try {
			result = WebService.findNearbyPlaceName(coords.y, coords.x);
		} catch (Exception e) {
			Activator.log(e);
		}
		return Status.OK_STATUS;
	}

	public String getObtainedName() {
		if (result != null && result.size() > 0) {
			return result.get(0).getName();
		}
		return null;
	}

	public Point2D getCoords() {
		return coords;
	}
	
}
