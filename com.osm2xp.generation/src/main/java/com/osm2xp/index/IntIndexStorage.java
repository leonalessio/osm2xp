package com.osm2xp.index;

import java.util.ArrayList;
import java.util.List;

/**
 * Storage for long key - T valu e pairs.
 * For now - keys should appear in strctly increasing manner
 * @author 32kda
 *
 * @param <T> Storage values type
 */
public abstract class IntIndexStorage<T> {
	
	protected List<IIdIndex<T>> indexList = new ArrayList<IIdIndex<T>>();

	protected long baseId = 0;
	
	public void add(long id, T t) {
		if (indexList.isEmpty()) {
			baseId = id;
		}
		int storeId = (int) ((id - baseId) / Character.MAX_VALUE);
		char inStoreId = (char) ((id - baseId) % Character.MAX_VALUE);
		while (indexList.size() <= storeId) {
			indexList.add(indexList.size() < storeId ? null : createIndex());
		}
		indexList.get(storeId).addItem(inStoreId, t);
	}
	
	public T get(long id) {
		int storeId = (int) ((id - baseId) / Character.MAX_VALUE);
		char inStoreId = (char) ((id - baseId) % Character.MAX_VALUE);
		if (storeId >= indexList.size()) {
			return null;
		}
		IIdIndex<T> store = indexList.get(storeId);
		if (store != null) {
			return store.getItem(inStoreId);
		}
		return null;
	}

	protected abstract IIdIndex<T> createIndex();

	public long size() {
		long sum = 0;
		for (IIdIndex<T> idIndex : indexList) {
			sum += idIndex.size();
		}
		return sum;
	}
}
