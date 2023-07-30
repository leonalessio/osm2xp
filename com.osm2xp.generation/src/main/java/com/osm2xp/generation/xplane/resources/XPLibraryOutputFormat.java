package com.osm2xp.generation.xplane.resources;

public class XPLibraryOutputFormat {
	
	private String virtualPathPreffix;

	public XPLibraryOutputFormat(String virtualPathPreffix) {
		this.virtualPathPreffix = virtualPathPreffix;
	}
	
	public String getHeaderString() {
		return  "A\r\n" + 
				"800\r\n" + 
				"LIBRARY\r\n";
		
	}
	
	public String getRecordString(String relPath) {
		return String.format("EXPORT %s %s", virtualPathPreffix + relPath, relPath);
	}
}
