package com.osm2xp.translators.xplane;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.preferences.InstanceScope;

import com.osm2xp.gui.Activator;
import com.osm2xp.model.facades.FacadeSetManager;
import com.osm2xp.translators.AbstractTranslatorProvider;
import com.osm2xp.translators.ISpecificTranslator;
import com.osm2xp.translators.ITranslator;
import com.osm2xp.translators.impl.ImageDebugTranslationListener;
import com.osm2xp.translators.impl.XPlaneTranslatorImpl;
import com.osm2xp.utils.DsfObjectsProvider;
import com.osm2xp.utils.DsfUtils;
import com.osm2xp.utils.helpers.FacadeSetHelper;
import com.osm2xp.utils.helpers.XplaneOptionsHelper;
import com.osm2xp.writers.IHeaderedWriter;
import com.osm2xp.writers.impl.DsfWriterImpl;

import math.geom2d.Point2D;

public abstract class XPlaneTranslatorProvider extends AbstractTranslatorProvider {

	protected String facadeSetsStr;
	protected DsfObjectsProvider dsfObjectsProvider;

	public XPlaneTranslatorProvider(File binaryFile, String folderPath) {
		super(binaryFile, folderPath);
		facadeSetsStr = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID).get(FacadeSetManager.FACADE_SETS_PROP,FacadeSetHelper.getDefaultFacadePath());
		dsfObjectsProvider = new DsfObjectsProvider(folderPath, FacadeSetManager.getManager(facadeSetsStr, new File(folderPath)));		
	}

	@Override
	public Collection<ISpecificTranslator> createAdditinalAdapters() {
		return Collections.emptyList();
	}

	@Override
	public ITranslator getTranslator(Point2D currentTile) {
		// create stats folder
//				if (XplaneOptionsHelper.getOptions().isGeneratePdfStats()
//						|| XplaneOptionsHelper.getOptions().isGenerateXmlStats()) {
//					new File(folderPath + File.separatorChar + "stats").mkdirs();
//				}

		// write the libraty file if needed
		if (!XplaneOptionsHelper.getOptions().isPackageFacades()) {
			DsfUtils.writeLibraryFile(folderPath, dsfObjectsProvider);
		}
		
		IHeaderedWriter writer = new DsfWriterImpl(folderPath, currentTile);
		XPlaneTranslatorImpl translatorImpl = createTranslator(currentTile, writer);
		if (XplaneOptionsHelper.getOptions().isGenerateDebugImg()) {
			translatorImpl.setTranslationListener(new ImageDebugTranslationListener());
		}
		return translatorImpl;
	}

	protected abstract XPlaneTranslatorImpl createTranslator(Point2D currentTile, IHeaderedWriter writer);

}
