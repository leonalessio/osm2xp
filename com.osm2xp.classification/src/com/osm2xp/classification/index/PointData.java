package com.osm2xp.classification.index;

import com.osm2xp.classification.index.KdTree.XYZPoint;

public class PointData<T> extends XYZPoint {

	private final T data;

	public PointData(double x, double y, T data) {
		super(x, y);
		this.data = data;
	}

	public T getData() {
		return data;
	}

}
