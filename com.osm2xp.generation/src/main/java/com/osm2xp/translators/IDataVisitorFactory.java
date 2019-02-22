package com.osm2xp.translators;

import com.osm2xp.core.parsers.IOSMDataVisitor;

public interface IDataVisitorFactory {
	public IOSMDataVisitor getVisitor(String outputFolder);
	
	public String getOutputType();
}
