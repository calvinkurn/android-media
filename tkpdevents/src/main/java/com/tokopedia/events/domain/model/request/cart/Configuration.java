package com.tokopedia.events.domain.model.request.cart;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Configuration{

	@SerializedName("price")
	private int price;

	@SerializedName("sub_config")
	private SubConfig subConfig;

	public void setPrice(int price){
		this.price = price;
	}

	public int getPrice(){
		return price;
	}

	public void setSubConfig(SubConfig subConfig){
		this.subConfig = subConfig;
	}

	public SubConfig getSubConfig(){
		return subConfig;
	}

	@Override
 	public String toString(){
		return 
			"Configuration{" + 
			"price = '" + price + '\'' + 
			",sub_config = '" + subConfig + '\'' + 
			"}";
		}
}