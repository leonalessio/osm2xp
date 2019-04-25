package com.osm2xp.stats;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CountStats implements ICountStats {
	
	private Map<String, Integer> countMap = new HashMap<String, Integer>();

	@Override
	public int getCount(String id) {
		Integer cnt = countMap.get(id);
		return cnt != null ? cnt : 0;
	}

	@Override
	public void incCount(String id) {
		Integer cnt = countMap.get(id);
		countMap.put(id, cnt != null ? ++cnt : 1);
	}

	public String getSummary() {
		return countMap.keySet().stream().sorted().map(key -> key + ":" + countMap.get(key)).collect(Collectors.joining(", "));
	}

	@Override
	public void setCount(String id, int count) {
		countMap.put(id, count);
	}

}
