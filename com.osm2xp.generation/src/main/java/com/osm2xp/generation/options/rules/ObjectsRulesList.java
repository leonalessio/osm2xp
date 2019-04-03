package com.osm2xp.generation.options.rules;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * ObjectsRulesList.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObjectsRulesList", propOrder = { "rules" })
public class ObjectsRulesList {

	@XmlElement(required = true)
	protected List<ObjectTagRule> rules;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public ObjectsRulesList() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public ObjectsRulesList(final List<ObjectTagRule> rules) {
		this.rules = rules;
	}

	/**
	 * Gets the value of the rules property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the rules property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getRules().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link ObjectTagRule }
	 * 
	 * 
	 */
	public List<ObjectTagRule> getRules() {
		if (rules == null) {
			rules = new ArrayList<ObjectTagRule>();
		}
		return this.rules;
	}

}
