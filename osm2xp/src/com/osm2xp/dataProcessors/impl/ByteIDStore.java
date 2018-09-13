package com.osm2xp.dataProcessors.impl;

public class ByteIDStore implements IIDStore{
	
	private long baseId;
	private byte[] coded;

	public ByteIDStore(long[] data) {
		baseId = data[0];
		coded = new byte[data.length - 1];
		for (int i = 0; i < coded.length; i++) {
			coded[i] = (byte) (data[i + 1] - baseId);
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
