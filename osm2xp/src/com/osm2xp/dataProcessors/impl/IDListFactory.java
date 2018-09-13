package com.osm2xp.dataProcessors.impl;

public class IDListFactory {
	
	public Object getStore(long[] nodeIds) {
		if (nodeIds.length > 3) {
			long base = nodeIds[0];
			boolean suitsShort = true;
			boolean suitsByte = true;
			boolean suitsInt = true;
			for (int i = 1; i < nodeIds.length; i++) {
				long diff = nodeIds[i] - base;
				if (diff < 0 || diff > 255) {
					suitsByte = false;	
				} 
				if (diff < Short.MIN_VALUE || diff > Short.MAX_VALUE) {
					suitsShort = false;					
				} 
				if (diff < Integer.MIN_VALUE || diff > Integer.MAX_VALUE) {
					suitsInt = false;
					break;
				}
			}
			if (suitsByte) {
				return new ByteIDStore(nodeIds);
			}
			if (suitsShort) {
				return new ShortIDStore(nodeIds);
			}
			if (suitsInt) {
				return new IntIDStore(nodeIds);
			}
		}
		return nodeIds;
	}
	
	public long[] getIdsList(Object store) {
		if (store instanceof IIDStore) {
			return ((IIDStore) store).getIds();
		} else if (store instanceof long[]) {
			return (long[]) store;
		}
		throw new IllegalArgumentException("Store of type " + store + " is not supported!");
	}

}
