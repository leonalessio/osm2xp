package com.osm2xp.jobs;

import org.eclipse.core.runtime.jobs.ISchedulingRule;

/**
 * MutexRule.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class MutexRule implements ISchedulingRule {
	public boolean isConflicting(ISchedulingRule rule) {
		return rule == this;
	}

	public boolean contains(ISchedulingRule rule) {
		return rule == this;
	}
}
