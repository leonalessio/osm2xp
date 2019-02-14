package com.osm2xp.generation.preferences;

import java.io.File;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import com.osm2xp.generation.paths.PathsService;

public class MapDBPreferences implements IProgramPreferences {
	
	private DB prefsDB;

	public MapDBPreferences() {
		File prefsFolder = new File(PathsService.getPathsProvider().getInstallFolder(),"preferences");
		prefsFolder.mkdir();
		File prefsFile = new File(prefsFolder, "preferences.dat");
		prefsDB = DBMaker.fileDB(prefsFile).fileMmapEnableIfSupported().closeOnJvmShutdown().make();
	}

	@Override
	public void flush(IProgramPreferenceNode node) {
		prefsDB.commit();
	}

	@Override
	public IProgramPreferenceNode getNode(String nodeName) {
		return new HashMapPrefsNode(prefsDB.hashMap(nodeName, Serializer.STRING, Serializer.STRING).createOrOpen());
	}

}
