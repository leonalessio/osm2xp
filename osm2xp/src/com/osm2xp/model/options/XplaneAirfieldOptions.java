package com.osm2xp.model.options;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="XplaneAirfieldOptions", propOrder = {"generateAirfields","useSingleAptAsMain","generateApron","flatten",
		"tryGetElev","defaultRunwayWidth","defaultTaxiwayWidth", "defaultHelipadSize", "ignoredAirfields"})
public class XplaneAirfieldOptions {
	protected boolean generateAirfields = false;
	protected boolean useSingleAptAsMain = true;
	protected boolean generateApron = true;
	protected boolean flatten = true;
	protected boolean tryGetElev = true;
	protected int defaultRunwayWidth = 60;
	protected int defaultTaxiwayWidth = 40;
	protected int defaultHelipadSize= 10;
	protected List<String> ignoredAirfields;
	
	public boolean isGenerateAirfields() {
		return generateAirfields;
	}
	public void setGenerateAirfields(boolean generateAirfields) {
		this.generateAirfields = generateAirfields;
	}
	public boolean isUseSingleAptAsMain() {
		return useSingleAptAsMain;
	}
	public void setUseSingleAptAsMain(boolean useSingleAptAsMain) {
		this.useSingleAptAsMain = useSingleAptAsMain;
	}
	public boolean isFlatten() {
		return flatten;
	}
	public void setFlatten(boolean flatten) {
		this.flatten = flatten;
	}
	public boolean isTryGetElev() {
		return tryGetElev;
	}
	public void setTryGetElev(boolean tryGetElev) {
		this.tryGetElev = tryGetElev;
	}
	public int getDefaultRunwayWidth() {
		return defaultRunwayWidth;
	}
	public void setDefaultRunwayWidth(int defaultRunwayWidth) {
		this.defaultRunwayWidth = defaultRunwayWidth;
	}
	public int getDefaultTaxiwayWidth() {
		return defaultTaxiwayWidth;
	}
	public void setDefaultTaxiwayWidth(int defaultTaxiwayWidth) {
		this.defaultTaxiwayWidth = defaultTaxiwayWidth;
	}
	public boolean isGenerateApron() {
		return generateApron;
	}
	public void setGenerateApron(boolean generateApron) {
		this.generateApron = generateApron;
	}
	public List<String> getIgnoredAirfields() {
		if (ignoredAirfields == null) {
			ignoredAirfields = new ArrayList<>();
		}
		return ignoredAirfields;
	}
	public void setIgnoredAirfields(List<String> ignoredAirfields) {
		this.ignoredAirfields = ignoredAirfields;
	}
	public int getDefaultHelipadSize() {
		return defaultHelipadSize;
	}
	public void setDefaultHelipadSize(int defaultHelipadSize) {
		this.defaultHelipadSize = defaultHelipadSize;
	}

}
