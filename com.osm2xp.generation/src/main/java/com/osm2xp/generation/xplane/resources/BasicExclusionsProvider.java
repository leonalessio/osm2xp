package com.osm2xp.generation.xplane.resources;

import com.osm2xp.generation.options.XPlaneOptionsProvider;

public class BasicExclusionsProvider implements IExclusionsProvider {
	@Override
	public boolean isExcludeObj() {
		return XPlaneOptionsProvider.getOptions().isExcludeObj() || (XPlaneOptionsProvider.getOptions().isAutoExclude() && objectsGenerated());
	}

	@Override
	public boolean isExcludeFac() {
		return XPlaneOptionsProvider.getOptions().isExcludeFac() || (XPlaneOptionsProvider.getOptions().isAutoExclude() && facadesGenerated());
	}

	@Override
	public boolean isExcludeFor() {
		return XPlaneOptionsProvider.getOptions().isExcludeFor() || (XPlaneOptionsProvider.getOptions().isAutoExclude() && forestsGenerated());
	}

	@Override
	public boolean isExcludeNet() {
		return XPlaneOptionsProvider.getOptions().isExcludeNet() || (XPlaneOptionsProvider.getOptions().isAutoExclude() && networkGenerated());
	}

	@Override
	public boolean isExcludeLin() {
		return XPlaneOptionsProvider.getOptions().isExcludeLin();
	}

	@Override
	public boolean isExcludePol() {
		return XPlaneOptionsProvider.getOptions().isExcludePol() || (XPlaneOptionsProvider.getOptions().isAutoExclude() && polysGenerated());
	}

	@Override
	public boolean isExcludeStr() {
		return XPlaneOptionsProvider.getOptions().isExcludeStr();
	}

	@Override
	public boolean objectsGenerated() {
		return XPlaneOptionsProvider.getOptions().isGenerateObj()
				|| XPlaneOptionsProvider.getOptions().isGenerateObjBuildings()
				|| XPlaneOptionsProvider.getOptions().isGenerateChimneys()
				|| XPlaneOptionsProvider.getOptions().isGenerateCoolingTowers();
	}

	@Override
	public boolean facadesGenerated() {
		return XPlaneOptionsProvider.getOptions().isGenerateBuildings()
				|| XPlaneOptionsProvider.getOptions().isGenerateFence()
				|| XPlaneOptionsProvider.getOptions().isGenerateTanks();
	}

	@Override
	public boolean forestsGenerated() {
		return XPlaneOptionsProvider.getOptions().isGenerateFor();
	}

	@Override
	public boolean networkGenerated() {
		return XPlaneOptionsProvider.getOptions().isGenerateRoads()
				|| XPlaneOptionsProvider.getOptions().isGenerateRailways()
				|| XPlaneOptionsProvider.getOptions().isGeneratePowerlines();
	}

	@Override
	public boolean polysGenerated() {
		return XPlaneOptionsProvider.getOptions().isGeneratePolys();
	}

	@Override
	public boolean isExcludeBch() {
		return XPlaneOptionsProvider.getOptions().isExcludeBch();
	}
	
}
