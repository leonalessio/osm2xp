package com.osm2xp.generation.options;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.osm2xp.generation.options.rules.XplaneLightTagRule;

/**
 * LightsRulesList.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LightsRulesList", propOrder = { "rules" })
public class LightsRulesList {

	@XmlElement(required = true)
	protected List<XplaneLightTagRule> rules;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public LightsRulesList() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public LightsRulesList(final List<XplaneLightTagRule> rules) {
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
	 * {@link XplaneLightTagRule }
	 * 
	 * 
	 */
	public List<XplaneLightTagRule> getRules() {
		if (rules == null) {
			rules = new ArrayList<XplaneLightTagRule>();
		}
		return this.rules;
	}

}
