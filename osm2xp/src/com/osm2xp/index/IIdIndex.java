package com.osm2xp.index;

public interface IIdIndex<T> {

	public void addItem(char id, T t);
	public T getItem(char id);
	public int size();
	
}
