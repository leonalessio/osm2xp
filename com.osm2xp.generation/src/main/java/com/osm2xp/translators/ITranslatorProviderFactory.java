package com.osm2xp.translators;

import java.io.File;

public interface ITranslatorProviderFactory extends ITranslatorFactory {

	public ITranslatorProvider getTranslatorProvider(File currentFile, String folderPath);

}
