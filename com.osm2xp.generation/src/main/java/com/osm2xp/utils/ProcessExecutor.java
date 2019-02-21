package com.osm2xp.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class ProcessExecutor {
	
	private static ProcessExecutor instance;
	
	private ExecutorService service = Executors.newFixedThreadPool(10, new ThreadFactoryBuilder().setDaemon(true).build());
	
	public static synchronized ProcessExecutor getExecutor() {
		if (instance == null) {
			instance = new ProcessExecutor();
		}
		return instance;
		
	}
	
	private ProcessExecutor() {
	}
	
	public Future<?> execute(Runnable runnable) {
		return service.submit(runnable);
	}
	
	public Future<?> execute(Callable<?> callable) {
		return service.submit(callable);
	}

}
