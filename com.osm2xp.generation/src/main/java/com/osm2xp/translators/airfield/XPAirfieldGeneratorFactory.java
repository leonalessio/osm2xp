package com.osm2xp.translators.airfield;

import com.osm2xp.converters.impl.SpecificTranslatingConverter;
import com.osm2xp.core.exceptions.DataSinkException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.parsers.IOSMDataVisitor;
import com.osm2xp.datastore.DataSinkFactory;
import com.osm2xp.translators.IDataVisitorFactory;

public class XPAirfieldGeneratorFactory implements IDataVisitorFactory {

	@Override
	public IOSMDataVisitor getVisitor(String outputFolder) {
		try {
			return new SpecificTranslatingConverter(new XPAirfieldTranslationAdapter(outputFolder), DataSinkFactory.getDataSink());
		} catch (DataSinkException e) {
			Osm2xpLogger.error(e);
		}
		return null;
	}

	@Override
	public String getOutputMode() {
		return "XP_AIRFIELDS";
	}

	@Override
	public boolean isFileWriting() {
		return true;
	}

}
