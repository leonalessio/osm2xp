package com.osm2xp.writers.impl;

import com.osm2xp.utils.MiscUtils;

public class RunProcessRunnable implements Runnable {
	
	private final String command;

	public RunProcessRunnable(String command) {
		this.command = command;
	}

	@Override
	public void run() {
		MiscUtils.execProgram(command);
	}

}
