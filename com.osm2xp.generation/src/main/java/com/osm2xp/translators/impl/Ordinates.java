package com.osm2xp.translators.impl;

import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFactory;

class Ordinates {
	int curr;
	double[] ordinates;

	public Ordinates() {
		this.ordinates = new double[10];
		this.curr = -1;
	}

	public Ordinates(int capacity) {
		this.ordinates = new double[capacity];
		this.curr = -1;
	}

	public CoordinateSequence toCoordinateSequence(CoordinateSequenceFactory csfac) {
		CoordinateSequence cs = CSUtil.createCS(csfac, this.size(), 2);

		for (int i = 0; i <= this.curr; ++i) {
			cs.setOrdinate(i, 0, this.ordinates[i * 2]);
			cs.setOrdinate(i, 1, this.ordinates[i * 2 + 1]);
		}

		return cs;
	}

	int size() {
		return this.curr + 1;
	}

	void add(double x, double y) {
		++this.curr;
		if (this.curr * 2 + 1 >= this.ordinates.length) {
			int newSize = this.ordinates.length * 3 / 2;
			if (newSize < 10) {
				newSize = 10;
			}

			double[] resized = new double[newSize];
			System.arraycopy(this.ordinates, 0, resized, 0, this.ordinates.length);
			this.ordinates = resized;
		}

		this.ordinates[this.curr * 2] = x;
		this.ordinates[this.curr * 2 + 1] = y;
	}

	void clear() {
		this.curr = -1;
	}

	double getOrdinate(int coordinate, int ordinate) {
		return this.ordinates[coordinate * 2 + ordinate];
	}

	public void init(CoordinateSequence cs) {
		this.clear();

		for (int i = 0; i < cs.size(); ++i) {
			this.add(cs.getOrdinate(i, 0), cs.getOrdinate(i, 1));
		}

	}

	public String toString() {
		StringBuilder sb = new StringBuilder("Ordinates[");

		for (int i = 0; i <= this.curr; ++i) {
			sb.append(this.ordinates[i * 2]);
			sb.append(" ");
			sb.append(this.ordinates[i * 2 + 1]);
			if (i < this.curr) {
				sb.append(";");
			}
		}

		sb.append("]");
		return sb.toString();
	}
}