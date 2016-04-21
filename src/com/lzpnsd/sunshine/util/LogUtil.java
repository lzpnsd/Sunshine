package com.lzpnsd.sunshine.util;

import android.util.Log;

public class LogUtil {

	private String mClassName;
	
	public static LogUtil getLog(Class cla){
		return new LogUtil(cla);
	}

	private LogUtil(Class cla){
		mClassName = cla.getSimpleName();
	}
	
	public void d(String msg) {
//		for (int i = 0; i < stack.length; i++) {
//			StackTraceElement ste = stack[i];
//			System.out.println(ste.getClassName() + "." + ste.getMethodName() + "(...)");
//			System.out.println(i + "--" + ste.getMethodName() + "--" + ste.getFileName() + "--" + ste.getLineNumber());
//		}
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		StackTraceElement ste = stack[1];
		String logOut = "["+ste.getClassName()+"--"+ste.getMethodName()+"--"+ste.getLineNumber()+"] : "+msg; 
		Log.d(mClassName, logOut);
	}

	public void i(String msg) {
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		StackTraceElement ste = stack[1];
		String logOut = "["+ste.getClassName()+"--"+ste.getMethodName()+"--"+ste.getLineNumber()+"] : "+msg; 
		Log.i(mClassName, logOut);
	}

	public void e(String msg) {
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		StackTraceElement ste = stack[1];
		String logOut = "["+ste.getClassName()+"--"+ste.getMethodName()+"--"+ste.getLineNumber()+"] : "+msg; 
		Log.e(mClassName, logOut);
	}

}
