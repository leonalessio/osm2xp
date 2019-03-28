package com.osm2xp.utils.helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.prefs.BackingStoreException;

import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.options.GlobalOptionsProvider;
import com.osm2xp.gui.Activator;
import com.osm2xp.translators.TranslatorBuilder;

import math.geom2d.Point2D;

/**
 * GuiOptionsHelper.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class GuiOptionsHelper {

	public static final String SCENE_NAME = "sceneName";
	public static final String USED_FILES = "usedFiles";
	public static final String USE_EXCLUSIONS_FROM_PBF = "useExclusionsFromPBF";
	
	public static final String ALLOWED_HIGHWAY_TYPES = "allowedHighwayTypes";
	public static final String ALLOWED_HIGHWAY_LINK_TYPES = "allowedHighwayLinkTypes";
	public static final String ALLOWED_HIGHWAY_SURFACE_TYPES = "allowedHighwaySurfaceTypes";
	
	private static Tag shapefileTag;
	private static File roofColorFile;
	private static List<String> lastFiles;

	private static Point2D selectedCoordinates;
	private static List<Consumer<String>> selectedFileListeners  = new ArrayList<>();



	/**
	 * return true if the choosen output mode is generating files
	 * @param mode 
	 * 
	 * @return boolean
	 */
	public static boolean isOutputFormatAFileGenerator(String mode) {
		return TranslatorBuilder.isFileWriting(mode);
	}
	
	public static void setSceneName(String sceneName) {
		putProperty(SCENE_NAME, sceneName);
	}
	
	private static String getStringProperty(String property) {
		return getStringProperty(property, "");
	}
	
	private static String getStringProperty(String property, String defaultVal) {
		return InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID).get(property, defaultVal);
	}
	
	private static boolean getBooleanProperty(String property, boolean defaultVal) {
		return InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID).getBoolean(property, defaultVal);
	}
	
	private static void putProperty(String property, String value) {
		IEclipsePreferences node = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		node.put(property, value);
		try {
			node.flush();
		} catch (BackingStoreException e) {
			Activator.log(e);
		}
	}

	public static String getSceneName() {
		return getStringProperty(SCENE_NAME);
	}

	public static void addUsedFile(String fileName) {
		checkGetLastFiles();
		if (lastFiles.indexOf(fileName) == -1) {
			lastFiles.add(fileName);
		}
		putProperty(USED_FILES, lastFiles.stream().collect(Collectors.joining(File.pathSeparator)));
	}

	private static synchronized void checkGetLastFiles() {
		if (lastFiles == null) {
			lastFiles = new ArrayList<>();
			String filesStr = getStringProperty(USED_FILES);
			if (filesStr.length() > 0) {
				lastFiles.addAll(Arrays.asList(filesStr.split(File.pathSeparator)).stream().filter(str -> new File(str).isFile()).collect(Collectors.toList()));
			}
		}
	}

	public static void askShapeFileNature(Shell shell) {
		MessageDialog messageDialog = new MessageDialog(shell,
				"shapefile nature", null, "What is the nature of "
						+ new File(GlobalOptionsProvider.getOptions().getCurrentFilePath()).getName()
						+ "?", MessageDialog.QUESTION, new String[] {
						"Buildings", "Forests" }, 1);
		if (messageDialog.open() == 0) {
			shapefileTag = new Tag("yes", "building");
		} else {
			shapefileTag = new Tag("forest", "landuse");
		}

	}

	public static Tag getShapefileTag() {
		return shapefileTag;
	}

	public static void setShapefileTag(Tag shapefileTag) {
		GuiOptionsHelper.shapefileTag = shapefileTag;
	}

	public static File getRoofColorFile() {
		return roofColorFile;
	}

	public static void setRoofColorFile(File roofColorFile) {
		GuiOptionsHelper.roofColorFile = roofColorFile;
	}

	public static void setSelectedCoordinates(Point2D selectedCoordinates) {
		GuiOptionsHelper.selectedCoordinates = selectedCoordinates;
	}

	public static Point2D getSelectedCoordinates() {
		return selectedCoordinates;
	}
	
	/**
	 * Gets the value of the lastFiles property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the lastFiles property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getLastFiles().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link String }
	 * 
	 * 
	 */
	public static List<String> getLastFiles() {
		checkGetLastFiles();
		return lastFiles;
	}
	
	public static String[] getAllowedHighwayTypes() {
		String str = getStringProperty(ALLOWED_HIGHWAY_TYPES, "motorway;trunk;primary;secondary;tertiary;unclassified;residential");
		return str.split(";");
	}
	
	public static void setAllowedHighwayTypes(String[] types) {
		putProperty(ALLOWED_HIGHWAY_TYPES, Arrays.asList(types).stream().collect(Collectors.joining(";")));
	}
	
	public static String[] getAllowedHighwayLinkTypes() {
		String str = getStringProperty(ALLOWED_HIGHWAY_LINK_TYPES, "motorway_link;trunk_link;primary_link;secondary_link;tertiary_link");
		return str.split(";");
	}
	
	public static void setAllowedHighwayLinkTypes(String[] types) {
		putProperty(ALLOWED_HIGHWAY_LINK_TYPES, Arrays.asList(types).stream().collect(Collectors.joining(";")));
	}
	
	public static String[] getAllowedHighwaySurfaceTypes() {
		String str = getStringProperty(ALLOWED_HIGHWAY_SURFACE_TYPES, "paved;asphalt;concrete");
		return str.split(";");
	}
	
	public static void setAllowedHighwaySurfaceTypes(String[] types) {
		putProperty(ALLOWED_HIGHWAY_SURFACE_TYPES, Arrays.asList(types).stream().collect(Collectors.joining(";")));
	}

	public static boolean isUseExclusionsFromPBF() {
		return getBooleanProperty(USE_EXCLUSIONS_FROM_PBF, true);
	}
	
	public static void setUseExclusionsFromPBF(boolean useExclusionsFromPBF) {
		putProperty(USE_EXCLUSIONS_FROM_PBF, String.valueOf(useExclusionsFromPBF));
	}

	public static void setCurrentFilePath(String fileName) {
		GlobalOptionsProvider.getOptions().setCurrentFilePath(fileName);
		for (Consumer<String> consumer: selectedFileListeners) {
			consumer.accept(fileName);
		}
	}
	
	public String getCurrentFilePath() {
		return GlobalOptionsProvider.getOptions().getCurrentFilePath();
	}
	
	public static void addInputFileListener(Consumer<String> listener) {
		selectedFileListeners.add(listener);
	}
	
	public static void removeInputFileListener(Consumer<String> listener) {
		selectedFileListeners.remove(listener);
	}
	
}
