package com.tokopedia.events.domain.model.request.likes;

import com.google.gson.annotations.SerializedName;

public class LikeUpdateModel{

	@SerializedName("rating")
	private Rating rating;

	public void setRating(Rating rating){
		this.rating = rating;
	}

	public Rating getRating(){
		return rating;
	}

	@Override
 	public String toString(){
		return 
			"LikeUpdateModel{" + 
			"rating = '" + rating + '\'' + 
			"}";
		}
}