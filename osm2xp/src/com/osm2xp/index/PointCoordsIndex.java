package com.osm2xp.index;

import java.util.Arrays;
import java.util.Collection;
/**
 * @author 32kda
 */
public class PointCoordsIndex implements IIdIndex<double[]>{
	
	private static final double PACK_FACTOR = 10000000.0;
	/**
     * Default initial capacity.
     */
    private static final int DEFAULT_CAPACITY = 100;
    /**
     * Min distance, on which we no longer go with binary search, but just iterate over all elements
     */
    private static final int MIN_SEARCH_DIST = 5;
    
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
  
    transient char[] idArray; 
    transient int[] latArray; 
    transient int[] lonArray; 
    
    transient double baseLon;
    transient double baseLat;
    
    /**
     * The number of times this list has been <i>structurally modified</i>.
     * Structural modifications are those that change the size of the
     * list, or otherwise perturb it in such a fashion that iterations in
     * progress may yield incorrect results.
     *
     * <p>This field is used by the iterator and list iterator implementation
     * returned by the {@code iterator} and {@code listIterator} methods.
     * If the value of this field changes unexpectedly, the iterator (or list
     * iterator) will throw a {@code ConcurrentModificationException} in
     * response to the {@code next}, {@code remove}, {@code previous},
     * {@code set} or {@code add} operations.  This provides
     * <i>fail-fast</i> behavior, rather than non-deterministic behavior in
     * the face of concurrent modification during iteration.
     *
     * <p><b>Use of this field by subclasses is optional.</b> If a subclass
     * wishes to provide fail-fast iterators (and list iterators), then it
     * merely has to increment this field in its {@code add(int, E)} and
     * {@code remove(int)} methods (and any other methods that it overrides
     * that result in structural modifications to the list).  A single call to
     * {@code add(int, E)} or {@code remove(int)} must add no more than
     * one to this field, or the iterators (and list iterators) will throw
     * bogus {@code ConcurrentModificationExceptions}.  If an implementation
     * does not wish to provide fail-fast iterators, this field may be
     * ignored.
     */
    protected transient int modCount = 0;
    
    protected long baseId = 0;

    /**
     * The size of the ArrayList (the number of elements it contains).
     *
     * @serial
     */
    private int size;
    
    public PointCoordsIndex(int initialCapacity) {
    	idArray = new char[initialCapacity];
    	latArray = new int[initialCapacity];
    	lonArray = new int[initialCapacity];
	}
    
    public PointCoordsIndex() {
    	this(DEFAULT_CAPACITY);
	}

	 // Positional Access Operations

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
     * Returns the element at the specified position in this list.
     *
     * @param  index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public double[] get(int index) {
        rangeCheck(index);

        return elementData(index);
    }
    
    public double[] getPoint(int id) {
    	if (idArray.length == 0 || size == 0) {
    		return null;
    	}
    	int start = 0;
    	int end = size - 1;
    	
    	long startId = idArray[start];
    	long endId = idArray[end];
    	if (id < startId || id > endId) {
    		return null;
    	}
    	int newPos = (int) Math.round((1.0 * (id - startId) / (endId - startId) * (end - start)));
    	while (end - start > MIN_SEARCH_DIST) {
    		if (idArray[newPos] == id) {
    			return get(newPos);
    		} else if (idArray[newPos] < id) {
    			start = newPos + 1;
    			startId = idArray[newPos + 1];
    		} else {
    			end = newPos - 1;
    			endId = idArray[newPos - 1];
    		}
    		newPos = start + ((end - start) / 2);
    	}
    	return iterateSearch(id, start, end);
    }

    protected double[] iterateSearch(long id, int start, int end) {
    	for (int i = start; i <= end; i++) {
			if (idArray[i] == id) {
				return get(i);
			}
		}
		return null;
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
    		System.out.println("PointIndex.lonToInt() - diff value too large");
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


	private void ensureCapacityInternal(int minCapacity) {
        if (idArray.length == 0) {
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }

        ensureExplicitCapacity(minCapacity);
    }

    private void ensureExplicitCapacity(int minCapacity) {
        modCount++;

        // overflow-conscious code
        if (minCapacity - idArray.length > 0)
            grow(minCapacity);
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
    private void grow(int minCapacity) {
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
    
    /**
     * Checks if the given index is in range.  If not, throws an appropriate
     * runtime exception.  This method does *not* check if the index is
     * negative: It is always used immediately prior to an array access,
     * which throws an ArrayIndexOutOfBoundsException if index is negative.
     */
    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * Constructs an IndexOutOfBoundsException detail message.
     * Of the many possible refactorings of the error handling code,
     * this "outlining" performs best with both server and client VMs.
     */
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

	public int size() {
		return size;
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
