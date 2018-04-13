package com.tokopedia.events.domain.model.request.cart;

import com.google.gson.annotations.SerializedName;

public class OtherChargesItem{

	@SerializedName("conv_fee")
	private int convFee;

	public void setConvFee(int convFee){
		this.convFee = convFee;
	}

	public int getConvFee(){
		return convFee;
	}

	@Override
 	public String toString(){
		return 
			"OtherChargesItem{" + 
			"conv_fee = '" + convFee + '\'' + 
			"}";
		}
}