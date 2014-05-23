package com.ymsl.viralnova.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.accounts.NetworkErrorException;
import android.util.Log;

public class NetUtil {

	private static final String TAG = NetUtil.class.getName();

	public static String getAllArticles2(String address) throws Exception {
		URL url = new URL(address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(3000);

		int code = conn.getResponseCode();
		Log.d(TAG, "code = " + code);
		if (code == 200) {
			InputStream in = conn.getInputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = in.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			in.close();
			bos.close();

			String articles = new String(bos.toByteArray());
			return articles;
		}
		throw new NetworkErrorException("网络连接出错!");
	}
	
	public static String getAllArticles(String address) throws Exception {
		HttpGet httpGet = new HttpGet(address);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity httpEntity = httpResponse.getEntity();
		InputStream is = httpEntity.getContent();
		BufferedReader br = new BufferedReader(  
                new InputStreamReader(is,"GB2312"));  
		String data = "";
		StringBuffer sb = new StringBuffer();
		while((data = br.readLine()) != null) {
			sb.append(data);
		}
		
		is.close();
		br.close();

		String articles = new String(sb.toString());
		return articles;		
		
		
	}

}
