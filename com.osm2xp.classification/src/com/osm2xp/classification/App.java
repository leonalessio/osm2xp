package com.osm2xp.classification;

import java.io.File;

import com.osm2xp.classification.parsing.LearningDataParser;

public class App {

	public static void main(String[] args) {
		new LearningDataParser(new File("F:/tmp/siberian-fed-district-latest.osm.pbf"));
	}

}
