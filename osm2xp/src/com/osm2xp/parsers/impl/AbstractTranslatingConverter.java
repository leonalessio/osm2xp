package com.osm2xp.parsers.impl;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import org.openstreetmap.osmosis.osmbinary.Osmformat.HeaderBBox;

import com.osm2xp.dataProcessors.IDataSink;
import com.osm2xp.model.osm.Node;
import com.osm2xp.model.osm.Tag;
import com.osm2xp.translators.IBasicTranslator;

public abstract class AbstractTranslatingConverter extends AbstractOSMDataConverter {

	protected IBasicTranslator translator;

	public AbstractTranslatingConverter(IBasicTranslator translator, IDataSink processor, Map<Long, Color> roofsColorMap) {
		super(processor, roofsColorMap);
		this.translator = translator;
	}

	@Override
	public void visit(HeaderBBox box) {
		translator.processBoundingBox(box);
	}

	@Override
	public void complete() {
		translator.complete();
	}

	@Override
	protected boolean mustStoreNode(Node node) {
		return translator.mustStoreNode(node);
	}

	@Override
	protected boolean mustProcessPolyline(List<Tag> tagsModel) {
		return translator.mustProcessPolyline(tagsModel);
	}


}
