package com.mutouren.common.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExceptionUtils {

	/**
	 * 转化为非受检异常
	 * @param t
	 * @return
	 */
	public static RuntimeException doUnChecked(Throwable t) {
		if (t instanceof RuntimeException) {
			return (RuntimeException) t;
		} else {
			return new RuntimeException(t);
		}
	}
	
	/**
	 * 获取默认异常轨迹描述
	 * @param t
	 * @return
	 */
	public static String getDefaultStackTrace(Throwable t) {
		if(t == null) return "";
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(byteStream);
		t.printStackTrace(ps);
		return byteStream.toString();
	}
	
	/**
	 * 获取完整异常轨迹描述
	 * @param t
	 * @return
	 */
	public static String getStackTrace(Throwable t) {
		if(t == null) return "";
		StringBuilder sb = new StringBuilder();
		getStackTraceSub(t, sb);
		return sb.toString();
	}
	
	private static void getStackTraceSub(Throwable t, StringBuilder strBuilder) {
		strBuilder.append(String.format("%s: %s\r\n", t.getClass(), t.getMessage()));
		for(StackTraceElement obj : t.getStackTrace()) {
			strBuilder.append(" at " +obj.toString());
			strBuilder.append("\r\n");
		}		
		if(t.getCause() != null) {
			getStackTraceSub(t.getCause(), strBuilder);
		}			
	}
	
	public static boolean isContainException(Throwable t, Class<?> clazz) {
		
		if (t == null) return false;
		
		do {
			//if (t instanceof CommunicationsException) return true;
			if (clazz.isInstance(t)) return true;
			t = t.getCause();
		} while(t != null);
				
		return false;
	}	
		
}
