package com.tokopedia.movies.data.entity.response;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.movies.data.entity.response.seatlayoutresponse.SeatLayoutResponse;

import org.json.JSONObject;

public class SeatLayoutItem {


	@SerializedName("layout")
	@Expose
	private String layout;

	public void setLayout(String layout){
		this.layout = layout;
	}

	public String getLayout(){
		return layout;
	}

}
