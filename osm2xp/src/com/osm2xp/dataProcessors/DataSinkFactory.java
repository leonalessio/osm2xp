package com.osm2xp.dataProcessors;

import com.osm2xp.dataProcessors.impl.MemoryCriticalProcessorImpl;
import com.osm2xp.exceptions.DataSinkException;

/**
 * Data Sink Factory.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class DataSinkFactory {

	public static IDataSink getProcessor() throws DataSinkException {
		return new MemoryCriticalProcessorImpl();
//		if (GuiOptionsHelper.getOptions().isDatabaseMode()) { //XXX debug
//			return new Jdbm2ProcessorImpl();
//		} else {
//			return new MemoryProcessorImpl();
//		}

	}
}
