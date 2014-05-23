package com.ymsl.viralnova.cache;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.util.Log;

public class MemoryCache {

	private static final String TAG = MemoryCache.class.getName();
	// 放入缓存是一个同步操作
	// LinkedHashMap构造方法的最后一个参数true代表这个map里的元素将按照
	// 最近使用次数由少到多排列，即LRU。
	// 这样的好处是如果要将缓存中的元素替换，则先遍历出最近最少使用的元素
	// 来替换以提高效率。
	private Map<String, Bitmap> cache = Collections
			.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));
	// 缓存中图片所占用的字节，初始0，将通过此变量严格控制缓存所占用的内存
	private long size = 0; // current allocated size
	// 缓存只能占用的最大堆内存
	private long limit = 1024 * 1024; // max memory in bytes

	public MemoryCache() {
		// use 10% of available heap size
		setLimit(Runtime.getRuntime().maxMemory() / 10);
	}

	public void setLimit(long new_limit) {
		limit = new_limit;
		Log.i(TAG, "MemoryCache will use up to " + limit / 1024. / 1024. + "MB");
	}

	public Bitmap get(String id) {
		try {
			if (!cache.containsKey(id))
				return null;
			return cache.get(id);
		} catch (NullPointerException e) {
			return null;
		}
	}

	public void put(String id, Bitmap bitmap) {
		try {
			if (cache.containsKey(id))
				size -= getSizeInBytes(cache.get(id));
			cache.put(id, bitmap);
			size += getSizeInBytes(bitmap);
			checkSize();
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

	private void checkSize() {
		Log.i(TAG, "cache size=" + size + " length=" + cache.size());
		if (size > limit) {
			// 先遍历最近最少使用的元素
			Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Bitmap> entry = iter.next();
				size -= getSizeInBytes(entry.getValue());
				iter.remove();
				if (size <= limit)
					break;
			}
			Log.i(TAG, "Clean cache. New size " + cache.size());
		}
	}
	
	public void clear(){
		cache.clear();
	}
	
	/**
	 * 图片占用的内存
	 * @param bitmap
	 * @return
	 */
	long getSizeInBytes(Bitmap bitmap){
		if(bitmap == null)
			return 0;
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

}
