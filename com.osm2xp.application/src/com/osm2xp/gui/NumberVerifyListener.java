package com.osm2xp.gui;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;

public class NumberVerifyListener implements VerifyListener {
	
	private boolean withPoint;

	public NumberVerifyListener(boolean withPoint) {
		this.withPoint = withPoint;
	}
	
	public NumberVerifyListener() {
		this(false);
	}
	
	@Override
	public void verifyText(VerifyEvent e) {
		String string = e.text;
		char[] chars = new char[string.length()];
		string.getChars(0, chars.length, chars, 0);
		for (int i = 0; i < chars.length; i++) {
			if (!(('0' <= chars[i] && chars[i] <= '9') || (withPoint && chars[i] == '.'))) {
				e.doit = false;
				return;
			}
		}
	}
}
