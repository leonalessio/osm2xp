package com.osm2xp.model.options;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="XplaneAirfieldOptions", propOrder = {"generateAirfields","useSingleAptAsMain","generateApron","generateMarks","flatten","preferEnglish",
		"tryGetElev","tryGetName","defaultHardRunwayWidth","defaultGrassRunwayWidth","defaultHardTaxiwayWidth", "defaultGrassTaxiwayWidth", "defaultHelipadSize", "ignoredAirfields"})
public class XplaneAirfieldOptions {
	protected boolean generateAirfields = false;
	protected boolean useSingleAptAsMain = true;
	protected boolean generateApron = true;
	protected boolean generateMarks = true;
	protected boolean flatten = true;
	protected boolean preferEnglish = true;
	protected boolean tryGetElev = true;
	protected boolean tryGetName = true;
	protected int defaultHardRunwayWidth = 60;
	protected int defaultGrassRunwayWidth = 40;
	protected int defaultHardTaxiwayWidth = 20;
	protected int defaultGrassTaxiwayWidth = 10;
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
	public int getDefaultHardRunwayWidth() {
		return defaultHardRunwayWidth;
	}
	public void setDefaultHardRunwayWidth(int defaultHardRunwayWidth) {
		this.defaultHardRunwayWidth = defaultHardRunwayWidth;
	}
	public int getDefaultHardTaxiwayWidth() {
		return defaultHardTaxiwayWidth;
	}
	public void setDefaultHardTaxiwayWidth(int defaultHardTaxiwayWidth) {
		this.defaultHardTaxiwayWidth = defaultHardTaxiwayWidth;
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
	public boolean isTryGetName() {
		return tryGetName;
	}
	public void setTryGetName(boolean tryGetName) {
		this.tryGetName = tryGetName;
	}
	public boolean isGenerateMarks() {
		return generateMarks;
	}
	public void setGenerateMarks(boolean generateMarks) {
		this.generateMarks = generateMarks;
	}
	public int getDefaultGrassTaxiwayWidth() {
		return defaultGrassTaxiwayWidth;
	}
	public void setDefaultGrassTaxiwayWidth(int defaultGrassTaxiwayWidth) {
		this.defaultGrassTaxiwayWidth = defaultGrassTaxiwayWidth;
	}
	public int getDefaultGrassRunwayWidth() {
		return defaultGrassRunwayWidth;
	}
	public void setDefaultGrassRunwayWidth(int defaultGrassRunwayWidth) {
		this.defaultGrassRunwayWidth = defaultGrassRunwayWidth;
	}
	public final boolean isPreferEnglish() {
		return preferEnglish;
	}
	public final void setPreferEnglish(boolean preferEnglish) {
		this.preferEnglish = preferEnglish;
	}

}
