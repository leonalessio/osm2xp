package com.osm2xp.generation.xplane.resources;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import com.osm2xp.core.logging.Osm2xpLogger;

public class ResourceLibraryDescriptor {
	
	protected List<String> polygonResources;
	protected List<String> objectResources;
	protected File parentFolder;
	protected boolean separateLibrary;
	
	public ResourceLibraryDescriptor(List<String> polygonResources, List<String> objectResources, File parentFolder,
			boolean separateLibrary) {
		super();
		this.polygonResources = polygonResources;
		this.objectResources = objectResources;
		this.parentFolder = parentFolder;
		this.separateLibrary = separateLibrary;
	}
	
	public void processResourceLibrary() {
		if (separateLibrary) {
			XPLibraryOutputFormat format = new XPLibraryOutputFormat(getLibPathPreffix());
			try (PrintWriter printWriter = new PrintWriter(
					new BufferedWriter(new FileWriter(new File(parentFolder, "library.txt"))))) {
				printWriter.println(format.getHeaderString());
				for (String entry : polygonResources) {
					if (new File(parentFolder, entry).isFile()) { //Is treated as another library link otherwise
						printWriter.println(format.getRecordString(entry));
					}
				}
				for (String entry : objectResources) {
					if (new File(parentFolder, entry).isFile()) { //Is treated as another library link otherwise
						printWriter.println(format.getRecordString(entry));
					}
				}
			} catch (Exception e) {
				Osm2xpLogger.error("Error generating library.txt descriptor", e);
			} 
		}
	}

	private String getLibPathPreffix() {
		return "osm2xp/";
	}

	public List<String> getPreffixedPolyDefinitions() {
		if (separateLibrary) {
			return polygonResources.stream().map(res -> checkAddPreffix(res)).collect(Collectors.toList());
		} else {
			return polygonResources;
		}
	}
	
	public List<String> getPreffixedObjectDefinitions() {
		if (separateLibrary) {
			return objectResources.stream().map(res -> checkAddPreffix(res)).collect(Collectors.toList());
		} else {
			return objectResources;
		}
	}

	private String checkAddPreffix(String res) {
		if (new File(parentFolder, res).exists()) {
			return getLibPathPreffix() + res;
		}
		return res;
	}
	
}
