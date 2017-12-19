package com.tokopedia.events.data.entity.response.verifyresponse;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class VerifyCartResponse{

	@SerializedName("cart")
	private Cart cart;

	@SerializedName("status")
	private Status status;

	public void setCart(Cart cart){
		this.cart = cart;
	}

	public Cart getCart(){
		return cart;
	}

	public void setStatus(Status status){
		this.status = status;
	}

	public Status getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"VerifyCartResponse{" + 
			"cart = '" + cart + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}