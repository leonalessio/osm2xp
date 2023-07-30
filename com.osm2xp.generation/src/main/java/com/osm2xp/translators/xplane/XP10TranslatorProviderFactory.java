package com.osm2xp.translators.xplane;

import java.io.File;

import com.osm2xp.translators.ITranslatorProvider;
import com.osm2xp.translators.ITranslatorProviderFactory;

public class XP10TranslatorProviderFactory implements ITranslatorProviderFactory {

	@Override
	public ITranslatorProvider getTranslatorProvider(File currentFile, String folderPath) {
		return new XPlane10TranslatorProvider(currentFile, folderPath);
	}

	@Override
	public String getOutputMode() {
		return "XPLANE10";
	}

	@Override
	public boolean isFileWriting() {
		return true;
	}

}
