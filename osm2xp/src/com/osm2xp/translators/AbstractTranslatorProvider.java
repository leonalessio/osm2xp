package com.osm2xp.translators;

import java.io.File;

public abstract class AbstractTranslatorProvider implements ITranslatorProvider {

	protected File binaryFile;
	protected String folderPath;

	public AbstractTranslatorProvider(File binaryFile, String folderPath) {
		this.binaryFile = binaryFile;
		this.folderPath = folderPath;
	}
	
}
