package com.osm2xp.translators;

import com.osm2xp.core.parsers.IOSMDataVisitor;

public interface IDataVisitorFactory extends ITranslatorFactory {
	public IOSMDataVisitor getVisitor(String outputFolder);
}
