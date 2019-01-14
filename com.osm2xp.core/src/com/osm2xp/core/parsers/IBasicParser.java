package com.osm2xp.core.parsers;

import com.osm2xp.core.exceptions.OsmParsingException;

public interface IBasicParser {
	
	public void process() throws OsmParsingException;

	public void complete();

}
