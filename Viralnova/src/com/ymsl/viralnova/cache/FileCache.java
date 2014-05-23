package com.ymsl.viralnova.cache;

import android.content.Context;

import com.ymsl.viralnova.util.FileManager;

public class FileCache extends AbstractFileCache {

	public FileCache(Context context) {
		super(context);
	}

	@Override
	public String getSavePath(String url) {
		String fileName = String.valueOf(url.hashCode());
		return getCacheDir() + fileName;
	}

	@Override
	public String getCacheDir() {
		return FileManager.getSaveFilePath();
	}

}
