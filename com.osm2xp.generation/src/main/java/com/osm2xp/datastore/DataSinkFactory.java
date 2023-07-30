package com.osm2xp.datastore;

import com.osm2xp.core.exceptions.DataSinkException;
import com.osm2xp.datastore.impl.MapDBProcessorImpl;
import com.osm2xp.datastore.impl.MemoryCriticalProcessorImpl;
import com.osm2xp.generation.options.GlobalOptionsProvider;

/**
 * Data Sink Factory.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class DataSinkFactory {

	public static IDataSink getDataSink() throws DataSinkException {
		if (GlobalOptionsProvider.getOptions().isDatabaseMode()) { 
			return new MapDBProcessorImpl();
		} else {
			return new MemoryCriticalProcessorImpl();
		}

	}
}
