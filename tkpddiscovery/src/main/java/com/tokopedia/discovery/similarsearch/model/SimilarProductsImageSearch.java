package com.tokopedia.discovery.similarsearch.model;

import com.google.gson.annotations.SerializedName;

public class SimilarProductsImageSearch{

	@SerializedName("data")
	private Data data;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"SimilarProductsImageSearch{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}