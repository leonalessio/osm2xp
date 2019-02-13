package com.osm2xp.index;

import java.util.Arrays;
import java.util.Collection;
/**
 * @author 32kda
 */
public class IdToObjectIndex implements IIdIndex<Object>{
	
	/**
     * Default initial capacity.
     */
    private static final int DEFAULT_CAPACITY = 100;
    /**
     * Min distance, on which we no longer go with binary search, but just iterate over all elements
     */
    private static final int MIN_SEARCH_DIST = 5;
    
	
	 /**
     * The array buffer into which the elements of the ArrayList are stored.
     * The capacity of the ArrayList is the length of this array buffer. Any
     * empty ArrayList with elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
     * will be expanded to DEFAULT_CAPACITY when the first element is added.
     */
    transient char[] idArray; 
    transient Object[] objectArray; 
    
    
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
    
    public IdToObjectIndex(int initialCapacity) {
    	idArray = new char[initialCapacity];
    	objectArray = new Object[initialCapacity];
	}
    
    public IdToObjectIndex() {
    	this(DEFAULT_CAPACITY);
	}

	 // Positional Access Operations

    Object elementData(int index) {
        return objectArray[index];
    }


	/**
     * Returns the element at the specified position in this list.
     *
     * @param  index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public Object get(int index) {
        rangeCheck(index);

        return elementData(index);
    }
    
    public Object getItem(char id) {
    	if (idArray.length == 0 || size == 0) {
    		return null;
    	}
    	int start = 0;
    	int end = size - 1;
    	
    	char startId = idArray[start];
    	char endId = idArray[end];
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

    protected Object iterateSearch(long id, int start, int end) {
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
    public void addItem(char pointId, Object object) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        idArray[size] = pointId;
        objectArray[size] = object;
        size++;
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
	        objectArray = Arrays.copyOf(objectArray, newCapacity);
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

	@Override
	public int size() {
		return size;
	}
	
}
