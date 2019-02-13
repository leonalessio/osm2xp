package com.osm2xp.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.InstanceScope;

import com.osm2xp.constants.Osm2xpConstants;
import com.osm2xp.constants.Perspectives;
import com.osm2xp.constants.XplaneConstants;
import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.exceptions.Osm2xpTechnicalException;
import com.osm2xp.gui.Activator;
import com.osm2xp.jobs.DsfConversionJob;
import com.osm2xp.model.facades.FacadeSetManager;
import com.osm2xp.model.xplane.XplaneDsfObject;
import com.osm2xp.utils.geometry.GeomUtils;
import com.osm2xp.generation.options.GlobalOptionsProvider;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.utils.ui.MiscUtils;
import com.osm2xp.writers.impl.OsmWriterImpl;

import math.geom2d.Box2D;
import math.geom2d.Point2D;

/**
 * DsfUtils.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class DsfUtils {

	private static final String DEFAULT_FACADE_LOD = "LOD 0.000000 25000.000000";

	public static void xplaneDsfObjectToOsm(XplaneDsfObject dsfObject) {
		OsmWriterImpl writer = new OsmWriterImpl("c:\\debug\\"
				+ new GregorianCalendar().getTimeInMillis() + "\\");
		writer.init(GeomUtils.cleanCoordinatePoint(dsfObject.getOsmPolygon()
				.getPolygon().firstPoint()));
		int cpt = 1;
		for (Point2D point : dsfObject.getOsmPolygon().getPolygon()
				.vertices()) {

			writer.write("<node id=\"" + cpt++ + "\" lat=\"" + point.y()
					+ "\" lon=\"" + point.x() + "\" version=\"1\" />\n");
		}

		writer.write("<way id=\"" + cpt++
				+ "\" visible=\"true\" version=\"2\" >\n");
		writer.write("<tag k=\"building\" v=\"yes\"/>\n");

		cpt = 1;
		for (Point2D point : dsfObject.getOsmPolygon().getPolygon()
				.vertices()) {

			writer.write("<nd ref=\"" + cpt++ + "\"/>\n");
		}
		writer.write("</way>\n");

		// write origin
		Point2D origin = null;// GeomUtils.getRotationPoint(dsfObject.getOsmPolygon().getPolygon(),50,0);

//		writer.write("<node id=\"" + cpt++ + "\" lat=\"" + origin.y() //TODO
//				+ "\" lon=\"" + origin.x() + "\" version=\"1\" >\n");
//		writer.write("<tag k=\"man_made\" v=\"water_tower\"/>\n");
//		writer.write("</node>\n");

		writer.complete();
	}

	/**
	 * get the right path to the dsfTool executable
	 * 
	 * @return String the path to the dsfTool executable
	 */
	public static String getDsfToolPath() {
		String dsfTool;
		if (MiscUtils.isWindows()) {
			dsfTool = Osm2xpConstants.UTILS_PATH + File.separatorChar
					+ "DSFTool.exe";
		} else if (MiscUtils.isMac()) {
			dsfTool = Osm2xpConstants.UTILS_PATH + File.separatorChar
					+ "DSFToolMac";
		} else {
			dsfTool = Osm2xpConstants.UTILS_PATH + File.separatorChar
					+ "DSFToolLinux";
		}
		return dsfTool;
	}

	public static void applyFacadeLod(File to) throws FileNotFoundException,
			IOException {

		File[] filesList = to.listFiles();
		if (filesList != null) {
			for (int cpt = 0; cpt < filesList.length; cpt++) {
				if (filesList[cpt].getName().toLowerCase().contains(".fac")) {
					File facade = filesList[cpt];
					String line = null;
					String facadeContent = new String();
					try (RandomAccessFile reader = new RandomAccessFile(facade, "rw");FileWriter writer = new FileWriter(facade.getAbsolutePath());) {
						
						while ((line = reader.readLine()) != null) {
							facadeContent += line + "\r\n";
						}
						facadeContent = facadeContent.replaceAll(
								DEFAULT_FACADE_LOD, "LOD 0.000000 "
										+ XPlaneOptionsProvider.getOptions()
										.getFacadeLod() + ".000000");
						
						writer.write(facadeContent);
						writer.close();
					} catch (IOException e) {
						throw e;
					}

				}
			}
		}
	}

	/**
	 * @param to
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void removeConcreteRoofsAndWalls(File to)
			throws FileNotFoundException, IOException {

		File[] filesList = to.listFiles();
		if (filesList != null) {
			for (int cpt = 0; cpt < filesList.length; cpt++) {
				if (filesList[cpt].getName().toLowerCase().contains(".fac")) {
					File facade = filesList[cpt];
					String line = null;
					String facadeContent = new String();
					try (RandomAccessFile reader = new RandomAccessFile(facade, "rw")) {
						while ((line = reader.readLine()) != null) {
							facadeContent += line + "\r\n";
						}
						facadeContent = facadeContent.replaceAll(
								"HARD_ROOF concrete", "");
						facadeContent = facadeContent.replaceAll(
								"HARD_WALL concrete", "");
						try (FileWriter writer = new FileWriter(facade.getAbsolutePath())) {
							writer.write(facadeContent);
						}
					}

				}
			}
		}
	}

	/**
	 * return the dsf file for given coordinates and scene folder
	 * 
	 * @param sceneFolder
	 * @param coordinates
	 * @return File
	 */
	public static File computeXPlaneDsfFilePath(String sceneFolder,
			Point2D coordinates) {
		// compute folder and file name , following xplane specifications
		String[] folderAndFileNames = DsfUtils
				.getFolderAndFileNames(coordinates);
		String nomFichier = folderAndFileNames[1];
		String nomRepertoire = folderAndFileNames[0];
		String repertoireDsf = sceneFolder + File.separatorChar
				+ "Earth nav data" + File.separatorChar + nomRepertoire;
		String nomFichierDsf = repertoireDsf + File.separatorChar + nomFichier
				+ ".dsf.txt";
		File dsfFile = new File(nomFichierDsf);
		return dsfFile;
	}

	/**
	 * @param sceneFolder
	 * @param dsfObjectsProvider
	 * @throws Osm2xpBusinessException
	 */
	public static void writeLibraryFile(String sceneFolder,
			DsfObjectsProvider dsfObjectsProvider) {
		try (BufferedWriter outputlibrary = new BufferedWriter(new FileWriter(sceneFolder
				+ File.separatorChar + "library.txt", false))) {
			outputlibrary.write("I" + "\n");
			outputlibrary.write("800" + "\n");
			outputlibrary.write("LIBRARY" + "\n");
			for (String facade : dsfObjectsProvider.getFacadesList()) {
				outputlibrary.write("EXPORT \\lib\\osm2xp\\facades\\" + facade
						+ " .." + File.separator + "osm2xpFacades"
						+ File.separator
						+ InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID).get(FacadeSetManager.FACADE_SETS_PROP,"")
						+ File.separator + facade + "\n");

			}
			outputlibrary.flush();
		} catch (IOException e) {
			throw new Osm2xpTechnicalException(e);
		}
	}

	/**
	 * Copy facades files from workspace to scene folder. Also apply lod and
	 * hard wall settings
	 * 
	 * @throws Osm2xpBusinessException
	 */
//	public static void copyFacadeSet(String sceneFolder) {
//		if (XPlaneOptionsProvider.getOptions().isPackageFacades()
//				&& !new File(sceneFolder + File.separatorChar + "facades")
//						.exists()) {
//			File from = new File(Osm2xpConstants.FACADES_SETS_PATH
//					+ File.separatorChar
//					+ XPlaneOptionsProvider.getOptions().getFacadeSet());
//			File to = new File(sceneFolder + File.separatorChar + "facades");
//
//			try {
//				FilesUtils.copyDirectory(from, to);
//				applyFacadeLod(to);
//				if (!XPlaneOptionsProvider.getOptions().isHardBuildings()) {
//					DsfUtils.removeConcreteRoofsAndWalls(to);
//				}
//			} catch (FileNotFoundException e) {
//				throw new Osm2xpTechnicalException(e);
//			} catch (IOException e) {
//				throw new Osm2xpTechnicalException(e);
//			}
//
//		}
//	}

	
	/**
	 * return the correct folder and file name for the given lat/long
	 * coordinates
	 * 
	 * @param coordinates
	 * @return String array, 0 = folder name, 1 = file name
	 */
	public static String[] getFolderAndFileNames(Point2D coordinates) {
		int latitude = (int) coordinates.y();
		int longitude = (int) coordinates.x();

		Double dossierFirst = Double.valueOf(latitude);
		Double dossierEnd = Double.valueOf(longitude);
		while (dossierFirst % 10 != 0) {
			dossierFirst--;
		}
		while (dossierEnd % 10 != 0) {
			dossierEnd--;
		}

		DecimalFormat dfLat = (DecimalFormat) DecimalFormat.getNumberInstance();
		dfLat.applyPattern("00");
		DecimalFormat dfLong = (DecimalFormat) DecimalFormat
				.getNumberInstance();
		dfLong.applyPattern("000");
		StringBuffer fichier = new StringBuffer();
		StringBuffer dossier = new StringBuffer();
		if (latitude >= 0) {
			fichier.append("+");
			dossier.append("+");
		}
		fichier.append(dfLat.format(latitude));
		dossier.append(dfLat.format(dossierFirst));
		if (longitude >= 0) {
			fichier.append("+");
			dossier.append("+");
		}
		fichier.append(dfLong.format(longitude));
		dossier.append(dfLong.format(dossierEnd));
		String nomFichier = fichier.toString();
		String nomRepertoire = dossier.toString();
		String[] result = new String[] { nomRepertoire, nomFichier };
		return result;
	}
	

}
