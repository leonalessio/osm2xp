package com.osm2xp.generation.paths;

import java.io.File;
import java.net.URISyntaxException;

public class DefaultPathsProvider implements IPathsProvider {
	
	protected File specFacadesFolder;
	private File specObjectsFolder;
	private File objectsFolder;
	private File forestsFolder;
	private File xPlaneToolsFolder;
	private File roofColorFile = null;

	@Override
	public File getInstallFolder() {
		try {
			return new File(DefaultPathsProvider.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (URISyntaxException e) { //Shouldn't happen
			e.printStackTrace();
			return new File(".");
		}
	}
	
	@Override
	public File getSpecFacadesFolder() {
		if (specFacadesFolder == null) {
			return new File(getInstallFolder(),"/resources/specfacades");
		}
		return specFacadesFolder;
	}

	@Override
	public void setSpecFacadesFolder(File specFacadesFolder) {
		this.specFacadesFolder = specFacadesFolder;
	}
	
	@Override
	public void setRoofColorFile(File roofColorFile) {
		this.roofColorFile = roofColorFile;
	}

	@Override
	public File getRoofColorFile() {
		return roofColorFile;
	}

	@Override
	public File getSpecObjectsFolder() {
		if (specObjectsFolder == null) {
			return new File(getInstallFolder(),"/resources/specobjects");
		}
		return specObjectsFolder;
	}

	public void setSpecObjectsFolder(File specObjectsFolder) {
		this.specObjectsFolder = specObjectsFolder;
	}

	@Override
	public File getObjectsFolder() {
		if (objectsFolder == null) {
			return new File(getInstallFolder(),"/resources/objects");
		}
		return objectsFolder;
	}

	public void setObjectsFolder(File objectsFolder) {
		this.objectsFolder = objectsFolder;
	}

	@Override
	public File getForestsFolder() {
		if (forestsFolder == null) {
			return new File(getInstallFolder(),"/resources/forests");
		}
		return forestsFolder;
	}

	@Override
	public File getXPlaneToolsFolder() {
		if (xPlaneToolsFolder == null) {
			return new File(getInstallFolder(),"/tools/xplane");
		}
		return xPlaneToolsFolder;
	}

}
