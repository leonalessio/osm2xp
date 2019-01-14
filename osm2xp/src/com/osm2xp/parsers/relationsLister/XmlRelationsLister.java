package com.osm2xp.parsers.relationsLister;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.model.osm.Member;
import com.osm2xp.core.model.osm.Relation;

/**
 * Permet de lister les tuiles existantes dans le fichier osm
 * 
 * @author Benjamin Blanchet
 * 
 */
public class XmlRelationsLister implements ContentHandler, RelationsLister {

	private List<Relation> relationsList = new ArrayList<Relation>();
	private File file;
	private Boolean parsingRelation = false;
	private Relation currentRelation;

	public XmlRelationsLister(File file) {
		this.file = file;
	}

	/**
	 * @return the tilesList
	 * @throws Osm2xpBusinessException
	 */
	public List<Relation> getRelationsList() {
		return relationsList;
	}

	/**
	 * 
	 * @param uri
	 * @throws Osm2xpBusinessException
	 * @throws Exception
	 */
	public void parseDocument() throws Osm2xpBusinessException {
		XMLReader saxReader = null;

		try {
			saxReader = XMLReaderFactory.createXMLReader();
			saxReader.setContentHandler(this);
			saxReader.parse(file.getAbsolutePath());
		} catch (SAXException e) {
			throw new Osm2xpBusinessException(e.getMessage());
		} catch (IOException e) {
			throw new Osm2xpBusinessException(e.getMessage());
		}

	}

	public void startElement(String nameSpaceURI, String localName,
			String rawName, Attributes attributs) throws SAXException {

		if (localName.equalsIgnoreCase("relation")) {
			this.parsingRelation = true;
			currentRelation = new Relation();
		}
		if (parsingRelation && localName.equalsIgnoreCase("member")) {
			long id = 0;
			String ref = attributs.getValue("ref");
			if (ref != null) {
				id = Long.parseLong(ref);
			}
			Member member = new Member(id, attributs.getValue("type"), //TODO use actual id here
					ref, attributs.getValue("role"));
			currentRelation.getMember().add(member);
		}
	}

	public void setDocumentLocator(Locator locator) {

	}

	public void startDocument() throws SAXException {

	}

	public void endDocument() throws SAXException {
	}

	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {

	}

	public void endPrefixMapping(String prefix) throws SAXException {

	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName.equalsIgnoreCase("relation")) {
			this.parsingRelation = false;
			relationsList.add(currentRelation);
		}

	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {

	}

	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {

	}

	public void processingInstruction(String target, String data)
			throws SAXException {

	}

	public void skippedEntity(String name) throws SAXException {

	}

	public void process() throws Osm2xpBusinessException {
		this.parseDocument();

	}

	public void complete() {

	}

}
