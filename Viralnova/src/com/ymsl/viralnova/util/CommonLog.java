package com.ymsl.viralnova.util;

import android.util.Log;

public class CommonLog {

	private String tag = "CommonLog";
	public static int logLevel = Log.VERBOSE;
	public static boolean isDebug = true;

	public CommonLog() {
	}

	public CommonLog(String tag) {
		this.tag = tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	private String getFunctionName() {
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (sts == null) {
			return null;
		}
		for (StackTraceElement st : sts) {
			if (st.isNativeMethod()) {
				continue;
			}
			if (st.getClassName().equals(Thread.class.getName())) {
				continue;
			}
			if (st.getClassName().equals(this.getClass().getName())) {
				continue;
			}
			return "[" + Thread.currentThread().getId() + ": "
					+ st.getFileName() + ": " + st.getLineNumber() + "]";
		}
		return null;
	}

	public void info(Object str) {
		if (logLevel <= Log.INFO) {
			String name = getFunctionName();
			String ls = (name == null ? str.toString() : (name + " - " + str));
			Log.i(tag, ls);
		}
	}

}
