package com.osm2xp.translators.airfield;

import com.osm2xp.converters.impl.SpecificTranslatingConverter;
import com.osm2xp.core.exceptions.DataSinkException;
import com.osm2xp.core.parsers.IOSMDataVisitor;
import com.osm2xp.datastore.DataSinkFactory;
import com.osm2xp.translators.IDataVisitorFactory;

public class XPAirfieldGeneratorFactory implements IDataVisitorFactory {

	@Override
	public IOSMDataVisitor getVisitor(String outputFolder) {
		try {
			return new SpecificTranslatingConverter(new XPAirfieldTranslationAdapter(outputFolder), DataSinkFactory.getProcessor(), null);
		} catch (DataSinkException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getOutputType() {
		return "XP_AIRFIELDS";
	}

}
