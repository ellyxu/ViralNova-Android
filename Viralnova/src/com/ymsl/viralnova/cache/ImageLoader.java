package com.ymsl.viralnova.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.ymsl.viralnova.util.CommonUtil;

public class ImageLoader {

	private static final String TAG = ImageLoader.class.getName();
	private MemoryCache memoryCache = new MemoryCache();
	private AbstractFileCache fileCache;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	private ExecutorService executorService;
	private Context context;

	public ImageLoader(Context context) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
		this.context = context;
	}

	public void DisplayImage(String photo_url, ImageView imageView,
			boolean isLoadOnlyFromCache) {
		imageViews.put(imageView, photo_url);
		// �ȴ��ڴ滺���в���
		Bitmap bitmap = memoryCache.get(photo_url);
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else if (!isLoadOnlyFromCache) {
			// ��û�еĻ��������̼߳���ͼƬ
			queuePhoto(photo_url, imageView);
		}
	}

	private void queuePhoto(String photo_url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(photo_url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		public PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.url);
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			// ���µĲ�������UI�߳���
			photoToLoad.imageView.post(bd);
		}
	}
	
	private Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		// �ȴ��ļ������в����Ƿ���
		Bitmap b = null;
		if (f != null && f.exists()) {
			b = decodeFile(f);
		}
		if (b != null) {
			return b;
		}
		// ����ָ����url������ͼƬ
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			CopyStream(is, os);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Exception e) {
			Log.e(TAG,
					"getBitmap catch Exception...\nmessage = " + e.getMessage());
			return null;
		}
	}

	/**
	 * decode���ͼƬ���Ұ��������Լ����ڴ����ģ��������ÿ��ͼƬ�Ļ����С Ҳ�������Ƶ�
	 * 
	 * @param f
	 * @return
	 */
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			int width_tmp = o.outWidth;
			int scale = 1;
			int screenWidth = CommonUtil.getScreenWidth(context);
			while (true) {
				if (width_tmp / 2 < screenWidth)
					break;
				width_tmp /= 2;
				scale *= 2;
			}
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
			// TODO: handle exception
		}
		return null;
	}
	
	/**
	 * ��ֹͼƬ��λ
	 * 
	 * @param photoToLoad
	 * @return true ����ͼƬ��λ     false ��ʾû�д�λ
	 */
	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// ������UI�߳��и��½���
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap bmp, PhotoToLoad photoToLoad) {
			Log.d(TAG, "BitmapDisplayer created");
			bitmap = bmp;
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null) {
				Log.d(TAG, "BitmapDisplayer before");
				photoToLoad.imageView.setImageBitmap(bitmap);
				Log.d(TAG, "BitmapDisplayer after");
			}
		}

	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception e) {
			Log.e(TAG, "CopyStream catch Exception...");
		}
	}

}
