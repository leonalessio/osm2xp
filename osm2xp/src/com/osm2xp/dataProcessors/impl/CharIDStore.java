package com.osm2xp.dataProcessors.impl;

public class CharIDStore implements IIDStore{
	
	private long baseId;
	/**
	 * Java doesn't have unsigned short, 'char' is bit hacky replacement for it
	 */
	private char[] coded;

	public CharIDStore(long[] data, long baseId) {
		this.baseId = baseId;
		coded = new char[data.length];
		for (int i = 0; i < coded.length; i++) {
			coded[i] = (char) (data[i] - baseId);
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
