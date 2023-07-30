package com.osm2xp.gui.views;

import java.io.File;
import java.util.function.Consumer;

import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;

import com.osm2xp.constants.Osm2xpConstants;
import com.osm2xp.generation.options.GlobalOptionsProvider;
import com.osm2xp.gui.Activator;
import com.osm2xp.utils.helpers.GuiOptionsHelper;

/**
 * LastFilesView.
 * 
 * @author Benjamin Blanchet, Dmitry Karpenko
 * 
 */
public class LastFilesView extends ViewPart {
	
	public static final String ID = "com.osm2xp.viewLastFiles";
	
	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());
	private Table lastFilesTable;
	private TableViewer lastFilesTableViewer;
	private IPreferenceChangeListener prefChangeListener = (event) -> {
		if (GuiOptionsHelper.USED_FILES.equals(event.getKey())) {
			refreshList();
		}
	};
	
	private Consumer<String> inputFileListener = (name) -> {
		selectFile(name);
	};

	public LastFilesView() {
		InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID).addPreferenceChangeListener(prefChangeListener);
	}

	@Override
	public void createPartControl(final Composite parent) {

		ScrolledForm scrldfrmLastOsmFiles = formToolkit
				.createScrolledForm(parent);
		scrldfrmLastOsmFiles.setImage(ResourceManager.getPluginImage(
				Activator.PLUGIN_ID, "images/toolbarsIcons/lastFiles_32.png"));

		formToolkit.paintBordersFor(scrldfrmLastOsmFiles);
		scrldfrmLastOsmFiles.setText("Last osm files");
		scrldfrmLastOsmFiles.getBody()
				.setLayout(new FillLayout(SWT.HORIZONTAL));

		lastFilesTableViewer = new TableViewer(scrldfrmLastOsmFiles.getBody(),
				SWT.BORDER | SWT.FULL_SELECTION);
		lastFilesTable = lastFilesTableViewer.getTable();
		lastFilesTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				ViewerCell cell = lastFilesTableViewer.getCell(new Point(e.x,e.y));
				if (cell != null && !lastFilesTableViewer.getSelection().isEmpty()) {
					IStructuredSelection selection = (IStructuredSelection) lastFilesTableViewer.getSelection();
					if (!selection.isEmpty()) {
						GlobalOptionsProvider.getOptions().setCurrentFilePath((String) selection.getFirstElement());
						if (((String) selection.getFirstElement()).toUpperCase().contains(".SHP")) {
							GuiOptionsHelper.askShapeFileNature(parent.getShell());
						}
					}
				} else {
					FileDialog dlg = new FileDialog(lastFilesTable.getShell(), SWT.OPEN);
					dlg.setFilterNames(Osm2xpConstants.OSM_FILE_FILTER_NAMES);
					dlg.setFilterExtensions(Osm2xpConstants.OSM_FILE_FILTER_EXTS);
					String fileName = dlg.open();
					if (fileName != null) {
						GuiOptionsHelper.addUsedFile(fileName);
						refreshList();
						lastFilesTableViewer.setSelection(new StructuredSelection(fileName));
					}
				}
			}
		});
		getSite().setSelectionProvider(lastFilesTableViewer);
		formToolkit.paintBordersFor(lastFilesTable);
		if (GuiOptionsHelper.getLastFiles() != null) {
			lastFilesTableViewer.setContentProvider(new ArrayContentProvider());
			lastFilesTableViewer.setLabelProvider(new LabelProvider() {
				public Image getImage(Object element) {
					if (element.toString().toUpperCase().indexOf(".PBF") != -1) {
						return ResourceManager.getPluginImage(Activator.PLUGIN_ID,
								"images/toolbarsIcons/file_16.png");
					} else {
						return ResourceManager.getPluginImage(Activator.PLUGIN_ID,
								"images/toolbarsIcons/fileBlank_16.png");
					}

				}

				public String getText(Object element) {
					File osmFile = new File(element.toString());
					if (osmFile.exists()) {
						return osmFile.getName() + "  -  (" + osmFile.getPath()
								+ ")";
					} else
						return null;

				}
			});
			lastFilesTableViewer.setInput(GuiOptionsHelper.getLastFiles());

		}
		GuiOptionsHelper.addInputFileListener(inputFileListener);
	}

	protected void selectFile(String fileName) {
		if (fileName != null && !fileName.equals(getSelection())) {
			GuiOptionsHelper.addUsedFile(fileName);
			refreshList();
			StructuredSelection selection = new StructuredSelection(fileName);
			lastFilesTableViewer.setSelection(selection);
		}
	}
	
	protected String getSelection() {
		ISelection selection = lastFilesTableViewer.getSelection();
		if (selection instanceof StructuredSelection && !selection.isEmpty()) {
			return ((StructuredSelection) selection).getFirstElement().toString();
		}
		return null;
	}
	
	public void refreshList() {
		lastFilesTableViewer.refresh();
	}

	@Override
	public void setFocus() {
		lastFilesTable.setFocus();
	}
	
	@Override
	public void dispose() {
		InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID).removePreferenceChangeListener(prefChangeListener);
		GuiOptionsHelper.removeInputFileListener(inputFileListener);
		super.dispose();
	}
}
