package com.tokopedia.events.data.entity.response.verifyresponse;

import com.google.gson.annotations.SerializedName;

public class EntityPackagesItem{

	@SerializedName("error_message")
	private String errorMessage;

	@SerializedName("price_per_seat")
	private int pricePerSeat;

	@SerializedName("address")
	private String address;

	@SerializedName("quantity")
	private int quantity;

	@SerializedName("city")
	private String city;

	@SerializedName("package_id")
	private int packageId;

	@SerializedName("display_name")
	private String displayName;

	@SerializedName("package_price")
	private int packagePrice;

	@SerializedName("group_id")
	private int groupId;

	@SerializedName("product_id")
	private int productId;

	@SerializedName("base_price")
	private int basePrice;

	@SerializedName("commission")
	private int commission;

	@SerializedName("schedule_id")
	private int scheduleId;

	@SerializedName("total_ticket_count")
	private int totalTicketCount;

	public void setErrorMessage(String errorMessage){
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage(){
		return errorMessage;
	}

	public void setPricePerSeat(int pricePerSeat){
		this.pricePerSeat = pricePerSeat;
	}

	public int getPricePerSeat(){
		return pricePerSeat;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	public int getQuantity(){
		return quantity;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setPackageId(int packageId){
		this.packageId = packageId;
	}

	public int getPackageId(){
		return packageId;
	}

	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}

	public String getDisplayName(){
		return displayName;
	}

	public void setPackagePrice(int packagePrice){
		this.packagePrice = packagePrice;
	}

	public int getPackagePrice(){
		return packagePrice;
	}

	public void setGroupId(int groupId){
		this.groupId = groupId;
	}

	public int getGroupId(){
		return groupId;
	}

	public void setProductId(int productId){
		this.productId = productId;
	}

	public int getProductId(){
		return productId;
	}

	public void setBasePrice(int basePrice){
		this.basePrice = basePrice;
	}

	public int getBasePrice(){
		return basePrice;
	}

	public void setCommission(int commission){
		this.commission = commission;
	}

	public int getCommission(){
		return commission;
	}

	public void setScheduleId(int scheduleId){
		this.scheduleId = scheduleId;
	}

	public int getScheduleId(){
		return scheduleId;
	}

	public void setTotalTicketCount(int totalTicketCount){
		this.totalTicketCount = totalTicketCount;
	}

	public int getTotalTicketCount(){
		return totalTicketCount;
	}

	@Override
 	public String toString(){
		return 
			"EntityPackagesItem{" + 
			"error_message = '" + errorMessage + '\'' + 
			",price_per_seat = '" + pricePerSeat + '\'' + 
			",address = '" + address + '\'' + 
			",quantity = '" + quantity + '\'' + 
			",city = '" + city + '\'' + 
			",package_id = '" + packageId + '\'' + 
			",display_name = '" + displayName + '\'' + 
			",package_price = '" + packagePrice + '\'' + 
			",group_id = '" + groupId + '\'' + 
			",product_id = '" + productId + '\'' + 
			",base_price = '" + basePrice + '\'' + 
			",commission = '" + commission + '\'' + 
			",schedule_id = '" + scheduleId + '\'' + 
			",total_ticket_count = '" + totalTicketCount + '\'' + 
			"}";
		}
}