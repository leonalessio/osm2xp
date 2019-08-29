package com.osm2xp.translators;

import java.util.Collection;

import com.osm2xp.core.parsers.IOSMDataVisitor;
import com.osm2xp.datastore.IDataSink;

public interface IPreprocessorProvider {
	
	public Collection<IOSMDataVisitor> createPreprocessors(IDataSink dataSink);
	
}
