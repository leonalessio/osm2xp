package com.osm2xp.utils.geometry;

import java.io.Serializable;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFactory;

/**
 * Creates {@link CoordinateSequence}s represented as an array of {@link Coordinate}s.
 *
 * @version 1.7
 */
public final class NodeCoordinateArraySequenceFactory
    implements CoordinateSequenceFactory, Serializable
{
  private static final long serialVersionUID = -4099577099607551657L;
  private static final NodeCoordinateArraySequenceFactory instanceObject = new NodeCoordinateArraySequenceFactory();

  private NodeCoordinateArraySequenceFactory() {
  }

  private Object readResolve() {
  	// http://www.javaworld.com/javaworld/javatips/jw-javatip122.html
    return NodeCoordinateArraySequenceFactory.instance();
  }

  /**
   * Returns the singleton instance of {@link NodeCoordinateArraySequenceFactory}
   */
  public static NodeCoordinateArraySequenceFactory instance() {
    return instanceObject;
  }

  /**
   * Returns a {@link CoordinateArraySequence} based on the given array (the array is
   * not copied).
   *
   * @param coordinates
   *            the coordinates, which may not be null nor contain null
   *            elements
   */
  public CoordinateSequence create(Coordinate[] coordinates) {
    return new NodeCoordinateArraySequence(coordinates);
  }

  /**
   * @see org.locationtech.jts.geom.CoordinateSequenceFactory#create(org.locationtech.jts.geom.CoordinateSequence)
   */
  public CoordinateSequence create(CoordinateSequence coordSeq) {
    return new NodeCoordinateArraySequence(coordSeq);
  }

  /**
   * The created sequence dimension is clamped to be &lt;= 3.
   * 
   * @see org.locationtech.jts.geom.CoordinateSequenceFactory#create(int, int)
   *
   */
  public CoordinateSequence create(int size, int dimension) {
    if (dimension > 3)
      dimension = 3;
      //throw new IllegalArgumentException("dimension must be <= 3");
    
    // handle bogus dimension
    if (dimension < 2)
      dimension = 2;      
    
    return new NodeCoordinateArraySequence(size, dimension);
  }
  
  public CoordinateSequence create(int size, int dimension, int measures) {
    int spatial = dimension - measures;
    
    if (measures > 1) {
      measures = 1; // clip measures
      //throw new IllegalArgumentException("measures must be <= 1");
    }
    if ((spatial) > 3) {
      spatial = 3; // clip spatial dimension
      //throw new IllegalArgumentException("spatial dimension must be <= 3");
    }
    
    if (spatial < 2)
      spatial = 2; // handle bogus spatial dimension
    
    return new NodeCoordinateArraySequence(size, spatial+measures, measures);
  }
}