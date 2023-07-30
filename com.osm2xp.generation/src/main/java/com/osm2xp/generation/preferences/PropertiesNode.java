package com.osm2xp.generation.preferences;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

import com.osm2xp.core.logging.Osm2xpLogger;

public class PropertiesNode implements IProgramPreferenceNode {

	private Properties properties;
	private File file;
	
	public static PropertiesNode from(String nodeName, File file) {
		Properties properties = new Properties();
		if (file.isDirectory()) {
			Osm2xpLogger.error("Invalid preference node name " + nodeName + " Can't create preference file.");
			return null;
		} else if (!file.exists()) {
			try {
				Files.createFile(file.toPath());
			} catch (IOException e1) {
				Osm2xpLogger.error("Invalid preference node name " + nodeName + " Can't create preference file.");
				return null;
			}
		} else {
			try (FileInputStream inputStream = new FileInputStream(file)) {
				properties.load(inputStream);
			} catch (Exception e) {
				Osm2xpLogger.error("Error loading preferences for node: " + nodeName);
			}
		}
		return new PropertiesNode(properties, file);
	}

	private PropertiesNode(Properties properties, File file) {
		this.properties = properties;
		this.file = file;
	}

	@Override
	public String get(String property, String defaultValue) {
		return properties.getProperty(property, defaultValue);
	}

	@Override
	public void put(String property, String value) {
		properties.put(property, value);
	}
	
	public void save() {
		try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file))) {
			properties.store(stream, file.getName());
		} catch (Exception e) {
			Osm2xpLogger.log(e);
		}
	}

}
