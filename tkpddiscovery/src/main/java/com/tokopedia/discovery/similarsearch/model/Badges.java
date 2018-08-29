package com.tokopedia.discovery.similarsearch.model;

import com.google.gson.annotations.SerializedName;

public class Badges{


	@SerializedName("image_url")
	private String imageUrl;

	@SerializedName("title")
	private String title;


	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	@Override
 	public String toString(){
		return 
			"Badges{" + 
			",image_url = '" + imageUrl + '\'' +
			",title = '" + title + '\'' + 
			"}";
		}
}