package com.osm2xp.gui.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

public class DelegateSelectionProvider implements ISelectionProvider {

	private ISelectionProvider provider;
	List<ISelectionChangedListener> listeners = new ArrayList<>();

	public DelegateSelectionProvider(ISelectionProvider provider) {
		this.provider = provider;
	}

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		provider.addSelectionChangedListener(listener);
		listeners.add(listener);
	}

	public ISelection getSelection() {
		return provider.getSelection();
	}

	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		provider.removeSelectionChangedListener(listener);
		listeners.remove(listener);
	}

	public void setSelection(ISelection selection) {
		provider.setSelection(selection);
	}

	public void fireSelection(StructuredSelection selection) {
		SelectionChangedEvent event = new SelectionChangedEvent(this, selection);
		for (ISelectionChangedListener listener : listeners) {
			listener.selectionChanged(event);
		}
	}
	
	

}
