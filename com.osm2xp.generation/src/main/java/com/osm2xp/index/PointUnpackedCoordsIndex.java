package com.osm2xp.index;

import java.util.Arrays;

public class PointUnpackedCoordsIndex extends AbstractPointCoordsIndex {
	
	transient double[] latArray;
	transient double[] lonArray;

	protected long baseId = 0;

	public PointUnpackedCoordsIndex(int initialCapacity) {
		super(initialCapacity);
		latArray = new double[initialCapacity];
		lonArray = new double[initialCapacity];
	}

	public PointUnpackedCoordsIndex() {
		this(DEFAULT_CAPACITY);
	}

	protected // Positional Access Operations
	double[] elementData(int index) {
		return new double[] { lonArray[index], latArray[index] };
	}


	/**
	 * Appends the specified element to the end of this list.
	 *
	 * @param e element to be appended to this list
	 * @return <tt>true</tt> (as specified by {@link Collection#add})
	 */
	public boolean add(char pointId, double lon, double lat) {
		ensureCapacityInternal(size + 1); // Increments modCount!!
		idArray[size] = pointId;
		lonArray[size] = lon;
		latArray[size] = lat;
		size++;
		return true;
	}

	/**
	 * The maximum size of array to allocate. Some VMs reserve some header words in
	 * an array. Attempts to allocate larger arrays may result in OutOfMemoryError:
	 * Requested array size exceeds VM limit
	 */
	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

	/**
	 * Increases the capacity to ensure that it can hold at least the number of
	 * elements specified by the minimum capacity argument.
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
		return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
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
