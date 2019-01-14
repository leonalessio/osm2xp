package com.osm2xp.parsers.relationsLister;

import java.util.List;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.model.osm.Relation;

/**
 * RelationsLister.
 * 
 * @author Benjamin Blanchet
 * 
 */
public interface RelationsLister {

	public List<Relation> getRelationsList();

	public void process() throws Osm2xpBusinessException;
}
