package com.osm2xp.translators.impl;

import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.TopologyException;
import org.locationtech.jts.precision.GeometryPrecisionReducer;
import java.util.ArrayList;
import java.util.List;

public class GeometryClipper {
	private static int RIGHT = 2;
	private static int TOP = 8;
	private static int BOTTOM = 4;
	private static int LEFT = 1;
	final double xmin;
	final double ymin;
	final double xmax;
	final double ymax;
	final Envelope bounds;

	public Envelope getBounds() {
		return this.bounds;
	}

	public GeometryClipper(Envelope bounds) {
		this.xmin = bounds.getMinX();
		this.ymin = bounds.getMinY();
		this.xmax = bounds.getMaxX();
		this.ymax = bounds.getMaxY();
		this.bounds = bounds;
	}

	public Geometry clipSafe(Geometry g, boolean ensureValid, double scale) {
		try {
			return this.clip(g, ensureValid);
		} catch (TopologyException arg10) {
			try {
				if ((g instanceof Polygon || g instanceof MultiPolygon) && !g.isValid()) {
					return this.clip(g.buffer(0.0D), ensureValid);
				}
			} catch (TopologyException arg9) {
				;
			}

			if (scale != 0.0D) {
				try {
					GeometryPrecisionReducer reducer = new GeometryPrecisionReducer(new PrecisionModel(scale));
					Geometry reduced = reducer.reduce(g);
					if (reduced.isEmpty()) {
						throw new TopologyException("Could not snap geometry to precision model");
					}

					return this.clip(reduced, ensureValid);
				} catch (TopologyException arg8) {
					;
				}
			}

			if (ensureValid) {
				try {
					return this.clip(g, false);
				} catch (TopologyException arg7) {
					;
				}
			}

			return g;
		}
	}

	public Geometry clip(Geometry g, boolean ensureValid) {
		if (g == null) {
			return null;
		} else {
			Envelope geomEnvelope = g.getEnvelopeInternal();
			if (geomEnvelope.isNull()) {
				return null;
			} else if (this.bounds.contains(geomEnvelope)) {
				return g;
			} else if (!this.bounds.intersects(geomEnvelope)) {
				return null;
			} else if (g instanceof LineString) {
				return this.clipLineString((LineString) g);
			} else if (g instanceof Polygon) {
				if (ensureValid) {
					GeometryFactory gf = g.getFactory();
					CoordinateSequenceFactory csf = gf.getCoordinateSequenceFactory();
					Polygon fence = gf.createPolygon(this.buildBoundsString(gf, csf), (LinearRing[]) null);
					return g.intersection(fence);
				} else {
					return this.clipPolygon((Polygon) g);
				}
			} else {
				return g instanceof GeometryCollection ? this.clipCollection((GeometryCollection) g, ensureValid) : g;
			}
		}
	}

	private int computeOutCode(double x, double y, double xmin, double ymin, double xmax, double ymax) {
		int code = 0;
		if (y > ymax) {
			code |= TOP;
		} else if (y < ymin) {
			code |= BOTTOM;
		}

		if (x > xmax) {
			code |= RIGHT;
		} else if (x < xmin) {
			code |= LEFT;
		}

		return code;
	}

	private double[] clipSegment(double[] segment) {
		double x0 = segment[0];
		double y0 = segment[1];
		double x1 = segment[2];
		double y1 = segment[3];
		int outcode0 = this.computeOutCode(x0, y0, this.xmin, this.ymin, this.xmax, this.ymax);
		int outcode1 = this.computeOutCode(x1, y1, this.xmin, this.ymin, this.xmax, this.ymax);
		int step = 0;

		while ((outcode0 | outcode1) != 0) {
			if ((outcode0 & outcode1) > 0) {
				return null;
			}

			int outcodeOut = outcode0 != 0 ? outcode0 : outcode1;
			double y;
			double x;
			if ((outcodeOut & TOP) > 0) {
				x = x0 + (x1 - x0) * (this.ymax - y0) / (y1 - y0);
				y = this.ymax;
			} else if ((outcodeOut & BOTTOM) > 0) {
				x = x0 + (x1 - x0) * (this.ymin - y0) / (y1 - y0);
				y = this.ymin;
			} else if ((outcodeOut & RIGHT) > 0) {
				y = y0 + (y1 - y0) * (this.xmax - x0) / (x1 - x0);
				x = this.xmax;
			} else {
				y = y0 + (y1 - y0) * (this.xmin - x0) / (x1 - x0);
				x = this.xmin;
			}

			if (outcodeOut == outcode0) {
				x0 = x;
				y0 = y;
				outcode0 = this.computeOutCode(x, y, this.xmin, this.ymin, this.xmax, this.ymax);
			} else {
				x1 = x;
				y1 = y;
				outcode1 = this.computeOutCode(x, y, this.xmin, this.ymin, this.xmax, this.ymax);
			}

			++step;
			if (step >= 5) {
				throw new RuntimeException("Algorithm did not converge");
			}
		}

		if (x0 == x1 && y0 == y1) {
			return null;
		} else {
			segment[0] = x0;
			segment[1] = y0;
			segment[2] = x1;
			segment[3] = y1;
			return segment;
		}
	}

	private boolean outside(double x0, double y0, double x1, double y1) {
		int outcode0 = this.computeOutCode(x0, y0, this.xmin, this.ymin, this.xmax, this.ymax);
		int outcode1 = this.computeOutCode(x1, y1, this.xmin, this.ymin, this.xmax, this.ymax);
		return (outcode0 & outcode1) > 0;
	}

	private boolean contained(double x, double y) {
		return x > this.xmin && x < this.xmax && y > this.ymin && y < this.ymax;
	}

	private Geometry clipPolygon(Polygon polygon) {
		GeometryFactory gf = polygon.getFactory();
		LinearRing exterior = (LinearRing) polygon.getExteriorRing();
		LinearRing shell = this.polygonClip(exterior);
		shell = this.cleanupRings(shell);
		if (shell == null) {
			return null;
		} else {
			ArrayList<LinearRing> holes = new ArrayList<LinearRing>();

			for (int i = 0; i < polygon.getNumInteriorRing(); ++i) {
				LinearRing hole = (LinearRing) polygon.getInteriorRingN(i);
				hole = this.polygonClip(hole);
				hole = this.cleanupRings(hole);
				if (hole != null) {
					holes.add(hole);
				}
			}

			return gf.createPolygon(shell, (LinearRing[]) ((LinearRing[]) holes.toArray(new LinearRing[holes.size()])));
		}
	}

	private LinearRing cleanupRings(LinearRing ring) {
		if (ring != null && !ring.isEmpty()) {
			CoordinateSequence cs = ring.getCoordinateSequence();
			double px = cs.getX(0);
			double py = cs.getY(0);
			boolean fullyOnBorders = true;

			for (int i = 1; i < cs.size() && fullyOnBorders; ++i) {
				double x = cs.getX(i);
				double y = cs.getY(i);
				if ((x != px || x != this.xmin && x != this.xmax) && (y != py || y != this.ymin && y != this.ymax)) {
					fullyOnBorders = false;
				} else {
					px = x;
					py = y;
				}
			}

			return fullyOnBorders ? (ring.getFactory().createPolygon(ring).getArea() > 0.0D ? ring : null) : ring;
		} else {
			return null;
		}
	}

	private LinearRing polygonClip(LinearRing ring) {
//		double INFINITY = Double.MAX_VALUE;
		CoordinateSequence cs = ring.getCoordinateSequence();
		Ordinates out = new Ordinates();

		for (int i = 0; i < cs.size() - 1; ++i) {
			double x0 = cs.getOrdinate(i, 0);
			double x1 = cs.getOrdinate(i + 1, 0);
			double y0 = cs.getOrdinate(i, 1);
			double y1 = cs.getOrdinate(i + 1, 1);
			double deltaX = x1 - x0;
			double deltaY = y1 - y0;
			double xIn;
			double xOut;
			if (deltaX <= 0.0D && (deltaX != 0.0D || x0 <= this.xmax)) {
				xIn = this.xmax;
				xOut = this.xmin;
			} else {
				xIn = this.xmin;
				xOut = this.xmax;
			}

			double yIn;
			double yOut;
			if (deltaY <= 0.0D && (deltaY != 0.0D || y0 <= this.ymax)) {
				yIn = this.ymax;
				yOut = this.ymin;
			} else {
				yIn = this.ymin;
				yOut = this.ymax;
			}

			double tOutX;
			if (deltaX != 0.0D) {
				tOutX = (xOut - x0) / deltaX;
			} else if (x0 <= this.xmax && this.xmin <= x0) {
				tOutX = Double.MAX_VALUE;
			} else {
				tOutX = -1.7976931348623157E308D;
			}

			double tOutY;
			if (deltaY != 0.0D) {
				tOutY = (yOut - y0) / deltaY;
			} else if (y0 <= this.ymax && this.ymin <= y0) {
				tOutY = Double.MAX_VALUE;
			} else {
				tOutY = -1.7976931348623157E308D;
			}

			double tOut1;
			double tOut2;
			if (tOutX < tOutY) {
				tOut1 = tOutX;
				tOut2 = tOutY;
			} else {
				tOut1 = tOutY;
				tOut2 = tOutX;
			}

			if (tOut2 > 0.0D) {
				double tInX;
				if (deltaX != 0.0D) {
					tInX = (xIn - x0) / deltaX;
				} else {
					tInX = -1.7976931348623157E308D;
				}

				double tInY;
				if (deltaY != 0.0D) {
					tInY = (yIn - y0) / deltaY;
				} else {
					tInY = -1.7976931348623157E308D;
				}

				double tIn2;
				if (tInX < tInY) {
					tIn2 = tInY;
				} else {
					tIn2 = tInX;
				}

				if (tOut1 < tIn2) {
					if (0.0D < tOut1 && tOut1 <= 1.0D) {
						if (tInX < tInY) {
							out.add(xOut, yIn);
						} else {
							out.add(xIn, yOut);
						}
					}
				} else if (0.0D < tOut1 && tIn2 <= 1.0D) {
					if (0.0D <= tIn2) {
						if (tInX > tInY) {
							out.add(xIn, y0 + tInX * deltaY);
						} else {
							out.add(x0 + tInY * deltaX, yIn);
						}
					}

					if (1.0D >= tOut1) {
						if (tOutX < tOutY) {
							out.add(xOut, y0 + tOutX * deltaY);
						} else {
							out.add(x0 + tOutY * deltaX, yOut);
						}
					} else {
						out.add(x1, y1);
					}
				}

				if (0.0D < tOut2 && tOut2 <= 1.0D) {
					out.add(xOut, yOut);
				}
			}
		}

		if (out.size() < 3) {
			return null;
		} else {
			if (out.getOrdinate(0, 0) == out.getOrdinate(out.size() - 1, 0)
					&& out.getOrdinate(0, 1) == out.getOrdinate(out.size() - 1, 1)) {
				if (out.size() == 3) {
					return null;
				}
			} else {
				out.add(out.getOrdinate(0, 0), out.getOrdinate(0, 1));
			}

			return ring.getFactory()
					.createLinearRing(out.toCoordinateSequence(ring.getFactory().getCoordinateSequenceFactory()));
		}
	}

	LinearRing buildBoundsString(GeometryFactory gf, CoordinateSequenceFactory csf) {
		CoordinateSequence cs = CSUtil.createCS(csf, 5, 2);
		cs.setOrdinate(0, 0, this.xmin);
		cs.setOrdinate(0, 1, this.ymin);
		cs.setOrdinate(1, 0, this.xmin);
		cs.setOrdinate(1, 1, this.ymax);
		cs.setOrdinate(2, 0, this.xmax);
		cs.setOrdinate(2, 1, this.ymax);
		cs.setOrdinate(3, 0, this.xmax);
		cs.setOrdinate(3, 1, this.ymin);
		cs.setOrdinate(4, 0, this.xmin);
		cs.setOrdinate(4, 1, this.ymin);
		return gf.createLinearRing(cs);
	}

	private Geometry clipCollection(GeometryCollection gc, boolean ensureValid) {
		if (gc.getNumGeometries() == 1) {
			return this.clip(gc.getGeometryN(0), ensureValid);
		} else {
			ArrayList<Geometry> result = new ArrayList<Geometry>(gc.getNumGeometries());

			for (int i = 0; i < gc.getNumGeometries(); ++i) {
				Geometry clipped = this.clip(gc.getGeometryN(i), ensureValid);
				if (clipped != null) {
					result.add(clipped);
				}
			}

			if (result.size() == 0) {
				return null;
			} else if (result.size() == 1) {
				return (Geometry) result.get(0);
			} else {
				this.flattenCollection(result);
				if (gc instanceof MultiPoint) {
					return gc.getFactory()
							.createMultiPoint((Point[]) ((Point[]) result.toArray(new Point[result.size()])));
				} else if (gc instanceof MultiLineString) {
					return gc.getFactory().createMultiLineString(
							(LineString[]) ((LineString[]) result.toArray(new LineString[result.size()])));
				} else if (gc instanceof MultiPolygon) {
					return gc.getFactory()
							.createMultiPolygon((Polygon[]) ((Polygon[]) result.toArray(new Polygon[result.size()])));
				} else {
					return gc.getFactory().createGeometryCollection(
							(Geometry[]) ((Geometry[]) result.toArray(new Geometry[result.size()])));
				}
			}
		}
	}

	private void flattenCollection(List<Geometry> result) {
		int i = 0;

		while (true) {
			while (i < result.size()) {
				Geometry g = (Geometry) result.get(i);
				if (g instanceof GeometryCollection) {
					GeometryCollection gc = (GeometryCollection) g;

					for (int j = 0; j < gc.getNumGeometries(); ++j) {
						result.add(gc.getGeometryN(j));
					}

					result.remove(i);
				} else {
					++i;
				}
			}

			return;
		}
	}

	Geometry clipLineString(LineString line) {
		ArrayList<LineString> clipped = new ArrayList<LineString>();
		GeometryFactory gf = line.getFactory();
		CoordinateSequenceFactory csf = gf.getCoordinateSequenceFactory();
		CoordinateSequence coords = line.getCoordinateSequence();
		Ordinates ordinates = new Ordinates(coords.size());
		double x0 = coords.getX(0);
		double y0 = coords.getY(0);
		boolean prevInside = this.contained(x0, y0);
		if (prevInside) {
			ordinates.add(x0, y0);
		}

		double[] segment = new double[4];
		int size = coords.size();

		for (int cs0 = 1; cs0 < size; ++cs0) {
			double cs1 = coords.getX(cs0);
			double i = coords.getY(cs0);
			boolean inside = this.contained(cs1, i);
			double[] clippedSegment;
			if (inside == prevInside) {
				if (inside) {
					ordinates.add(cs1, i);
				} else if (!this.outside(x0, y0, cs1, i)) {
					segment[0] = x0;
					segment[1] = y0;
					segment[2] = cs1;
					segment[3] = i;
					clippedSegment = this.clipSegment(segment);
					if (clippedSegment != null) {
						CoordinateSequence sequence1 = CSUtil.createCS(csf, 2, 2);
						sequence1.setOrdinate(0, 0, clippedSegment[0]);
						sequence1.setOrdinate(0, 1, clippedSegment[1]);
						sequence1.setOrdinate(1, 0, clippedSegment[2]);
						sequence1.setOrdinate(1, 1, clippedSegment[3]);
						clipped.add(gf.createLineString(sequence1));
					}
				}
			} else {
				segment[0] = x0;
				segment[1] = y0;
				segment[2] = cs1;
				segment[3] = i;
				clippedSegment = this.clipSegment(segment);
				if (clippedSegment != null) {
					if (prevInside) {
						ordinates.add(clippedSegment[2], clippedSegment[3]);
					} else {
						ordinates.add(clippedSegment[0], clippedSegment[1]);
						ordinates.add(clippedSegment[2], clippedSegment[3]);
					}

					if (prevInside) {
						clipped.add(gf.createLineString(ordinates.toCoordinateSequence(csf)));
						ordinates.clear();
					}
				} else {
					prevInside = false;
				}
			}

			prevInside = inside;
			x0 = cs1;
			y0 = i;
		}

		if (ordinates.size() > 1) {
			clipped.add(gf.createLineString(ordinates.toCoordinateSequence(csf)));
		}

		if (line.isClosed() && clipped.size() > 1) {
			CoordinateSequence arg21 = ((LineString) clipped.get(0)).getCoordinateSequence();
			CoordinateSequence arg22 = ((LineString) clipped.get(clipped.size() - 1)).getCoordinateSequence();
			if (arg21.getOrdinate(0, 0) == arg22.getOrdinate(arg22.size() - 1, 0)
					&& arg21.getOrdinate(0, 1) == arg22.getOrdinate(arg22.size() - 1, 1)) {
				CoordinateSequence cs = CSUtil.createCS(csf, arg21.size() + arg22.size() - 1, 2);

				int arg23;
				for (arg23 = 0; arg23 < arg22.size(); ++arg23) {
					cs.setOrdinate(arg23, 0, arg22.getOrdinate(arg23, 0));
					cs.setOrdinate(arg23, 1, arg22.getOrdinate(arg23, 1));
				}

				for (arg23 = 1; arg23 < arg21.size(); ++arg23) {
					cs.setOrdinate(arg23 + arg22.size() - 1, 0, arg21.getOrdinate(arg23, 0));
					cs.setOrdinate(arg23 + arg22.size() - 1, 1, arg21.getOrdinate(arg23, 1));
				}

				clipped.remove(0);
				clipped.remove(clipped.size() - 1);
				clipped.add(gf.createLineString(cs));
			}
		}

		return (Geometry) (clipped.size() > 1
				? gf.createMultiLineString(
						(LineString[]) ((LineString[]) clipped.toArray(new LineString[clipped.size()])))
				: (clipped.size() == 1 ? (Geometry) clipped.get(0) : null));
	}
}