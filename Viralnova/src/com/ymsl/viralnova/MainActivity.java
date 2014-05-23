package com.ymsl.viralnova;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.ymsl.viralnova.cache.ImageLoader;
import com.ymsl.viralnova.util.NetUtil;

public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getName();

	private ListView mArticlesLV;
	private ArticlesAdapter mArticlesAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mArticlesLV = (ListView) findViewById(R.id.articles_lv);
		mArticlesLV.setOnScrollListener(mScrollListener);
		setOnItemClickListener();
		setAdapter();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	protected void onDestroy() {
		if (mArticlesAdapter != null) {
			ImageLoader imageLoader = mArticlesAdapter.getImageLoader();
			if (imageLoader != null) {
				imageLoader.clearCache();
			}
		}
		super.onDestroy();
	}

	private void setAdapter() {
		try {
			new AsyncTask<Void, Void, String>() {

				@Override
				protected String doInBackground(Void... params) {
					String allArticles;
					try {
						allArticles = NetUtil
								.getAllArticles(Constants.url_getAllArticles);
						return allArticles;
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}

				protected void onPostExecute(String allArticles) {
					ArrayList<Article> articles = (ArrayList<Article>) JSON
							.parseArray(allArticles, Article.class);
					mArticlesAdapter = new ArticlesAdapter(
							getApplicationContext(), articles);
					mArticlesLV.setAdapter(mArticlesAdapter);
				}

			}.execute();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setOnItemClickListener() {
		mArticlesLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mArticlesAdapter == null)
					return;
				ArrayList<Article> articles = mArticlesAdapter.getArticles();
				Intent intent = new Intent(getApplicationContext(),
						ArticleActivity.class);
				intent.putExtra("article", articles.get(position));
				startActivity(intent);
			}

		});
	}

	OnScrollListener mScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (mArticlesAdapter == null)
				return;
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_FLING:
				mArticlesAdapter.setFlagBusy(true);
				break;
			case OnScrollListener.SCROLL_STATE_IDLE:
				mArticlesAdapter.setFlagBusy(false);
				break;
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				mArticlesAdapter.setFlagBusy(false);
				break;
			default:
				break;
			}
			mArticlesAdapter.notifyDataSetChanged();
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}
	};
}
