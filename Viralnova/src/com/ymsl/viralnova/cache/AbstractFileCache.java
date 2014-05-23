package com.ymsl.viralnova.cache;

import java.io.File;

import android.content.Context;

import com.ymsl.viralnova.util.FileHelper;

public abstract class AbstractFileCache {

	private String dirString;

	public AbstractFileCache(Context context) {
		dirString = getCacheDir();
		FileHelper.createDirectory(dirString);
	}

	public File getFile(String url) {
		File f = new File(getSavePath(url));
		return f;
	}

	public abstract String getSavePath(String url);

	public abstract String getCacheDir();

	public void clear() {
		FileHelper.deleteDirectory(dirString);
	}
}
