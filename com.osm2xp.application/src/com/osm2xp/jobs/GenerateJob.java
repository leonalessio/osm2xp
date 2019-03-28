package com.osm2xp.jobs;

import java.io.File;

import org.eclipse.core.runtime.jobs.Job;

public abstract class GenerateJob extends Job {

	protected String family;
	protected transient File currentFile;
	protected transient String folderPath;
	
	public GenerateJob(String name, File currentFile, 
			String folderPath, String family) {
		super(name);
		this.currentFile = currentFile;
		this.folderPath = folderPath;
		this.family = family;
	}

	public boolean belongsTo(Object family) {
		return this.family.equals(family);
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String familly) {
		this.family = familly;
	}

}