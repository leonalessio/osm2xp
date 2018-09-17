package com.osm2xp.dataProcessors;

import com.osm2xp.dataProcessors.impl.Jdbm2ProcessorImpl;
import com.osm2xp.dataProcessors.impl.MemoryCriticalProcessorImpl;
import com.osm2xp.exceptions.DataSinkException;
import com.osm2xp.utils.helpers.GuiOptionsHelper;

/**
 * Data Sink Factory.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class DataSinkFactory {

	public static IDataSink getProcessor() throws DataSinkException {
		if (GuiOptionsHelper.getOptions().isDatabaseMode()) { 
			return new Jdbm2ProcessorImpl();
		} else {
			//return new MemoryProcessorImpl();
			return new MemoryCriticalProcessorImpl();
		}

	}
}
