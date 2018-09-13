package com.osm2xp.dataProcessors.impl;

public class IntIDStore implements IIDStore{
	
	private long baseId;
	private int[] coded;

	public IntIDStore(long[] data) {
		baseId = data[0];
		coded = new int[data.length - 1];
		for (int i = 0; i < coded.length; i++) {
			coded[i] = (int) (data[i + 1] - baseId);
		}
	}

	@Override
	public long[] getIds() {
		long[] result = new long[coded.length + 1];
		result[0] = baseId;
		for (int i = 0; i < coded.length; i++) {
			result[i + 1] = baseId + coded[i];
		}
		return result;
	}
	
}
