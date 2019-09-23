package com.osm2xp.parsers.relationsLister;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.openstreetmap.osmosis.osmbinary.BinaryParser;
import org.openstreetmap.osmosis.osmbinary.Osmformat;
import org.openstreetmap.osmosis.osmbinary.Osmformat.*;
import org.openstreetmap.osmosis.osmbinary.file.BlockInputStream;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.model.osm.Member;
import com.osm2xp.core.model.osm.Relation;

/**
 * PbfRelationsLister.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class PbfRelationsLister extends BinaryParser implements RelationsLister {

	private List<Relation> relationsList = new ArrayList<Relation>();
	private File file;

	public PbfRelationsLister(File file) {
		this.file = file;

	}

	/**
	 * 
	 */
	public void complete() {

	}

	@Override
	protected void parseRelations(List<Osmformat.Relation> rels) {
		if (rels != null && rels.size() > 0) {
			for (Osmformat.Relation rel : rels) {
				Relation relation = new Relation();
				relation.setId(rel.getId());
				long lastMemberId = 0;
				for (int i = 0; i < rel.getMemidsList().size(); i++) {
					long memberId = lastMemberId + rel.getMemids(i);
					lastMemberId = memberId;
					Integer rolesSid = rel.getRolesSidList().get(i);
					String type = rel.getTypesList().get(i).toString();
					String role = getStringById(rolesSid);
					String ref = rel.getMemidsList().get(i).toString();
					relation.getMember().add(new Member(memberId,type, ref, role));
				}
				relationsList.add(relation);
			}
		}
	}

	@Override
	protected void parseDense(DenseNodes nodes) {

	}

	@Override
	protected void parseNodes(List<Node> nodes) {
	}

	@Override
	protected void parseWays(List<Way> ways) {

	}

	@Override
	protected void parse(HeaderBlock header) {
	}

	public void process() throws Osm2xpBusinessException {

		InputStream input = null;
		try {
			input = new FileInputStream(this.file);
		} catch (FileNotFoundException e) {
			throw new Osm2xpBusinessException(e.getMessage());
		}
		BlockInputStream bm = new BlockInputStream(input, this);
		try {
			bm.process();
		} catch (IOException e) {
			throw new Osm2xpBusinessException(e.getMessage());

		}
	}

	public List<Relation> getRelationsList() {
		return this.relationsList;
	}

}
