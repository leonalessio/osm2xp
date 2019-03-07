package com.osm2xp.controllers;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.osm2xp.constants.Perspectives;
import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.generation.options.GlobalOptionsProvider;
import com.osm2xp.gui.Activator;
import com.osm2xp.gui.perspectives.Xplane10ConfigurationPerspective;
import com.osm2xp.gui.views.MainSceneryFileView;
import com.osm2xp.jobs.GenerateMultiTilesJob;
import com.osm2xp.jobs.MutexRule;
import com.osm2xp.model.facades.FacadeSetManager;
import com.osm2xp.stats.StatsProvider;
import com.osm2xp.utils.FilesUtils;
import com.osm2xp.utils.helpers.GuiOptionsHelper;
import com.osm2xp.utils.helpers.Osm2xpProjectHelper;
import com.osm2xp.utils.ui.UiUtil;

/**
 * Build controller.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class BuildController {
	
	private static String mode = Xplane10ConfigurationPerspective.MODE;
	
	private String folderPath;
	private MutexRule rule = new MutexRule();

	/**
	 * @throws Osm2xpBusinessException
	 */
	public void launchBuild() throws Osm2xpBusinessException {
		File currentFile = getSelectedFile();
		if (currentFile == null) {
			return;
		}
		// if choosen output mode will generate file, first check that user is
		// ok to overwrite file is present.
		if (GuiOptionsHelper.isOutputFormatAFileGenerator(mode)) {
			folderPath = currentFile.getParent() + File.separator
					+ GuiOptionsHelper.getSceneName();

			if (checkDeleteFolder(new File(folderPath))) {
				startGeneration(currentFile);
			}
		} else {
			startGeneration(currentFile);
		}
	}

	public static File getSelectedFile() {
		String currentFilePath = GlobalOptionsProvider.getOptions()
				.getCurrentFilePath();
		String path = StringUtils.stripToEmpty(currentFilePath).trim();
		if (path.isEmpty()) {
			MessageDialog.openError(Display.getDefault().getActiveShell(),"No file specified", "No file with OSM data (*.pbf, *.osm...) specified. Please choose valid file");
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(MainSceneryFileView.ID);
			} catch (PartInitException e) {
				Activator.log(e);
			}
			return null;
		}
		File currentFile = new File(path);
		if (!currentFile.isFile()) {
			MessageDialog.openError(Display.getDefault().getActiveShell(),"Invalid file specified", "Can't open OSM data file " + path + ". Please check this file exists");
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(MainSceneryFileView.ID);
			} catch (PartInitException e) {
				Activator.log(e);
			}
			return null;
		}
		return currentFile;
	}

	/**
	 * @throws Osm2xpBusinessException
	 */
//	public void restartImportedProject() throws Osm2xpBusinessException {
//
//		// switch to build perspective
//		UiUtil.switchPerspective(Perspectives.PERSPECTIVE_BUILD);
//		GlobalOptionsProvider.getOptions().setCurrentFilePath(
//				Osm2xpProjectHelper.getOsm2XpProject().getFile());
//		File currentFile = new File(Osm2xpProjectHelper.getOsm2XpProject()
//				.getFile());
//		this.folderPath = Osm2xpProjectHelper.getProjectFile().getParent();
//
//		for (Coordinates coordinates : Osm2xpProjectHelper.getOsm2XpProject()
//				.getCoordinatesList().getCoordinates()) {
//			Point2D tuile = new Point2D(coordinates.getLongitude(),
//					coordinates.getLatitude());
//			try {
//				generateSingleTile(currentFile, tuile, folderPath);
//			} catch (Osm2xpBusinessException e) {
//				Osm2xpLogger.error("Error generating tile", e);
//			}
//		}
//
//	}
	
	public static boolean checkDeleteFolder(File currentFolder) {
		if (!currentFolder.exists()) {
			return true;
		}
		if (MessageDialog.openConfirm(Display.getDefault()
						.getActiveShell(), "Confirm", currentFolder.getAbsolutePath()
						+ " already exists, delete?")) {
			FilesUtils.deleteDirectory(currentFolder);
			return true;
		}
		return false;
	}

	/**
	 * @param currentFile
	 * @throws Osm2xpBusinessException
	 */
	private void startGeneration(File currentFile)
			throws Osm2xpBusinessException {
		// delete existing file if exists
		if (GuiOptionsHelper.isOutputFormatAFileGenerator(mode)
				&& new File(folderPath).exists()) {
			FilesUtils.deleteDirectory(new File(folderPath));
		}
		StatsProvider.reinit();
		// switch to build perspective
		UiUtil.switchPerspective(Perspectives.PERSPECTIVE_BUILD);
		// get user setted cordinates
		FacadeSetManager.clearCache();
		generateWholeFile(currentFile, folderPath);

//		new ParsingExperimentJob(currentFile).schedule(); //Experimental to check new osmosis API
	}


	/**
	 * @param currentFile
	 * @param folderPath
	 */
	private void generateWholeFile(final File currentFile,
			final String folderPath) {
		Job tilesJob = new Job("Listing tiles ") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
//				final TilesLister tilesLister = TilesListerFactory
//						.getTilesLister(currentFile);
//				Osm2xpLogger.info("Listing relations in file " + currentFile.getName());
//				Osm2xpLogger.info("Listing tiles in file " + currentFile.getName());
//				try {
//					tilesLister.process();
//				} catch (Osm2xpBusinessException e) {
//					Osm2xpLogger.error(
//							"Error listing tiles :\n" + e.getMessage(), e);
//				}
//
//				// If you want to update the UI
//				Display.getDefault().asyncExec(new Runnable() {
//					@Override
//					public void run() {
//					}
//				});

				// get tiles list
//				List<Point2D> tilesList = new ArrayList<Point2D>(
//						tilesLister.getTilesList());
//				Osm2xpLogger.info("listing of tiles complete");
//				Osm2xpLogger.info(tilesList.size() + " tile(s) found");
				// init the current project, only if the output mode will
				// generate files
				if (GuiOptionsHelper.isOutputFormatAFileGenerator(mode)) {
					try {
						Osm2xpProjectHelper.initProject(folderPath, GlobalOptionsProvider.getOptions()
								.getCurrentFilePath());
					} catch (Osm2xpBusinessException e1) {
						Osm2xpLogger.error("Error creating project file", e1);
					}
				}
				GenerateMultiTilesJob tilesJob = new GenerateMultiTilesJob("Generate several tiles", BuildController.getGenerationMode(), currentFile, folderPath, "todoJob");
				tilesJob.setRule(new MutexRule());
				tilesJob.addJobChangeListener(new JobChangeAdapter() {


					@Override
					public void done(IJobChangeEvent event) {
						tilesJob.setFamily("endedJob");
						Display.getDefault().asyncExec(new Runnable() {
							@Override
							public void run() {
								if ((Job.getJobManager().find("todoJob")).length == 0) {
									
									Osm2xpLogger.info("Generation finished.");
									
									// MiscUtils.switchPerspective(GuiOptionsHelper
									// .getOptions().getOutputFormat());

								}
							}
						});

					}
					
				});

				tilesJob.setRule(rule);
				tilesJob.schedule();
				
//				if (tilesList.isEmpty()) {
//					try {
//						GlobalOptionsProvider.getOptions().setSinglePass(true);
//						generateWholeFileOnASinglePass(currentFile, folderPath);
//					} catch (Osm2xpBusinessException e) {
//						Osm2xpLogger.error("Error generating tile", e);
//						return new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
//					}
//				}
				return Status.OK_STATUS;

			}
		};

		tilesJob.schedule();

	}

	public static String getGenerationMode() {
		return mode;
	}

	public static void setGenerationMode(String mode) {
		BuildController.mode = mode;
	}
}
