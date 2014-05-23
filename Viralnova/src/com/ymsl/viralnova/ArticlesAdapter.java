package com.ymsl.viralnova;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ymsl.viralnova.cache.ImageLoader;

public class ArticlesAdapter extends BaseAdapter implements ListAdapter {

	private static final String TAG = ArticlesAdapter.class.getName();
	private Context mContext;
	private ArrayList<Article> mArticles;

	private boolean mBusy = false;

	private ImageLoader mImageLoader;

	public void setFlagBusy(boolean busy) {
		this.mBusy = busy;
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

	public ArticlesAdapter(Context context, ArrayList<Article> articles) {
		mImageLoader = new ImageLoader(context);
		this.mContext = context;
		this.mArticles = articles;
	}

	public ArrayList<Article> getArticles() {
		return mArticles;
	}

	@Override
	public int getCount() {
		if (mArticles != null) {
			return mArticles.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mArticles != null) {
			return mArticles.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String title = mArticles.get(position).getTitle();
		String date = mArticles.get(position).getDate();
		String category = mArticles.get(position).getCategory();
		String imageUrl = mArticles.get(position).getImageUrl();
		String entryContent = mArticles.get(position).getEntryContent();
		ViewHolder viewHolder;
		if (convertView != null) {
			viewHolder = (ViewHolder) convertView.getTag();
		} else {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.date = (TextView) convertView.findViewById(R.id.date);
			viewHolder.category = (TextView) convertView
					.findViewById(R.id.category);
			viewHolder.photo = (ImageView) convertView.findViewById(R.id.photo);
			viewHolder.entryContent = (TextView) convertView
					.findViewById(R.id.article_short);
			convertView.setTag(viewHolder);
		}
		viewHolder.title.setText(Html.fromHtml(title));
		viewHolder.date.setText(date);
		viewHolder.category.setText(category);
		viewHolder.photo.setImageResource(R.drawable.ic_launcher);
		viewHolder.entryContent.setText(Html.fromHtml(entryContent));
		Log.d(TAG, entryContent);

		if (!mBusy) {
			mImageLoader.DisplayImage(imageUrl, viewHolder.photo, false);
		} else {
			mImageLoader.DisplayImage(imageUrl, viewHolder.photo, false);
		}

		return convertView;
	}

	static class ViewHolder {
		TextView title;
		TextView date;
		TextView category;
		ImageView photo;
		TextView entryContent;
	}
}
