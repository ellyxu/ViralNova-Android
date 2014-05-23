package com.ymsl.viralnova;

import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable {

	private String title;
	private String url;
	private String date;
	private String category;
	private String imageUrl;
	private String entryContent;
	private String content;
	
	public Article() {
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getEntryContent() {
		return entryContent;
	}

	public void setEntryContent(String entryContent) {
		this.entryContent = entryContent;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Article [title=" + title + ", url=" + url + ", date=" + date
				+ ", category=" + category + ", imageUrl=" + imageUrl
				+ ", entryContent=" + entryContent + ", content=" + content
				+ "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(url);
		dest.writeString(date);
		dest.writeString(category);
		dest.writeString(imageUrl);
		dest.writeString(entryContent);
		dest.writeString(content);
	}

	public Article(Parcel in) {
		title = in.readString();
		url = in.readString();
		date = in.readString();
		category = in.readString();
		imageUrl = in.readString();
		entryContent = in.readString();
		content = in.readString();
	}

	public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {

		@Override
		public Article createFromParcel(Parcel source) {
			return new Article(source);
		}

		@Override
		public Article[] newArray(int size) {
			return new Article[size];
		}
	};

}
