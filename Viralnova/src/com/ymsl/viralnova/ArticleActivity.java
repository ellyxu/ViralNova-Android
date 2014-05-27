package com.ymsl.viralnova;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class ArticleActivity extends Activity {

	private static final String TAG = ArticleActivity.class.getName();
	private Article mArticle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article);

		Intent intent = getIntent();
		mArticle = intent.getParcelableExtra("article");

		if (mArticle == null) {
			// TODO
		} else {
			WebView myWebView = (WebView) findViewById(R.id.webview);
			WebSettings webSettings = myWebView.getSettings();
			webSettings.setJavaScriptEnabled(true);
			myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
			myWebView.loadUrl("file:///android_asset/ViralNovaTemplate2.html");
		}
	}


class WebAppInterface {
	Context context;

	/** Instantiate the interface and set the context */
	WebAppInterface(Context c) {
		context = c;
	}

	@JavascriptInterface
	public String getTitle() {
//		String data = "{\"title\": \"Here's What The Future Was Going To Look Like According To These Old Ads. YES, Please!\",\"date\": \"May 12, 2014\",\"category\": \"Entertainment\"}";
		String data = "{\"title\": \"" + mArticle.getTitle() + "\",\"date\": \"" + mArticle.getDate() + "\",\"category\": \"" + mArticle.getCategory() + "\"}";
		Log.d(TAG, data);
		return data;
		
	}
	
	@JavascriptInterface
	public String getContent() {
		String data = mArticle.getContent();
		System.out.println(data);
		return data;
	}
	
	@JavascriptInterface
	public String getContent2() {
		final String data = "{" +
				"list: [" +
						"{'type':'text', 'text':'Humans, in general, have an obsession with the future. " +
						"We worry about what we will be doing in 5 years, what technological advancements will be and what the world will look " +
						"like for our children. We have always been that way.<br/><br/>Take these ads from past decades as an example. The vintage " +
						"advertisements attempted to guess what the future would look like. Sometimes they were close�� but most often, they were " +
						"way, way off.'}," +
						"{'type':'image', 'url':'http://content3.viralnova.com/wp-content/uploads/2014/05/vintage-ads-future.jpg', " +
						"'pre':'1.) They thought that THIS is what voicemail would look like.'}," +
						"{'type':'image', 'url':'http://content4.viralnova.com/wp-content/uploads/2014/05/vintage-ads-future2.jpg', " +
						"'pre':'2.) Instead of a portable AC unit, maybe just air condition the entire vehicle?'}," +
						"{'type':'image', 'url':'http://content4.viralnova.com/wp-content/uploads/2014/05/vintage-ads-future3.jpg', " +
						"'pre':'3.) Instead of automatic doors, there was�� this?'}," +
						"{'type':'image', 'url':'http://content4.viralnova.com/wp-content/uploads/2014/05/vintage-ads-future4.jpg', " +
						"'pre':'4.) Not sure if we��ll ever get to the Jetson flying car like this.'}," +
						"{'type':'image', 'url':'http://content4.viralnova.com/wp-content/uploads/2014/05/vintage-ads-future5.jpg', " +
						"'pre':'5.) Self-driving cars? Yes, please.'}," +
						"{'type':'image', 'url':'http://content4.viralnova.com/wp-content/uploads/2014/05/vintage-ads-future6.jpg', " +
						"'pre':'6.) Self-checkins are thankfully not this complicated at the airport. Most of the time.'}," +
						"{'type':'image', 'url':'http://content4.viralnova.com/wp-content/uploads/2014/05/vintage-ads-future7.jpg', " +
						"'pre':'7.) Getting things delivered to you isn��t that outlandish.'}," +
						"{'type':'image', 'url':'http://content4.viralnova.com/wp-content/uploads/2014/05/vintage-ads-future8.jpg', " +
						"'pre':'8.) They were right. Food trucks ARE awesome.'}," +
						"{'type':'image', 'url':'http://content4.viralnova.com/wp-content/uploads/2014/05/vintage-ads-future9.jpg', " +
						"'pre':'9.) This HAS to happen.'}," +
						"{'type':'image', 'url':'http://content4.viralnova.com/wp-content/uploads/2014/05/vintage-ads-future10.jpg', " +
						"'pre':'10.) Because I never want to fold clothes ever again.'}," +
					"]}";
		Log.d(TAG, data);
		return data;
	}

	/** Show a toast from the web page */
	@JavascriptInterface
	public void showToast(String toast) {
		Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
	}
}

}