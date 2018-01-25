package com.tokopedia.movies.domain.model.request.cart;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
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