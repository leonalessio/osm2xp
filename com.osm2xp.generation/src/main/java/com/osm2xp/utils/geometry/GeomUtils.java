package com.osm2xp.utils.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.locationtech.jts.algorithm.Centroid;
import org.locationtech.jts.algorithm.Orientation;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateArrays;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineSegment;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.TopologyException;
import org.locationtech.jts.geom.impl.CoordinateArraySequenceFactory;
import org.locationtech.jts.geom.util.LineStringExtracter;
import org.locationtech.jts.operation.buffer.BufferOp;
import org.locationtech.jts.operation.polygonize.Polygonizer;
import org.locationtech.jts.operation.valid.IsValidOp;
import org.locationtech.jts.operation.valid.TopologyValidationError;

import com.osm2xp.core.model.osm.Node;
import com.osm2xp.model.geom.Lod13Location;

import math.geom2d.Angle2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.line.Line2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.polygon.LinearCurve2D;
import math.geom2d.polygon.LinearRing2D;
import math.geom2d.polygon.Polyline2D;
import math.geom2d.polygon.Rectangle2D;

/**
 * GeomUtils.
 * 
 * @author Benjamin Blanchet, Dmitry Karpenko
 * 
 */
public class GeomUtils {
	
	/** */
    private static final Comparator<Polygon> POLYGON_AREA_COMPARATOR = new Comparator<Polygon>() {
            public int compare (Polygon p1, Polygon p2) {
                double a1 = p1.getArea();
                double a2 = p2.getArea();
                if (a1 < a2) {
                    return(1);
                } else if (a1 > a2) {
                    return(-1);
                } else {
                    return(0);
                }
            }
        };
	
	public static final double E = 0.000001;

	/**
	 * Check if the object fits the polygon.
	 * 
	 * @param xMaxLength
	 *            object x max length.
	 * @param yMaxLength
	 *            object y max length.
	 * @param xMinLength
	 *            bject x min length.
	 * @param yMinLength
	 *            bject y min length.
	 * @param poly
	 *            osm polygon.
	 * @return true if the object can be used for this polygon.
	 */
	public static boolean isRectangleBigEnoughForObject(int xMaxLength,
			int yMaxLength, int xMinLength, int yMinLength, LinearRing2D poly) {
		Boolean result = false;
		if (poly.vertices().size() == 5) {
			double segment1 = latLonDistance(poly.vertex(0).y(),
					poly.vertex(0).x(), poly.vertex(1).y(),
					poly.vertex(1).x());
			double segment2 = latLonDistance(poly.vertex(1).y(),
					poly.vertex(1).x(), poly.vertex(2).y(),
					poly.vertex(2).x());
			result = segment1 < xMaxLength && segment1 > xMinLength
					&& segment2 < yMaxLength && segment2 > yMinLength
					|| segment1 < yMaxLength && segment1 > yMinLength
					&& segment2 < xMaxLength && segment2 > xMinLength;

		}
		return result;
	}
	
	public static Geometry polylineToJtsGeom(LinearCurve2D polyline2d) {
		if (polyline2d instanceof LinearRing2D) {
			return linearRing2DToJtsPolygon((LinearRing2D) polyline2d);
		}
		List<Coordinate> coords = new ArrayList<Coordinate>();
		for (Point2D point : polyline2d.vertices()) {
			coords.add(new Coordinate(point.x(), point.y()));
		}
		Coordinate[] points = (Coordinate[]) coords
				.toArray(new Coordinate[coords.size()]);
		GeometryFactory geometryFactory = new GeometryFactory();
		return geometryFactory.createLineString(points);
	}
	
	public static Geometry geom2dToJtsLocal(LinearCurve2D line, Point2D centerpoint) {
		List<Coordinate> coords = new ArrayList<Coordinate>();
		double factor = Math.cos(Math.toRadians(centerpoint.y()));
		for (Point2D point : line.vertices()) {
			double x = (point.x() - centerpoint.x()) * factor;
			double y = point.y() - centerpoint.y();
			coords.add(new Coordinate(x,y));
		}
		if (line.isClosed() && coords.size() > 1 && !coords.get(coords.size() - 1).equals(coords.get(0))) {
			coords.add(coords.get(0));
		}
		Coordinate[] points = (Coordinate[]) coords
				.toArray(new Coordinate[coords.size()]);
		CoordinateSequence coordSeq = CoordinateArraySequenceFactory.instance()
				.create(points);
		GeometryFactory geometryFactory = Osm2XPGeometryFactory.getInstance();
		if (line.isClosed()) {
			LinearRing linearRing = geometryFactory.createLinearRing(coordSeq);
			Polygon jtsPolygon = geometryFactory.createPolygon(linearRing, null);
			return jtsPolygon;
		} else {
			return geometryFactory.createLineString(coordSeq);
		}
	}
	
	public static LinearCurve2D jtsToGeom2dLocal(LineString lineString, Point2D centerpoint) {
		Coordinate[] coordinates = lineString.getCoordinates();
		Point2D[] newPoints = new Point2D[coordinates.length];
		double factor = Math.cos(Math.toRadians(centerpoint.y()));
		for (int i = 0; i < newPoints.length; i++) {
			double x = coordinates[i].x / factor + centerpoint.x();
			double y = coordinates[i].y + centerpoint.y();
			newPoints[i] = new Point2D(x, y);
		}
		if (lineString.isClosed()) {
			return new LinearRing2D(newPoints);
		} else {
			return new Polyline2D(newPoints);
		}
	}

	public static Polygon linearRing2DToJtsPolygon(LinearRing2D ring2d) {
		List<Coordinate> coords = new ArrayList<Coordinate>();
		for (Point2D point : ring2d.vertices()) {
			coords.add(new Coordinate(point.x(), point.y()));
		}
		if (coords.size() > 1 && !coords.get(coords.size() - 1).equals(coords.get(0))) {
			coords.add(coords.get(0));
		}
		Coordinate[] points = (Coordinate[]) coords
				.toArray(new Coordinate[coords.size()]);
		CoordinateSequence coordSeq = CoordinateArraySequenceFactory.instance()
				.create(points);
		GeometryFactory geometryFactory = Osm2XPGeometryFactory.getInstance();
		LinearRing linearRing = geometryFactory.createLinearRing(coordSeq);
		Polygon jtsPolygon = geometryFactory.createPolygon(linearRing, null);
		return jtsPolygon;
	}
	
	public static LineString lineToLineString(Line2D line) {
		List<Coordinate> coords = new ArrayList<Coordinate>();
		for (Point2D point : line.vertices()) {
			coords.add(new Coordinate(point.x(), point.y()));
		}
		GeometryFactory geometryFactory = Osm2XPGeometryFactory.getInstance();
		return geometryFactory.createLineString(coords.toArray(new Coordinate[0]));
	}

	public static Double latLonDistance(double lat1, double lon1, double lat2,
			double lon2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		int meterConversion = 1609;

		return Double.valueOf(dist * meterConversion);
	}

//	private static double deg2rad(double deg) {
//		return (deg * Math.PI / 180.0);
//	}
//
//	private static double rad2deg(double rad) {
//		return (rad * 180.0 / Math.PI);
//	}

	/**
	 * compute min and max vectors for a polygon
	 * 
	 * @param polygon
	 * @return double[]
	 */
	public static Double[] computeExtremeVectors(LinearCurve2D polygon) {

		Double minVector = null;
		Double maxVector = null;

		for (LineSegment2D segment : polygon.edges()) {
			Double distance = latLonDistance(segment.firstPoint().y(),
					segment.firstPoint().x(), segment.lastPoint().y(),
					segment.lastPoint().x());
			if (minVector == null || minVector > distance)
				minVector = Double.valueOf(distance);
			if (maxVector == null || maxVector < distance)
				maxVector = Double.valueOf(distance);
		}
		return new Double[] { minVector, maxVector };
	}

	/**
	 * compute min and max vectors for a polygon
	 * 
	 * @param polygon
	 * @return double[]
	 */
//	public static Double[] computeExtremeVectorsBad(LinearRing2D polygon) {
//
//		Double minVector = null;
//		Double maxVector = null;
//
//		for (LineSegment2D segment : polygon.getEdges()) {
//			LineSegment lineSegment = new LineSegment(
//					segment.getFirstPoint().x, segment.getFirstPoint().y,
//					segment.lastPoint() .x, segment.lastPoint() .y());
//			double distance = lineSegment.getLength() * 100000;
//			if (minVector == null || minVector > distance)
//				minVector = Double.valueOf(distance);
//			if (maxVector == null || maxVector < distance)
//				maxVector = Double.valueOf(distance);
//		}
//		return Double.valueOf[] { minVector, maxVector };
//	}

	/**
	 * Compute the smallest vector of the polygon in meters.
	 * 
	 * @param polygon
	 * @return Double
	 */
	public static Double computeMinVector(LinearRing2D polygon) {
		Double[] vectors = computeExtremeVectors(polygon);
		return vectors[0];
	}
	
	/**
	 * Compute length of line formed by given points array in meters
	 * 
	 * @param line - specified by {@link Point2D} coordinates (lat, lon) array
	 * @return line length in meters
	 */
	public static double computeLengthInMeters(Point2D[] line) {
		double sum = 0;
		for (int i = 1; i < line.length; i++) {
			sum += latLonDistance(line[i-1].y(),
					line[i-1].x(), line[i].y(),line[i].x());
		}
		return sum;
	}
	
	/**
	 * Compute polyline edge length sum or perimeter of the polygon in meters.
	 * 
	 * @param polyline - polyline with edges specified by coordinates (lat, lon) 
	 * @return Double - perimeter value, meters
	 */
	public static double computeEdgesLength(LinearCurve2D polyline) {
		double sum = 0;
		for (LineSegment2D segment : polyline.edges()) {
			Double distance = latLonDistance(segment.firstPoint().y(),
					segment.firstPoint().x(), segment.lastPoint().y(),
					segment.lastPoint().x());
			sum += distance;
		}
		return sum;
	}

	/**
	 * Compute the laregest vector of the polygon in meters.
	 * 
	 * @param polygon
	 * @return Double
	 */
//	public static Double computeMaxVector(LinearRing2D polygon) {
//		Double[] vectors = computeExtremeVectors(polygon);
//		return vectors[1];
//	}

	/**
	 * @param pointA
	 * @param pointB
	 * @return
	 */
	public static boolean compareCoordinates(Point2D pointA, Point2D pointB) {
		return ((int) Math.floor(pointA.x()) == (int) Math.floor(pointB.x()) && (int) Math
				.floor(pointA.y()) == (int) Math.floor(pointB.y()));
	}

	/**
	 * @param tile
	 * @param nodes
	 * @return
	 */
//	public static boolean isListOfNodesOnTile(Point2D tile, List<Node> nodes) {
//		for (Node node : nodes) {
//			if (!compareCoordinates(tile, node)) {
//				return false;
//			}
//		}
//		return true;
//	}

	/**
	 * @param linearRing2D
	 * @return
	 */
//	public static LineSegment2D getLargestVector(LinearRing2D linearRing2D) {
//		LineSegment2D result = null;
//		for (LineSegment2D loc : linearRing2D.getEdges()) {
//			if (result == null || loc.getLength() > result.getLength()) {
//				result = loc;
//			}
//		}
//		return result;
//	}
	
	public static Point2D cleanCoordinatePoint(double latitude,
			double longitude) {
		int longi = (int) Math.floor(longitude);
		int lati = (int) Math.floor(latitude);
		Point2D cleanedLoc = new Point2D(longi, lati);
		return cleanedLoc;
	}

	public static Point2D cleanCoordinatePoint(Point2D basePoint) {
		int longi = (int) Math.floor(basePoint.x());
		int lati = (int) Math.floor(basePoint.y());
		Point2D cleanedLoc = new Point2D(longi, lati);
		return cleanedLoc;
	}

	
	/**
	 * @param linearRing2D
	 * @return
	 */
	private static Polygon linearRing2DToPolygon(LinearRing2D linearRing2D) {
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		for (Point2D pt : linearRing2D.vertices()) {
			Coordinate coordinate = new LatLonCoordinate(pt.x(), pt.y(), 1);
			coordinates.add(coordinate);
		}

		Coordinate[] coordinatesTab = (Coordinate[]) coordinates
				.toArray(new Coordinate[coordinates.size()]);

		GeometryFactory factory = Osm2XPGeometryFactory.getInstance();
		return factory.createPolygon(coordinatesTab);
	}

	/**
	 * @param polygon
	 * @return
	 */
	private static LinearRing2D polygonToLinearRing2D(Geometry polygon) {

		List<Point2D> points = new ArrayList<Point2D>();
		Coordinate[] coordinates = polygon instanceof Polygon ? ((Polygon) polygon).getExteriorRing().getCoordinates() : polygon.getCoordinates();
		for (Coordinate coordinate : coordinates) {
			Point2D point2d = new Point2D(coordinate.x, coordinate.y);
			points.add(point2d);
		}

		return new LinearRing2D(points);
	}

	/**
	 * @param linearRing2D
	 * @return
	 */
	private static Boolean isLinearRingOnASingleTile(LinearRing2D linearRing2D) {
		for (Point2D point : linearRing2D.vertices()) {
			if (!compareCoordinates(linearRing2D.firstPoint(), point)) {
				return false;
			}
		}
		return true;
	}

	public static boolean areParallelsSegmentsIdentics(LinearRing2D linearRing2D) {
		if (linearRing2D.vertexNumber() == 5) {

			LineSegment2D line1 = new LineSegment2D(linearRing2D.vertex(0),
					linearRing2D.vertex(1));
			LineSegment2D line2 = new LineSegment2D(linearRing2D.vertex(1),
					linearRing2D.vertex(2));
			LineSegment2D line3 = new LineSegment2D(linearRing2D.vertex(2),
					linearRing2D.vertex(3));
			LineSegment2D line4 = new LineSegment2D(linearRing2D.vertex(3),
					linearRing2D.vertex(4));

			double length1 = line1.length();
			double length2 = line2.length();
			double length3 = line3.length();
			double length4 = line4.length();

			double diff1 = Math.abs(length1 - length3);
			double diff2 = Math.abs(length2 - length4);

			boolean sameLength1 = diff1 < (line1.length() / 10);
			boolean sameLength2 = diff2 < (line2.length() / 10);
			if (sameLength1 && sameLength2) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @param sourceFootprint
	 * @return
	 */
	public static LinearRing2D simplifyPolygon(LinearRing2D sourceFootprint) {
		LinearRing2D result = sourceFootprint;
		if (sourceFootprint.vertexNumber() > 5) {
			result = ShortEdgesDeletion(sourceFootprint);
			if (result == null) {
				result = sourceFootprint;
			}
		}
		return result;

	}
	
	private static Point2D[] getMedianPoints(Point2D[] points, int pointCount) {
		Point2D[] result = new Point2D[pointCount];
		int next = 0;
		for (int i = 0; i < result.length && next < points.length; i++) {
			Point2D startPt = points[next];
			Point2D closestPoint = null;
			double closestDist = Double.MAX_VALUE;
			for (int j = i+1; j < points.length - 1; j++) { //ignore last point, since it equals first one
				double dist = startPt.distance(points[j]);
				if (dist < closestDist) {
					closestDist = dist;
					closestPoint = points[j];
				}
			}
			next++;
			if (closestPoint == points[i+1]) {
				next++;
			}
			result[i] = new Point2D((startPt.x() + closestPoint.x()) / 2, (startPt.y() + closestPoint.y()) / 2);
		}
		return result;
	}
	
	public static Line2D getCenterline(LinearCurve2D linearCurve2D) {
		int vertexNumber = linearCurve2D.vertexNumber();
		if (vertexNumber < 2 && linearCurve2D.isClosed() && vertexNumber < 3) {
			return null;
		}
		if (!linearCurve2D.isClosed()) {
			return new Line2D(linearCurve2D.firstPoint(), linearCurve2D.lastPoint()); 
		}
		if (vertexNumber > 5) { 
			Point2D[] points = linearCurve2D.vertexArray();
			return PolygonUtil.getMedianLine(points);
		}
		Point2D[] points = linearCurve2D.vertexArray();
		Point2D[] endPoints = getMedianPoints(points, 2);
		return new Line2D(endPoints[0], endPoints[1]);
	}

	/**
	 * remove some vertex to simplify polygon
	 * 
	 * @param polygon
	 * @return
	 */
	public static LinearRing2D ShortEdgesDeletion(LinearRing2D sourceFootprint) {
		try {
			//test on way 51812313
			// we create a jts polygon from the linear ring
			Polygon sourcePoly = linearRing2DToPolygon(sourceFootprint);

			// we create a simplified polygon
			Geometry cleanPoly = LatLonShortEdgesDeletion.get(sourcePoly, 5.0); //Min side 5 meters

			// we create a linearRing2D from the modified polygon
			LinearRing2D result = polygonToLinearRing2D(cleanPoly);

			// we check if the simplification hasn't moved one point to another
			// tile
			boolean isOnSingleTile = isLinearRingOnASingleTile(result);
			// we check if the result is a simple footprint
			boolean isASimpleFootprint = result.vertexNumber() == 5;
			// we check if the result hasn't made too much simplification
			boolean isAreaChangeMinimal = linearRing2DToPolygon(result)
					.getArea() > (sourcePoly.getArea() / 1.5);

			if (isOnSingleTile && isASimpleFootprint && isAreaChangeMinimal) {
				return result;
			} else {
				return null;
			}

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param polygon
	 * @return
	 */
	public static Double[] getExtremesAngle(LinearRing2D polygon) {
		Double minAngle = null;
		Double maxAngle = null;
		for (int i = 0; i < polygon.vertexNumber() - 2; i++) {
			Double angle = Math.toDegrees(Angle2D.absoluteAngle(
					polygon.vertex(i), polygon.vertex(i + 1),
					polygon.vertex(i + 2)));
			if (minAngle == null || angle < minAngle) {
				minAngle = angle;
			}

			if (maxAngle == null || angle > maxAngle) {
				maxAngle = angle;
			}
		}
		Double lastAngle = Math.toDegrees(Angle2D.absoluteAngle(
				polygon.vertex(polygon.vertexNumber() - 2),
				polygon.vertex(0), polygon.vertex(1)));
		if (minAngle == null || lastAngle < minAngle) {
			minAngle = lastAngle;
		}
		if (maxAngle == null || lastAngle > maxAngle) {
			maxAngle = lastAngle;
		}

		return new Double[] { minAngle, maxAngle };
	}

	/**
	 * @param nodes OSM node list
	 * @return Polygon consisting of points specified by given nodes
	 */
	public static LinearRing2D getPolygonFromOsmNodes(List<Node> nodes) {
		LinearRing2D result = new LinearRing2D();
		for (Node node : nodes) {
			result.addVertex(new Point2D(node.getLon(), node.getLat()));
		}
		return result;
	}
	
	/**
	 * @param nodes OSM node list
	 * @return Polyline consisting of points specified by given nodes
	 */
	public static LinearCurve2D getPolylineFromOsmNodes(List<Node> nodes) {
		if (nodes.size() > 2 && nodes.get(0).getId() == nodes.get(nodes.size() - 1).getId()) {
			return getPolygonFromOsmNodes(nodes);
		}
		Polyline2D result = new Polyline2D();
		for (Node node : nodes) {
			result.addVertex(new Point2D(node.getLon(), node.getLat()));
		}
		return result;
	}
	
	public static Point2D[] getPointsFromOsmNodes(List<Node> nodes) {
		Point2D[] result = new Point2D[nodes.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = new Point2D(nodes.get(i).getLon(), nodes.get(i).getLat());
		}
		return result;
	}

	/**
	 * @param nodes
	 * @return
	 */
	public static Point2D getNodesCenter(List<Node> nodes) {
		Point2D center = null;
		if (nodes.size() > 3) {
			LinearRing2D polygon = new LinearRing2D();

			for (Node node : nodes) {
				polygon.addVertex(new Point2D(node.getLon(), node.getLat()));
			}
			center = getPolylineCenter(polygon);
		} else {
			center = new Point2D(nodes.get(0).getLon(), nodes.get(0).getLat());
		}
		return center;

	}

	/**
	 * @param tile
	 * @param node
	 * @return
	 */
	public static boolean compareCoordinates(Point2D tile, Node node) {

		return ((int) Math.floor(tile.x()) == (int) Math.floor(node.getLon()) && (int) Math
				.floor(tile.y()) == (int) Math.floor(node.getLat()));

	}

	/**
	 * Set clockwise point order for given {@link LinearRing2D} if necessary
	 * @param linearRing2D
	 * @return clockwise {@link LinearRing2D}
	 */
	public static LinearRing2D setClockwise(LinearRing2D linearRing2D) {
		return setDirection(linearRing2D, true);
	}
	
	/**
	 * Set counter-clockwise point order for given {@link LinearRing2D} if necessary
	 * X-Plane needs facades to be defined in counter-clockwise order, see <a href="https://developer.x-plane.com/2010/07/facade-tuning-and-tips/">this article</a> 
	 * @param linearRing2D
	 * @return counter-clockwise {@link LinearRing2D}
	 */
	public static LinearRing2D setCCW(LinearRing2D linearRing2D) {
		return setDirection(linearRing2D, false);
	}
	
	/**
	 * Set counter-clockwise point order for given {@link LinearRing} if necessary
	 * X-Plane needs facades to be defined in counter-clockwise order, see <a href="https://developer.x-plane.com/2010/07/facade-tuning-and-tips/">this article</a> 
	 * @param linearRing
	 * @return counter-clockwise {@link LinearRing}
	 */
	public static LineString setCCW(LineString ring) {
		if (!ring.isClosed()) {
			return ring;
		}
		if (!Orientation.isCCW(ring.getCoordinates())) {
			return (LinearRing) ring.reverse();
		}
		return ring;
	}
	
	/**
	 * Set clockwise point order for given {@link LinearRing} if necessary
	 * X-Plane needs facades to be defined in counter-clockwise order, see <a href="https://developer.x-plane.com/2010/07/facade-tuning-and-tips/">this article</a> 
	 * @param linearRing
	 * @return clockwise {@link LinearRing}
	 */
	public static LineString setCW(LineString ring) {
		if (!ring.isClosed()) {
			return ring;
		}
		if (Orientation.isCCW(ring.getCoordinates())) {
			return (LinearRing) ring.reverse();
		}
		return ring;
	}
	
	/**
	 * Set specified point order for given {@link LinearRing2D} if necessary
	 * @param linearRing2D
	 * @param clockwise <code>true</code> to set clockwise point order, <code>false</code> - for counter-clockwise one
	 * @return
	 */
	public static LinearRing2D setDirection(LinearRing2D linearRing2D, boolean clockwise) {
//		double edgeSum = 0;
//		for (int i = 0; i < linearRing2D.vertices().size() - 1; i++) {
//			double a = (linearRing2D.vertex(i + 1).x() - linearRing2D
//					.vertex(i).x());
//			double b = (linearRing2D.vertex(i + 1).y() + linearRing2D
//					.vertex(i).y());
//			edgeSum = edgeSum + (a * b);
//		}
//		if ((edgeSum < 0 && clockwise) || (edgeSum > 0 && !clockwise)) {
//			return linearRing2D.reverse();
//		}
//		return linearRing2D;
		double area = linearRing2D.area();
		if ((area > 0 && clockwise) || (area < 0 && !clockwise)) {
			List<Point2D> list = new ArrayList<Point2D>(linearRing2D.vertices()); 
			Collections.reverse(list);
			return new LinearRing2D(list);
		}
		return linearRing2D;
	}

	/**
	 * Check if a linear ring is counter-clockwise and reverse direction in case it's not CCW
	 * X-Plane facades outer ring needs to be <b>CCW</b> (see https://developer.x-plane.com/2010/07/facade-tuning-and-tips/)
	 * @param ring2d {@link LinearRing2D} to check
	 * @return ring2d itself if it's CCW, reversed direction ring otherwise
	 */
	public static LinearRing2D forceCCW(LinearRing2D ring2d) {
		LinearRing2D result = null;
		if (ring2d.vertices().size() > 4) { //4 because it makes sense only in case we have 3 points. 4th one is equal to 1st one 

			Coordinate[] coords = new Coordinate[ring2d.vertices().size()];
			for (int i = 0; i < ring2d.vertices().size(); i++) {
				coords[i] = new Coordinate(ring2d.vertex(i).x(),
						ring2d.vertex(i).y());
			}

			if (Orientation.isCCW(coords)) {
				result = ring2d;
			} else {
				Collection<Point2D> clockwiseVectors = new ArrayList<Point2D>();
				for (int i = ring2d.vertices().size() - 1; i > -1; i--) {
					clockwiseVectors.add(ring2d.vertex(i));
				}

				result = new LinearRing2D(clockwiseVectors);
			}
		} else {
			result = ring2d;
		}

		return result;
	}
	
	/**
	 * Check if a linear ring is clockwise and reverse direction in case it's not CW
	 * X-Plane multipoly inner ring needs to be clockwise
	 * @param ring2d {@link LinearRing2D} to check
	 * @return ring2d itself if it's CW, reversed direction ring otherwise
	 */
	public static LinearRing2D forceCW(LinearRing2D ring2d) {
		if (ring2d.vertices().size() > 4) { //4 because it makes sense only in case we have 3 points. 4th one is equal to 1st one 
			List<Coordinate> coords = ring2d.vertices().stream().map(vertex -> new Coordinate(vertex.x(), vertex.y())).collect(Collectors.toList());
//			Coordinate[] coords = new Coordinate[ring2d.vertices().size()];
//			for (int i = 0; i < ring2d.vertices().size(); i++) {
//				coords[i] = new Coordinate(ring2d.vertex(i).x,
//						ring2d.vertex(i).y());
//			}
			if (Orientation.isCCW(coords.toArray(new Coordinate[0]))) {
				Collection<Point2D> clockwiseVectors = new ArrayList<Point2D>();
				for (int i = ring2d.vertices().size() - 1; i > -1; i--) {
					clockwiseVectors.add(ring2d.vertex(i));
				}
				return new LinearRing2D(clockwiseVectors);
			} 
		} 
		
		return ring2d;
	}
	
	/**
	 * Check if a {@link LineString} is counter-clockwise and reverse direction in case it's not CCW
	 * X-Plane facades outer ring needs to be <b>CCW</b> (see https://developer.x-plane.com/2010/07/facade-tuning-and-tips/)
	 * @param lineString {@link LineString} to check
	 * @return lineString itself if it's CCW, reversed direction ring otherwise
	 */
	public static LineString forceCCW(LineString lineString) {
		return forceDirection(lineString, true);
	}
	
	/**
	 * Check if a {@link LineString} is clockwise and reverse direction in case it's not CW
	 * X-Plane multipoly inner ring needs to be clockwise
	 * @param lineString {@link LineString} to check
	 * @return lineString itself if it's CCW, reversed direction ring otherwise
	 */
	public static LineString forceCW(LineString lineString) {
		return forceDirection(lineString, false);
	}
	
	private static LineString forceDirection(LineString lineString, boolean ccw) {
		if (!lineString.isClosed()) {
			return lineString;
		}
		if (Orientation.isCCW(lineString.getCoordinates()) != ccw) {
			return (LineString) lineString.reverse();
		}
		return lineString;
	}

	/**
	 * @param polygon
	 * @return
	 */
	public static Point2D getPolylineCenter(LinearCurve2D polygon) {
		Point2D center = null;
		if (polygon.vertices().size() > 3) {
			Centroid centroidArea = new Centroid(polylineToJtsGeom(polygon));
			center = new Point2D(centroidArea.getCentroid().x,
					centroidArea.getCentroid().y);
		} else {
			center = polygon.firstPoint();
		}
		return center;
	}

	public static Point2D getRotationPoint(Point2D origin, Point2D ptX,
			Point2D ptY, Point2D lastPoint, int xCoord, int yCoord) {

		Point2D result = null;

		LineSegment segmentX = new LineSegment(new Coordinate(origin.x(),
				origin.y()), new Coordinate(ptX.x(), ptX.y()));
		LineSegment segmentY = new LineSegment(new Coordinate(origin.x(),
				origin.y()), new Coordinate(ptY.x(), ptY.y()));
		LineSegment segment2X = new LineSegment(new Coordinate(ptY.x(), ptY.y()),
				new Coordinate(lastPoint.x(), lastPoint.y()));
		LineSegment segment2Y = new LineSegment(new Coordinate(ptX.x(), ptX.y()),
				new Coordinate(lastPoint.x(), lastPoint.y()));

		// compute the X point wanted by the user
		float xFragment = (float) xCoord / 100;
		Coordinate xUserPoint = segmentX.pointAlong(xFragment);
		Coordinate x2UserPoint = segment2X.pointAlong(xFragment);
		// compute the Y point wanted by the user
		float yFragment = (float) yCoord / 100;
		Coordinate yUserPoint = segmentY.pointAlong(yFragment);
		Coordinate y2UserPoint = segment2Y.pointAlong(yFragment);

		Line2D xSeg = new Line2D(xUserPoint.x, xUserPoint.y, x2UserPoint.x,
				x2UserPoint.y);
		Line2D ySeg = new Line2D(yUserPoint.x, yUserPoint.y, y2UserPoint.x,
				y2UserPoint.y);
		result = xSeg.intersection(ySeg);

		return result;
	}

	/**
	 * lod13 algo by Jeema
	 * 
	 * @param latitude
	 * @param longitude
	 * @return Lod13Location
	 */
	public static Lod13Location getLod13Location(double latitude,
			double longitude) {
		StringBuilder lod13String = new StringBuilder();

		double leftLong, rightLong, topLat, bottomLat, centerLat, centerLong;
		double newLeftLong, newRightLong, newTopLat, newBottomLat;
		double xOffset, yOffset;
		int i, quadNumber;

		leftLong = -180.0;
		rightLong = 300.0;
		topLat = 90.0;
		bottomLat = -270.0;

		// ----- Iterate through the 15 level of detail levels to compute the
		// LOD13 location value
		for (i = 0; i < 15; i++) {
			// ----- Get the lat/long values for the center of the current
			// lat/long bounds
			centerLong = (leftLong + rightLong) / 2;
			centerLat = (topLat + bottomLat) / 2;

			// ----- Is the target longitude to the left of the current center
			// longitude?
			if (longitude < centerLong) {
				// ----- Then the new quad is on the left
				quadNumber = 0;
				newLeftLong = leftLong;
				newRightLong = centerLong;
			} else {
				// ----- Otherwise the new quad is on the right
				quadNumber = 1;
				newLeftLong = centerLong;
				newRightLong = rightLong;
			}

			// ----- Is the target latitude above the current center latitude?
			if (latitude > centerLat) {
				// ----- Then the new quad is on the top
				newTopLat = topLat;
				newBottomLat = centerLat;
			} else {
				// ----- Otherwise the new quad is on the bottom
				quadNumber += 2;
				newTopLat = centerLat;
				newBottomLat = bottomLat;
			}

			// ----- Concactenate the quad number onto the LOD13 string
			lod13String.append(quadNumber);

			// ----- Update the left/right/top/bottom bounds for the next
			// iteration
			leftLong = newLeftLong;
			rightLong = newRightLong;
			topLat = newTopLat;
			bottomLat = newBottomLat;
		}

		// ----- Now calculate the X and Y offsets within the LOD13 square
		xOffset = (longitude - leftLong) / (rightLong - leftLong);
		yOffset = (topLat - latitude) / (topLat - bottomLat);

		// ----- Build the Lod13Location object and return it to the caller
		Lod13Location location = new Lod13Location();
		location.setLod13String(lod13String.toString());

		location.setxOffset(xOffset);
		location.setyOffset(yOffset);

		return location;
	}

	public static boolean boxContainsAnotherBox(Box2D box1, Box2D box2) {
		// poly inside another?
		Rectangle2D rect1 = new Rectangle2D(box1.asAwtRectangle2D());
		Rectangle2D rect2 = new Rectangle2D(box2.asAwtRectangle2D());
		if (box1.containsBounds(rect2)) {
			return true;
		}
		if (box2.containsBounds(rect1)) {
			return true;
		}

		// check intersections
		// for (LinearShape2D line : box1.getEdges()) {
		// for (LinearShape2D line2 : box2.getEdges()) {
		// if (line.getIntersection(line2) != null) {
		// return true;
		// }
		// }
		// }

		return false;
	}

	public static List<Node> removeExtraEnd(List<Node> nodes) {
		if (nodes.size() > 3 && nodes.get(0).getId() == nodes.get(nodes.size() - 1).getId()) {
			nodes.remove(nodes.size() - 1);
		}
		return nodes;
	}
	
	public static boolean isValid(LinearCurve2D polyline) {
		return polylineToJtsGeom(polyline).isValid();
	}
	
	/**
	 * Get / create a valid version of the geometry given. If the geometry is a polygon or multi polygon, self intersections /
	 * inconsistencies are fixed. Otherwise the geometry is returned.
	 * 
	 * @param geom
	 * @return a geometry 
	 */
	@SuppressWarnings("unchecked")
	public static Geometry fix(Geometry geom){
		try {
		    if(geom instanceof Polygon){
		        if(geom.isValid()){
	//	            geom.normalize(); // validate does not pick up rings in the wrong order - this will fix that //TODO not sure it's needed for our task
		            return geom; // If the polygon is valid just return it
		        }
		        Polygonizer polygonizer = new Polygonizer();
		        addPolygon((Polygon)geom, polygonizer);
		        return toPolygonGeometry(polygonizer.getPolygons(), geom.getFactory());
		    }else if(geom instanceof MultiPolygon){
		        if(geom.isValid()){
		            geom.normalize(); // validate does not pick up rings in the wrong order - this will fix that
		            return geom; // If the multipolygon is valid just return it
		        }
		        Polygonizer polygonizer = new Polygonizer();
		        for(int n = geom.getNumGeometries(); n-- > 0;){
		            addPolygon((Polygon)geom.getGeometryN(n), polygonizer);
		        }
		        return toPolygonGeometry(polygonizer.getPolygons(), geom.getFactory());
		    }else{
		        return geom; // In my case, I only care about polygon / multipolygon geometries
		    }
		} catch (TopologyException e) {
			try {
				Geometry newGeom = BufferOp.bufferOp(geom,0);
				if (newGeom != geom && (geom instanceof Polygon || geom instanceof MultiPolygon)) {
					return fix(newGeom);
				}
				newGeom = repair(newGeom);
				if (newGeom != geom && (geom instanceof Polygon || geom instanceof MultiPolygon) && !geom.isEmpty()) {
					return newGeom;
				}
			} catch (TopologyException  e1) {
				return null;
			}
		}
		return null;
	}

	/**
	 * Add all line strings from the polygon given to the polygonizer given
	 * 
	 * @param polygon polygon from which to extract line strings
	 * @param polygonizer polygonizer
	 */
	static void addPolygon(Polygon polygon, Polygonizer polygonizer){
	    addLineString(polygon.getExteriorRing(), polygonizer);
	    for(int n = polygon.getNumInteriorRing(); n-- > 0;){
	        addLineString(polygon.getInteriorRingN(n), polygonizer);
	    }
	}

	/**
	 * Add the linestring given to the polygonizer
	 * 
	 * @param linestring line string
	 * @param polygonizer polygonizer
	 */
	static void addLineString(LineString lineString, Polygonizer polygonizer){

	    if(lineString instanceof LinearRing){ // LinearRings are treated differently to line strings : we need a LineString NOT a LinearRing
	        lineString = lineString.getFactory().createLineString(lineString.getCoordinateSequence());
	    }

	    // unioning the linestring with the point makes any self intersections explicit.
	    Point point = lineString.getFactory().createPoint(lineString.getCoordinateN(0));
	    Geometry toAdd = lineString.union(point); 

	    //Add result to polygonizer
	    polygonizer.add(toAdd);
	}

	/**
	 * Get a geometry from a collection of polygons.
	 * 
	 * @param polygons collection
	 * @param factory factory to generate MultiPolygon if required
	 * @return null if there were no polygons, the polygon if there was only one, or a MultiPolygon containing all polygons otherwise
	 */
	static Geometry toPolygonGeometry(Collection<Polygon> polygons, GeometryFactory factory){
	    switch(polygons.size()){
	        case 0:
	            return null; // No valid polygons!
	        case 1:
	            return polygons.iterator().next(); // single polygon - no need to wrap
	        default:
	            //polygons may still overlap! Need to sym difference them
	            Iterator<Polygon> iter = polygons.iterator();
	            Geometry ret = iter.next();
	            while(iter.hasNext()){
            		ret = getFirst(ret);
            		if (ret == null) {
						return null;
					}
	            	Polygon next = iter.next();
	            	try {
						ret = ret.symDifference(next);
	            	} catch (TopologyException e) {
	            		ret = BufferOp.bufferOp(ret, 0);
	            		ret = getFirst(ret);
	            		if (ret == null) {
							return null;
						}
	            		try {
	            			ret = ret.symDifference(next);
	            		} catch (TopologyException e1) {
	            			return null;
						}
	            	}
	            }
	            return ret;
	    }
	}

	protected static Geometry getFirst(Geometry ret) {
		if (ret instanceof Polygon) {
			return ret;
		}
		List<Polygon> polys = flatMapToPoly(ret);
		if (!polys.isEmpty()) {
			return polys.get(0);
		} else {
			return null;
		}
	}
	
	public static List<Geometry> flatMap(Geometry  geometry) {
		List<Geometry> resList = new ArrayList<Geometry>();
		if (geometry instanceof GeometryCollection) {
			for (int i = 0; i < geometry.getNumGeometries(); i++) {
				Geometry curGeom = geometry.getGeometryN(i);
				if (curGeom != null) {
					resList.addAll(flatMap(curGeom));
				}
			}
		} else if (geometry != null){
			resList.add(geometry);
		}
		return resList;		
	}
	
	public static List<Polygon> flatMapToPoly(Geometry geometry) {
		List<Polygon> resList = new ArrayList<Polygon>();
		if (geometry instanceof GeometryCollection) {
			for (int i = 0; i < geometry.getNumGeometries(); i++) {
				Geometry curGeom = geometry.getGeometryN(i);
				if (curGeom != null) {
					resList.addAll(flatMapToPoly(curGeom));
				}
			}
		} else if (geometry instanceof Polygon){
			resList.add((Polygon) geometry);
		}
		return resList;		
	}

	/**
	 * Get / create a valid version of given {@link LinearRing2D}. If the geometry is a polygon or multi polygon, self intersections /
	 * inconsistencies are fixed. Otherwise polygon itself is returned.
	 * 
	 * @param polygon
	 * @return
	 */
	public static List<LinearRing2D> fix(LinearRing2D polygon) {
//		Geometry fixed = fix(linearRing2DToJtsPolygon(polygon)); TODO check whether repair() works better
		Geometry fixed = repair(linearRing2DToJtsPolygon(polygon));
		if (fixed instanceof Polygon) {
			return Collections.singletonList(polygonToLinearRing2D(fixed));
		} else if (fixed instanceof MultiPolygon) {
			List<LinearRing2D> resList = new ArrayList<LinearRing2D>();
			for (int i = 0; i < fixed.getNumGeometries(); i++) {
				Geometry geom = fixed.getGeometryN(i);
				if (geom instanceof Polygon) {
					resList.add(polygonToLinearRing2D(geom));
				}
			}
			return resList;
		}
		if (fixed == null) {
			return Collections.emptyList();
		}
		return Collections.singletonList(polygon);
	}
	

	@SuppressWarnings("unchecked")
	public static Geometry polygonize(Geometry geometry) {
		List<?> lines = LineStringExtracter.getLines(geometry);
		Polygonizer polygonizer = new Polygonizer();
		polygonizer.add(lines);
		Collection<Polygon> polys = polygonizer.getPolygons();
		Polygon[] polyArray = GeometryFactory.toPolygonArray(polys);
		return geometry.getFactory().createGeometryCollection(polyArray);
	}

	public static Geometry splitPolygon(Geometry poly, Geometry line) {
		Geometry nodedLinework = poly.getBoundary().union(line);
		Geometry polys = polygonize(nodedLinework);

		// Only keep polygons which are inside the input
		List<Polygon> output = new ArrayList<Polygon>();
		for (int i = 0; i < polys.getNumGeometries(); i++) {
			Polygon candpoly = (Polygon) polys.getGeometryN(i);
			if (poly.contains(candpoly.getInteriorPoint())) {
				output.add(candpoly);
			}
		}
		return poly.getFactory().createGeometryCollection(GeometryFactory.toGeometryArray(output));
	}
	
	public static Collection<? extends Geometry> cutHoles(Geometry geometry, int maxHoleCount) {
		if (!(geometry instanceof Polygon)) {
			return Collections.singletonList(geometry);
		}
		int numHoles = ((Polygon) geometry).getNumInteriorRing();
		if (numHoles <= maxHoleCount) {
			return Collections.singletonList(geometry);
		}
		GeometryFactory geometryFactory = Osm2XPGeometryFactory.getInstance();
		Polygon poly = (Polygon) geometry;
		Envelope envelope = poly.getExteriorRing().getCoordinateSequence().expandEnvelope(new Envelope());
		Coordinate p1 = poly.getInteriorRingN(0).getCentroid().getCoordinate();
		//If we have only one hole, cutting line is horizontal line containing it's center
		//If we have more - cutting line is a line going through centers of two first holes - this would allow to git rid of two holes at least per each cut
		LineString cuttingLine;								
		if (numHoles > 1) {
			Coordinate p2 =  poly.getInteriorRingN(1).getCentroid().getCoordinate();
			double dx = p2.x-p1.x;
			if (Math.abs(dx) < GeomUtils.E) { //Use vertical cutting line
				cuttingLine = geometryFactory.createLineString(new Coordinate[] {
						new Coordinate(p1.x, envelope.getMinY()), 
						new Coordinate(p1.x, envelope.getMaxY()),
						});
			} else {
				double k = (p2.y-p1.y)/dx;
				double b = p1.y - k* p1.x;
				cuttingLine = geometryFactory.createLineString(new Coordinate[] {
						new Coordinate(envelope.getMinX(), k * envelope.getMinX() + b), 
						new Coordinate(envelope.getMaxX(), k * envelope.getMaxX() + b)
						});
			}
		} else {
			cuttingLine = geometryFactory.createLineString(new Coordinate[] {
					new Coordinate(envelope.getMinX(), p1.y), 
					new Coordinate(envelope.getMaxX(), p1.y)
					});
		}
		List<Geometry> cutResult = GeomUtils.flatMap(GeomUtils.splitPolygon(geometry, cuttingLine));
		if (cutResult.size() > 0 && cutResult.get(0).equals(geometry)) { //If we got stucked and can get infinite recursion here - possibly inner ring is too small and too close to border. Just ignore inner ring.
			//TODO if we have more than one inner ring - try to cut on bigger one and maybe just ignore too small ones
			return Collections.singletonList(geometryFactory.createPolygon((CoordinateSequence) poly.getExteriorRing()));
		}
		List<Geometry> resultList = new ArrayList<Geometry>();
		for (Geometry curGeom : cutResult) {
			resultList.addAll(cutHoles(curGeom, maxHoleCount));
		}
		return resultList;
	}
	
	public static double getMagneticBearing(Point2D p1, Point2D p2){
		return getTrueBearing(p1,p2) - new Geomagnetism(p1.x(),p1.y()).getDeclination() ;
	}
	
	public static double getTrueBearing(Point2D p1, Point2D p2){
		return getTrueBearing(p1.y(),p1.x(),p2.y(),p2.x());
	}
	
	protected static double getTrueBearing(double lat1, double lon1, double lat2, double lon2) {
		double longitude1 = lon1;
		double longitude2 = lon2;
		double latitude1 = Math.toRadians(lat1);
		double latitude2 = Math.toRadians(lat2);
		double longDiff = Math.toRadians(longitude2 - longitude1);
		double y = Math.sin(longDiff) * Math.cos(latitude2);
		double x = Math.cos(latitude1) * Math.sin(latitude2)
				- Math.sin(latitude1) * Math.cos(latitude2) * Math.cos(longDiff);

		return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
	}

	/**
	 *
	 * @param geom
	 * @return
	 */
	public static Geometry repair(Geometry geom) {
		GeometryFactory factory = geom.getFactory();
		if (geom instanceof MultiPolygon) {
			MultiPolygon mp = (MultiPolygon) geom;
			Polygon[] polys = new Polygon[mp.getNumGeometries()];
			for (int i = 0; i < mp.getNumGeometries(); i += 1) {
				polys[i] = repair((Polygon) mp.getGeometryN(i));
			}
			return factory.createMultiPolygon(polys);
		} else if (geom instanceof Polygon) {
			return repair((Polygon) geom);
		} else if (geom.getGeometryType().equals("GeometryCollection")) {
			GeometryCollection gc = (GeometryCollection) geom;
			Geometry[] geoms = new Geometry[gc.getNumGeometries()];
			for (int i = 0; i < gc.getNumGeometries(); i += 1) {
				geoms[i] = repair(gc.getGeometryN(i));
			}
			Thread.dumpStack();
			return factory.createGeometryCollection(geoms);
		} else {
			return (geom);
		}
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Polygon repair(Polygon p) {
		GeometryFactory factory = p.getFactory();
		IsValidOp isValidOp = new IsValidOp(p);
		TopologyValidationError err = isValidOp.getValidationError();
		while (err != null) {
			if ((err.getErrorType() == TopologyValidationError.SELF_INTERSECTION)
					|| (err.getErrorType() == TopologyValidationError.RING_SELF_INTERSECTION)
					|| (err.getErrorType() == TopologyValidationError.DISCONNECTED_INTERIOR)) {
				Geometry boundary = p.getBoundary();
				// calling union will re-node the boundary curve to eliminate self-intersections
				// see
				// http://lists.jump-project.org/pipermail/jts-devel/2006-November/001815.html
				boundary = boundary.union(boundary);
				Polygonizer polygonizer = new Polygonizer();
				polygonizer.add(boundary);
				Collection<Polygon> c = polygonizer.getPolygons();
				if (c.size() > 0) {
					Polygon[] polys = (Polygon[]) c.toArray(new Polygon[c.size()]);
					Arrays.sort(polys, POLYGON_AREA_COMPARATOR);
					p = polys[0];
				} else {
					System.err.println("unable to fix polygon: " + err);
					p = factory.createPolygon(null, null);
				}
			} else if (err.getErrorType() == TopologyValidationError.TOO_FEW_POINTS) {
				LinearRing exterior = (LinearRing) p.getExteriorRing();
				Coordinate[] coords = CoordinateArrays.removeRepeatedPoints(exterior.getCoordinates());
				if (coords.length < 4) {
					p = factory.createPolygon(null, null);
				} else {
					exterior = factory.createLinearRing(coords);
					List<LinearRing> validInteriorRings = new ArrayList<LinearRing>(p.getNumInteriorRing());
					for (int i = 0; i < p.getNumInteriorRing(); i += 1) {
						LinearRing s = (LinearRing) p.getInteriorRingN(i);
						coords = CoordinateArrays.removeRepeatedPoints(s.getCoordinates());
						if (coords.length >= 4) {
							validInteriorRings.add(factory.createLinearRing(coords));
						}
					}
					p = factory.createPolygon(exterior, GeometryFactory.toLinearRingArray(validInteriorRings));
				}
			} else {
				System.err.println(err);
				p = factory.createPolygon(null, null);
			}
			isValidOp = new IsValidOp(p);
			err = isValidOp.getValidationError();
		}
		return (p);
	}

	public static double latLonDistance(Point2D p1, Point2D p2) {
		return latLonDistance(p1.y(),p1.x(),p2.y(),p2.x());
	}

}
