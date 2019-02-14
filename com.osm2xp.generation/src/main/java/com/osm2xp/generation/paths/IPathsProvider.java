package com.osm2xp.generation.paths;

import java.io.File;

public interface IPathsProvider {
	public File getInstallFolder();

	File getSpecFacadesFolder();

	void setSpecFacadesFolder(File specFacadesFolder);

	File getRoofColorFile();

	void setRoofColorFile(File roofColorFile);

	public File getObjectsFolder();
	
	public File getSpecObjectsFolder();
	
	public File getForestsFolder();
	
	public File getXPlaneToolsFolder();
}
