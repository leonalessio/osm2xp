package com.osm2xp.index;

import java.util.Arrays;
import java.util.Collection;
/**
 * @author 32kda
 */
public class PointCoordsIndex extends AbstractPointCoordsIndex{
	
	private static final double PACK_FACTOR = 10000000.0;
    
    private static final double MAX_PACKED_VALUE = Integer.MAX_VALUE / PACK_FACTOR;
	
	 /**
     * The array buffer into which the elements of the ArrayList are stored.
     * The capacity of the ArrayList is the length of this array buffer. Any
     * empty ArrayList with elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
     * will be expanded to DEFAULT_CAPACITY when the first element is added.
     * 
     * 	
	 * Java doesn't have unsigned short, 'char' is bit hacky replacement for it
	 */
  
    transient int[] latArray; 
    transient int[] lonArray; 
    
    transient double baseLon;
    transient double baseLat;
      
    
    protected long baseId = 0;

    /**
     * The size of the ArrayList (the number of elements it contains).
     *
     * @serial
     */
    private int size;
    
    public PointCoordsIndex(int initialCapacity) {
    	super(initialCapacity);
    	latArray = new int[initialCapacity];
    	lonArray = new int[initialCapacity];
	}
    
    public PointCoordsIndex() {
    	this(DEFAULT_CAPACITY);
	}

	 protected // Positional Access Operations

    double[] elementData(int index) {
        return new double[] {intToLon(lonArray[index]), intToLat(latArray[index])};
    }

    private double intToLon(int intLon) {
		return baseLon + intLon / PACK_FACTOR;
	}

	private double intToLat(int intLat) {
		return baseLat + intLat / PACK_FACTOR;
	}

	/**
     * Appends the specified element to the end of this list.
     *
     * @param e element to be appended to this list
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     */
    public boolean add(char pointId, double lon, double lat) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        idArray[size] = pointId;
        lonArray[size] = lonToInt(lon);
        latArray[size] = latToInt(lat);
        size++;
        return true;
    }

    private int lonToInt(double lon) {
    	if (size == 0) {
    		baseLon = lon;
    	}
    	double packedVal = (lon - baseLon) * PACK_FACTOR;
		if (Math.abs(lon - baseLon) > MAX_PACKED_VALUE) {
			if (lon * baseLon < 0) { //Should be around meridian180
			} else {
				System.out.println("PointIndex.lonToInt() - diff value too large");
			}
    	}
		return (int) Math.round(packedVal);
	}
    
    private int latToInt(double lat) {
    	if (size == 0) {
    		baseLat = lat;
    	}
    	double packedVal = (lat - baseLat) * PACK_FACTOR;
		if (Math.abs(lat - baseLat) > MAX_PACKED_VALUE) {
    		System.out.println("PointIndex.latToInt() - diff value too large");
    	}
		return (int) Math.round(packedVal);
	}

    /**
     * The maximum size of array to allocate.
     * Some VMs reserve some header words in an array.
     * Attempts to allocate larger arrays may result in
     * OutOfMemoryError: Requested array size exceeds VM limit
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * Increases the capacity to ensure that it can hold at least the
     * number of elements specified by the minimum capacity argument.
     *
     * @param minCapacity the desired minimum capacity
     */
    protected void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = idArray.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        try {
	        idArray = Arrays.copyOf(idArray, newCapacity);
	        lonArray = Arrays.copyOf(lonArray, newCapacity);
	        latArray = Arrays.copyOf(latArray, newCapacity);
        } catch (OutOfMemoryError e) {
        	throw new Error("Error growing point index, current size " + size + " new capacity " + newCapacity, e);
		}
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }
  
	@Override
	public void addItem(char id, double[] coords) {
		add(id, coords[0], coords[1]);
	}

	@Override
	public double[] getItem(char id) {
		return getPoint(id);
	}

	
}
