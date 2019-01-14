package com.osm2xp.core.parsers.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.osm2xp.core.exceptions.DataSinkException;
import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.exceptions.OsmParsingException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Member;
import com.osm2xp.core.model.osm.Nd;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Relation;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.core.model.osm.Way;
import com.osm2xp.core.parsers.IOSMDataVisitor;
import com.osm2xp.core.parsers.IVisitingParser;

/**
 * Sax parser implementation.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class SaxParserImpl implements ContentHandler, IVisitingParser {

	private static final String XML_ATTRIBUTE_LONGITUDE = "lon";
	private static final String XML_ATTRIBUTE_LATITUDE = "lat";
	private static final String XML_ATTRIBUTE_ID = "id";
	private static final String XML_ATTRIBUTE_REF = "ref";
	private static final String XML_NODE_ND = "nd";
	private static final String XML_NODE_TAG = "tag";
	private static final String XML_NODE_NODE = "node";
	private static final String XML_NODE_MEMBER = "member";
	private static final String XML_NODE_WAY = "way";
	private static final String XML_NODE_RELATION = "relation";
	protected Locator locator;
	protected List<Tag> tagList;
	protected List<Member> membersList;
	protected OsmAttributes currentAttributes;
	protected List<Nd> ndList;
	private File xmlFile;
//	private boolean nodesRefCollectionDone;
	private IOSMDataVisitor visitor;

	public SaxParserImpl(File xmlFile, IOSMDataVisitor visitor) {
		this.xmlFile = xmlFile;
		this.visitor = visitor;
	}

	/**
	 * 
	 * @param uri
	 * @throws Osm2xpBusinessException
	 * @throws Exception
	 */
	public void parseDocument() throws SAXException, Osm2xpBusinessException {
		try {
	        SAXParserFactory factory = SAXParserFactory.newInstance();
	        SAXParser parser = factory.newSAXParser();
			XMLReader saxReader = parser.getXMLReader();
			saxReader.setContentHandler(this);
			saxReader.parse(this.xmlFile.getAbsolutePath());
		} catch (Exception e) {
			throw new Osm2xpBusinessException(e.getMessage());
		}
	}

	public void setDocumentLocator(Locator value) {
		this.locator = value;
	}

	public Locator getDocumentLocator() {
		return this.locator;
	}

	public void startDocument() throws SAXException {

	}

	public void endDocument() throws SAXException {
		complete();
	}

	public void startPrefixMapping(String prefix, String URI)
			throws SAXException {

	}

	public void endPrefixMapping(String prefix) throws SAXException {

	}

	public void startElement(String nameSpaceURI, String localName, String rawName, Attributes attributs) {
		String name = !localName.isEmpty() ? localName : rawName;
		if (name.equalsIgnoreCase(XML_NODE_WAY) || name.equalsIgnoreCase(XML_NODE_RELATION)
				|| name.equalsIgnoreCase(XML_NODE_NODE)) {
			tagList = new ArrayList<Tag>();
			ndList = new ArrayList<Nd>();
			membersList = new ArrayList<>();
			currentAttributes = new OsmAttributes(attributs);
		} else if (name.equalsIgnoreCase(XML_NODE_TAG)) {
			Tag tag = new Tag();
			tag.setKey(attributs.getValue(0));
			tag.setValue(attributs.getValue(1));
			tagList.add(tag);
		} else if (name.equalsIgnoreCase(XML_NODE_ND)) {
			Nd nd = new Nd();
			nd.setRef(Long.parseLong(attributs.getValue(XML_ATTRIBUTE_REF)));
			this.ndList.add(nd);
		} else if (name.equalsIgnoreCase(XML_NODE_MEMBER)) {
			long id = 0;
			String ref = attributs.getValue("ref");
			if (ref != null) {
				id = Long.parseLong(ref);
			}
			membersList.add(new Member(id, attributs.getValue("type"), //TODO use actual id here
					ref, attributs.getValue("role")));
		}
	}

	@Override
	public void endElement(String nameSpaceURI, String localName, String rawName)
			throws SAXException {
		String name = !localName.isEmpty() ? localName : rawName;
		if (name.equals(XML_NODE_WAY)) {
			try {
				parseWay();
			} catch (Osm2xpBusinessException e) {
				Osm2xpLogger.error("Error parsing way object.", e);
			} catch (DataSinkException e) {
				Osm2xpLogger.error("Error parsing way object.", e);
			}
		} if (name.equals(XML_NODE_RELATION)) {
			parseRelation();
		} else if (name.equals(XML_NODE_NODE)) {
			parseNode();
		}
	}	

	private void parseRelation() {
		Relation relation = new Relation();
		relation.setId(Long.parseLong(currentAttributes.getValue(XML_ATTRIBUTE_ID)));
		relation.setTags(tagList);
		relation.setMember(membersList);
		
		visitor.visit(relation);
	}

	private void parseWay() throws Osm2xpBusinessException, DataSinkException {
		Way way = new Way();
		way.setId(Long.parseLong(currentAttributes.getValue(XML_ATTRIBUTE_ID)));
		way.getTags().addAll(tagList);
		way.getNd().addAll(ndList);

		visitor.visit(way);

	}

	private void parseNode() {
		Node node = new Node();
		node.setId(Long.parseLong(currentAttributes
				.getValue(XML_ATTRIBUTE_ID)));
		node.setLat(Double.parseDouble(currentAttributes
				.getValue(XML_ATTRIBUTE_LATITUDE)));
		node.setLon(Double.parseDouble(currentAttributes
				.getValue(XML_ATTRIBUTE_LONGITUDE)));
		node.getTags().addAll(tagList);
		visitor.visit(node);
	}

	public void process() {
		try {
			this.parseDocument();
		} catch (Osm2xpBusinessException e) {
			Osm2xpLogger.log(new OsmParsingException("Osm parser error on line "
					+ locator.getLineNumber() + " col "
					+ locator.getColumnNumber(), e));
		} catch (SAXException e) {
			Osm2xpLogger.log(new OsmParsingException("Sax parser initialization error.", e));
		}

	}

	public void complete() {
//		if (GuiOptionsHelper.getOptions().isSinglePass()
//				&& !nodesRefCollectionDone) {
//			nodesRefCollectionDone = true;
//			Osm2xpLogger.info("First pass done");
//			process();
//		} else {
//			visitor.complete();
//		}
		visitor.complete();
	}

	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
	}

	@Override
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
			throws SAXException {
	}

	@Override
	public void processingInstruction(String arg0, String arg1)
			throws SAXException {
	}

	@Override
	public void skippedEntity(String arg0) throws SAXException {
	}

	/**
	 * inner class to store xml attributes
	 */
	private class OsmAttributes {

		private HashMap<String, String> values = new HashMap<String, String>();

		public OsmAttributes(Attributes attributs) {
			for (int i = 0; i < attributs.getLength(); i++) {
				values.put(attributs.getLocalName(i), attributs.getValue(i));

			}
		}

		public String getValue(String key) {
			return values.get(key);
		}
	}

	@Override
	public IOSMDataVisitor getVisitor() {
		return visitor;
	}
}
