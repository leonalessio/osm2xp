package com.osm2xp.dataProcessors.impl;

public class IDListFactory {
	
	public Object getStore(long[] nodeIds) {
		if (nodeIds.length > 3) {
			long min = nodeIds[0];
			long max = nodeIds[0];
			for (int i = 1; i < nodeIds.length; i++) {
				if (min > nodeIds[i]) {
					min = nodeIds[i];
				}
				if (max < nodeIds[i]) {
					max = nodeIds[i];
				}
			}
			long diff = max - min;
			if (diff < 255) {
				return new ByteIDStore(nodeIds, min);	
			} 
			if (diff < Character.MAX_VALUE) {
				return new CharIDStore(nodeIds, min);
			} 
			long midId = min + (max - min) / 2;
			if (min - midId > Integer.MIN_VALUE && max - midId < Integer.MAX_VALUE) {
				return new IntIDStore(nodeIds, midId);
			}
		}
		return nodeIds;
	}
	
	public long[] getIdsList(Object store) {
		if (store instanceof IIDStore) {
			return ((IIDStore) store).getIds();
		} else if (store instanceof long[]) {
			return (long[]) store;
		} else if (store == null) {
			return null;
		}
		throw new IllegalArgumentException("Store of type " + store + " is not supported!");
	}

}
