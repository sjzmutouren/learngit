package com.mutouren.common.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.mutouren.common.exception.ExceptionUtils;

public class ThreadPoolManager {
	private volatile static boolean isRun = true;
	private static volatile ExecutorService exec = Executors.newCachedThreadPool();
	private static List<ExecutorService> listThreadPool = new ArrayList<ExecutorService>();
	
	//private static Logger errorLogger = LogManager.getLogger(LogAlias.SystemError.name());
	
	public static synchronized ExecutorService createThreadPool(int corePoolSize,
        int maximumPoolSize,
        long keepAliveTime,
        TimeUnit unit,
        BlockingQueue<Runnable> workQueue) {

		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		listThreadPool.add(threadPool);
		return threadPool;
	}
	
	public static synchronized ExecutorService createSingleThreadExecutor() {		
		ExecutorService executorService = Executors.newSingleThreadExecutor();			
		listThreadPool.add(executorService);
		return executorService;
	}	
	
	private static synchronized void closeListThreadPool() {
		for(ExecutorService pool : listThreadPool) {
			pool.shutdownNow();
		}
	}
	
	public static void asynRun(Runnable runJob) {
		try {
			exec.execute(runJob);
		}  catch (Exception e) {
			throw ExceptionUtils.doUnChecked(e);
			//errorLogger.error("asynRun job error: ", e);
		}
	}
	
	public static void close() {
		isRun = false;
		exec.shutdownNow();
		
		closeListThreadPool();
	}
	
	public static boolean isRun() {
		return isRun;
	}
}

