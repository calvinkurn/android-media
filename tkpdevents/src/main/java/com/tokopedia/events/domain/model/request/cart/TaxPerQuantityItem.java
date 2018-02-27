package com.tokopedia.events.domain.model.request.cart;

import com.google.gson.annotations.SerializedName;

public class TaxPerQuantityItem{

	@SerializedName("entertaiment")
	private int entertaiment;
	@SerializedName("service")
	private int service;

	public void setEntertaiment(int entertaiment){
		this.entertaiment = entertaiment;
	}

	public int getEntertaiment(){
		return entertaiment;
	}

	@Override
 	public String toString(){
		return 
			"TaxPerQuantityItem{" + 
			"entertaiment = '" + entertaiment + '\'' + 
			"}";
		}
}