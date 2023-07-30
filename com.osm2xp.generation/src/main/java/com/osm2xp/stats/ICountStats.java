package com.osm2xp.stats;

public interface ICountStats {
	
	public int getCount(String id);
	
	public void incCount(String id);
	
	public void setCount(String id, int count);
}
