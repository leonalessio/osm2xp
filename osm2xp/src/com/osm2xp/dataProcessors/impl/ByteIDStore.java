package com.osm2xp.dataProcessors.impl;

public class ByteIDStore implements IIDStore{
	
	private long baseId;
	private byte[] coded;

	public ByteIDStore(long[] data, long baseId) {
		this.baseId = baseId;
		coded = new byte[data.length];
		for (int i = 0; i < coded.length; i++) {
			coded[i] = (byte) (data[i] - baseId);
		}
	}

	@Override
	public long[] getIds() {
		long[] result = new long[coded.length];
		for (int i = 0; i < coded.length; i++) {
			result[i] = baseId + (coded[i] & 0xFF);
		}
		return result;
	}
	
}
