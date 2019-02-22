package com.osm2xp.translators;

import java.io.File;

public interface ITranslatorProviderFactory {

	public ITranslatorProvider getTranslatorProvider(File currentFile, String folderPath);

	public String getOutputType();

}
