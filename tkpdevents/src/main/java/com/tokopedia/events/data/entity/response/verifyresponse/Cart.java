package com.tokopedia.events.data.entity.response.verifyresponse;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Cart{

	@SerializedName("total_price")
	private int totalPrice;

	@SerializedName("promocode_success_message")
	private String promocodeSuccessMessage;

	@SerializedName("cart_items")
	private List<CartItemsItem> cartItems;

	@SerializedName("promocode_failure_message")
	private String promocodeFailureMessage;

	@SerializedName("count")
	private int count;

	@SerializedName("total_conv_fee")
	private int totalConvFee;

	@SerializedName("promocode")
	private String promocode;

	@SerializedName("error")
	private String error;

	@SerializedName("promocode_status")
	private String promocodeStatus;

	@SerializedName("promocode_cashback")
	private int promocodeCashback;

	@SerializedName("promocode_discount")
	private int promocodeDiscount;

	@SerializedName("error_code")
	private String errorCode;

	@SerializedName("grand_total")
	private int grandTotal;

	@SerializedName("user")
	private User user;

	public void setTotalPrice(int totalPrice){
		this.totalPrice = totalPrice;
	}

	public int getTotalPrice(){
		return totalPrice;
	}

	public void setPromocodeSuccessMessage(String promocodeSuccessMessage){
		this.promocodeSuccessMessage = promocodeSuccessMessage;
	}

	public String getPromocodeSuccessMessage(){
		return promocodeSuccessMessage;
	}

	public void setCartItems(List<CartItemsItem> cartItems){
		this.cartItems = cartItems;
	}

	public List<CartItemsItem> getCartItems(){
		return cartItems;
	}

	public void setPromocodeFailureMessage(String promocodeFailureMessage){
		this.promocodeFailureMessage = promocodeFailureMessage;
	}

	public String getPromocodeFailureMessage(){
		return promocodeFailureMessage;
	}

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	public void setTotalConvFee(int totalConvFee){
		this.totalConvFee = totalConvFee;
	}

	public int getTotalConvFee(){
		return totalConvFee;
	}

	public void setPromocode(String promocode){
		this.promocode = promocode;
	}

	public String getPromocode(){
		return promocode;
	}

	public void setError(String error){
		this.error = error;
	}

	public String getError(){
		return error;
	}

	public void setPromocodeStatus(String promocodeStatus){
		this.promocodeStatus = promocodeStatus;
	}

	public String getPromocodeStatus(){
		return promocodeStatus;
	}

	public void setPromocodeCashback(int promocodeCashback){
		this.promocodeCashback = promocodeCashback;
	}

	public int getPromocodeCashback(){
		return promocodeCashback;
	}

	public void setPromocodeDiscount(int promocodeDiscount){
		this.promocodeDiscount = promocodeDiscount;
	}

	public int getPromocodeDiscount(){
		return promocodeDiscount;
	}

	public void setErrorCode(String errorCode){
		this.errorCode = errorCode;
	}

	public String getErrorCode(){
		return errorCode;
	}

	public void setGrandTotal(int grandTotal){
		this.grandTotal = grandTotal;
	}

	public int getGrandTotal(){
		return grandTotal;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

	@Override
 	public String toString(){
		return 
			"Cart{" + 
			"total_price = '" + totalPrice + '\'' + 
			",promocode_success_message = '" + promocodeSuccessMessage + '\'' + 
			",cart_items = '" + cartItems + '\'' + 
			",promocode_failure_message = '" + promocodeFailureMessage + '\'' + 
			",count = '" + count + '\'' + 
			",total_conv_fee = '" + totalConvFee + '\'' + 
			",promocode = '" + promocode + '\'' + 
			",error = '" + error + '\'' + 
			",promocode_status = '" + promocodeStatus + '\'' + 
			",promocode_cashback = '" + promocodeCashback + '\'' + 
			",promocode_discount = '" + promocodeDiscount + '\'' + 
			",error_code = '" + errorCode + '\'' + 
			",grand_total = '" + grandTotal + '\'' + 
			",user = '" + user + '\'' + 
			"}";
		}
}