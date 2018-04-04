package com.tokopedia.events.domain.model.request.cart;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CartItems{

	@SerializedName("cart_items")
	private List<CartItem> cartItems;

	@SerializedName("promocode")
	private String promocode;

	public void setCartItems(List<CartItem> cartItems){
		this.cartItems = cartItems;
	}

	public List<CartItem> getCartItems(){
		return cartItems;
	}

	public void setPromocode(String promocode){
		this.promocode = promocode;
	}

	public String getPromocode(){
		return promocode;
	}

	@Override
 	public String toString(){
		return 
			"CartItems{" + 
			"cart_items = '" + cartItems + '\'' + 
			",promocode = '" + promocode + '\'' + 
			"}";
		}
}