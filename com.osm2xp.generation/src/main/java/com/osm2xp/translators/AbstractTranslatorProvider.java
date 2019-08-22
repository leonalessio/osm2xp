package com.osm2xp.translators;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.osm2xp.core.parsers.IOSMDataVisitor;

public abstract class AbstractTranslatorProvider implements ITranslatorProvider {

	protected File binaryFile;
	protected String folderPath;

	public AbstractTranslatorProvider(File binaryFile, String folderPath) {
		this.binaryFile = binaryFile;
		this.folderPath = folderPath;
	}
	
	@Override
	public Collection<ISpecificTranslator> createAdditinalAdapters() {
		return new ArrayList<>();
	}

	@Override
	public Collection<IOSMDataVisitor> createPreprocessors() {
		return Collections.emptyList();
	}
	
	
	
}
