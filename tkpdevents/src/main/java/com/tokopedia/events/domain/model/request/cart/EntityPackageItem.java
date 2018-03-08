package com.tokopedia.events.domain.model.request.cart;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class EntityPackageItem {

	@SerializedName("price_per_seat")
	private int pricePerSeat;

	@SerializedName("seat_row_id")
	private List<String> seatRowId;

	@SerializedName("quantity")
	private int quantity;

	@SerializedName("group_id")
	private int groupId;

	@SerializedName("seat_id")
	private List<String> seatId;

	@SerializedName("seat_physical_row_id")
	private List<String> seatPhysicalRowId;

	@SerializedName("area_id")
	private String areaId;

	@SerializedName("provider_group_id")
	private String providerGroupId;

	@SerializedName("area_code")
	private List<String> areaCode;

	@SerializedName("product_id")
	private int productId;

	@SerializedName("description")
	private String description;

	@SerializedName("session_id")
	private String sessionId;

	@SerializedName("package_id")
	private int packageId;

	@SerializedName("schedule_id")
	private int scheduleId;

	public void setPricePerSeat(int pricePerSeat){
		this.pricePerSeat = pricePerSeat;
	}

	public int getPricePerSeat(){
		return pricePerSeat;
	}

	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	public int getQuantity(){
		return quantity;
	}

	public void setGroupId(int groupId){
		this.groupId = groupId;
	}

	public int getGroupId(){
		return groupId;
	}

	public void setSeatId(List<String> seatId){
		this.seatId = seatId;
	}

	public List<String> getSeatId(){
		return seatId;
	}

	public void setProductId(int productId){
		this.productId = productId;
	}

	public int getProductId(){
		return productId;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setSessionId(String sessionId){
		this.sessionId = sessionId;
	}

	public String getSessionId(){
		return sessionId;
	}

	public void setPackageId(int packageId){
		this.packageId = packageId;
	}

	public int getPackageId(){
		return packageId;
	}

	public void setScheduleId(int scheduleId){
		this.scheduleId = scheduleId;
	}

	public int getScheduleId(){
		return scheduleId;
	}

	public void setSeatRowId(List<String> seatRowId) {
		this.seatRowId = seatRowId;
	}

	public List<String> getSeatPhysicalRowId() {
		return seatPhysicalRowId;
	}

	public void setSeatPhysicalRowId(List<String> seatPhysicalRowId) {
		this.seatPhysicalRowId = seatPhysicalRowId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getProviderGroupId() {
		return providerGroupId;
	}

	public void setProviderGroupId(String providerGroupId) {
		this.providerGroupId = providerGroupId;
	}

	public void setAreaCode(List<String> areaCode) {
		this.areaCode = areaCode;
	}

	@Override
 	public String toString(){
		return 
			"EntityPackageItem{" +
			"price_per_seat = '" + pricePerSeat + '\'' + 
			",seat_row_id = '" + seatRowId + '\'' + 
			",quantity = '" + quantity + '\'' + 
			",group_id = '" + groupId + '\'' + 
			",seat_id = '" + seatId + '\'' + 
			",area_code = '" + areaCode + '\'' + 
			",product_id = '" + productId + '\'' + 
			",description = '" + description + '\'' + 
			",session_id = '" + sessionId + '\'' + 
			",package_id = '" + packageId + '\'' + 
			",schedule_id = '" + scheduleId + '\'' + 
			"}";
		}
}