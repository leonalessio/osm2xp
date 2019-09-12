package com.osm2xp.gui.views.panels.xplane;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.osm2xp.generation.options.rules.IHasAreaTypes;

public class LanduseText extends Composite{

	private IHasAreaTypes rule;
	private Text text;
	
	public LanduseText(Composite parent) {
		super(parent, SWT.NONE);
		
		setLayout(GridLayoutFactory.fillDefaults().numColumns(2).create());
		
		Label lblAreaType = new Label(this, SWT.NONE);
		lblAreaType.setText("Area type: ");
		lblAreaType.setToolTipText("Allowed/disallowed landuse Area type list for this rule, comma-separated. Leave empty, if area type should be ignored.");
		GridDataFactory.swtDefaults().applyTo(lblAreaType);
		
		text = new Text(this, SWT.BORDER);
		text.setEnabled(false);
		text.addModifyListener(e -> {
			rule.setAreaTypes(text.getText());
		});
		GridDataFactory.fillDefaults().grab(true, false).applyTo(text);
	}

	public IHasAreaTypes getRule() {
		return rule;
	}

	public void setRule(IHasAreaTypes rule) {
		this.rule = rule;
		text.setEnabled(rule != null);
		text.setText(StringUtils.stripToEmpty(rule.getAreaTypes()));
	}

}
