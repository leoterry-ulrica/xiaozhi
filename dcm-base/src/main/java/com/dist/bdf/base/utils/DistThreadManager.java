package com.dist.bdf.base.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DistThreadManager {

	/**
	 * 缓存线程池对象
	 */
	public static ExecutorService MyCacheThreadPool = Executors.newCachedThreadPool();
	
}
