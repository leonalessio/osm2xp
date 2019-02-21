package com.osm2xp.translators.impl;

import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.locationtech.jts.geom.impl.CoordinateArraySequenceFactory;

public class CSUtil {
	  /**
     * Creates a {@link CoordinateSequence} using the provided factory confirming the provided size
     * and dimension are respected.
     *
     * <p>If the requested dimension is larger than the CoordinateSequence implementation can
     * provide, then a sequence of maximum possible dimension should be created. An error should not
     * be thrown.
     *
     * <p>This method is functionally identical to calling csFactory.create(size,dim) - it contains
     * additional logic to work around a limitation on the commonly used
     * NodeCoordinateArraySequenceFactory.
     *
     * @param size the number of coordinates in the sequence
     * @param dimension the dimension of the coordinates in the sequence
     */
    public static CoordinateSequence createCS(
            CoordinateSequenceFactory csFactory, int size, int dimension) {
        // the coordinates don't have measures
        return createCS(csFactory, size, dimension, 0);
    }

    /**
     * Creates a {@link CoordinateSequence} using the provided factory confirming the provided size
     * and dimension are respected.
     *
     * <p>If the requested dimension is larger than the CoordinateSequence implementation can
     * provide, then a sequence of maximum possible dimension should be created. An error should not
     * be thrown.
     *
     * <p>This method is functionally identical to calling csFactory.create(size,dim) - it contains
     * additional logic to work around a limitation on the commonly used
     * NodeCoordinateArraySequenceFactory.
     *
     * @param size the number of coordinates in the sequence
     * @param dimension the dimension of the coordinates in the sequence
     * @param measures the measures of the coordinates in the sequence
     */
    public static CoordinateSequence createCS(
            CoordinateSequenceFactory csFactory, int size, int dimension, int measures) {
        CoordinateSequence cs;
        if (csFactory instanceof CoordinateArraySequenceFactory && dimension == 1) {
            // work around JTS 1.14 NodeCoordinateArraySequenceFactory regression ignoring provided
            // dimension
            cs = new CoordinateArraySequence(size, dimension, measures);
        } else {
            cs = csFactory.create(size, dimension, measures);
        }
        if (cs.getDimension() != dimension) {
            // illegal state error, try and fix
            throw new IllegalStateException(
                    "Unable to use"
                            + csFactory
                            + " to produce CoordinateSequence with dimension "
                            + dimension);
        }
        return cs;
    }

}
