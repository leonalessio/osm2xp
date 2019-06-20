package com.onpositive.classification.core.buildings;

public enum OSMBuildingType {
	BLOCK, //Living block
	INDUSTRIAL,
	SOCIAL, //School, hospital, university...
	OFFICE,	
	SHOP, //Tag shop=* (except mall)
	MALL, //Tag shop=mall or shop=department_store
	HOUSE, //house,detached..
	GARAGE
}
