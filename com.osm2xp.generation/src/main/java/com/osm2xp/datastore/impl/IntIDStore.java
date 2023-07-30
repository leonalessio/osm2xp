package com.osm2xp.datastore.impl;

public class IntIDStore implements IIDStore{
	
	private long baseId;
	private int[] coded;

	public IntIDStore(long[] data, long baseId) {
		this.baseId = baseId;
		coded = new int[data.length];
		for (int i = 0; i < coded.length; i++) {
			coded[i] = (int) (data[i] - baseId);
		}
	}

	@Override
	public long[] getIds() {
		long[] result = new long[coded.length];
		for (int i = 0; i < coded.length; i++) {
			result[i] = baseId + coded[i];
		}
		return result;
	}
	
}
