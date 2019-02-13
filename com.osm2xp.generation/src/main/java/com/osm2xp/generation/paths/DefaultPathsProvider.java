package com.osm2xp.generation.paths;

import java.io.File;
import java.net.URISyntaxException;

public class DefaultPathsProvider implements IPathsProvider {

	@Override
	public File getInstallFolder() {
		try {
			return new File(DefaultPathsProvider.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (URISyntaxException e) { //Shouldn't happen
			e.printStackTrace();
			return new File(".");
		}
	}

}
