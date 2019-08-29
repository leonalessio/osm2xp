package com.osm2xp.converters.impl;

import java.util.List;

import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.datastore.IDataSink;
import com.osm2xp.translators.IBasicTranslator;

import math.geom2d.Box2D;

public abstract class AbstractTranslatingConverter extends AbstractOSMDataConverter {

	protected IBasicTranslator translator;

	public AbstractTranslatingConverter(IBasicTranslator translator, IDataSink processor) {
		super(processor);
		this.translator = translator;
	}

	@Override
	public void visit(Box2D box) {
		translator.processBoundingBox(box);
	}

	@Override
	public void complete() {
		translator.complete();
	}

	@Override
	protected boolean mustStoreNode(Node node) {
		return !dataSink.isCompleted() && translator.mustStoreNode(node);
	}

	@Override
	protected boolean mustProcessPolyline(List<Tag> tagsModel) {
		return translator.mustProcessPolyline(tagsModel);
	}


}
