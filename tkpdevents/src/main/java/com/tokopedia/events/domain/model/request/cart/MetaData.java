package com.tokopedia.events.domain.model.request.cart;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MetaData{

	@SerializedName("entity_product_id")
	private int entityProductId;

	@SerializedName("entity_end_time")
	private String entityEndTime;

	@SerializedName("entity_start_time")
	private String entityStartTime;

	@SerializedName("entity_category_id")
	private int entityCategoryId;

	@SerializedName("city_searched")
	private String citySearched;

	@SerializedName("tax_per_quantity")
	private List<TaxPerQuantityItem> taxPerQuantity;

	@SerializedName("total_other_charges")
	private int totalOtherCharges;

	@SerializedName("entity_passengers")
	private List<EntityPassengerItem> entityPassengers;

	@SerializedName("total_ticket_price")
	private int totalTicketPrice;

	@SerializedName("entity_category_name")
	private String entityCategoryName;

	@SerializedName("entity_group_id")
	private int entityGroupId;

	@SerializedName("other_charges")
	private List<OtherChargesItem> otherCharges;

	@SerializedName("entity_address")
	private EntityAddress entityAddress;

	@SerializedName("total_tax_amount")
	private int totalTaxAmount;

	@SerializedName("entity_image")
	private String entityImage;

	@SerializedName("entity_schedule_id")
	private int entityScheduleId;

	@SerializedName("entity_packages")
	private List<EntityPackageItem> entityPackages;

	@SerializedName("total_ticket_count")
	private int totalTicketCount;

	public void setEntityProductId(int entityProductId){
		this.entityProductId = entityProductId;
	}

	public int getEntityProductId(){
		return entityProductId;
	}

	public void setEntityEndTime(String entityEndTime){
		this.entityEndTime = entityEndTime;
	}

	public String getEntityEndTime(){
		return entityEndTime;
	}

	public void setEntityStartTime(String entityStartTime){
		this.entityStartTime = entityStartTime;
	}

	public String getEntityStartTime(){
		return entityStartTime;
	}

	public void setEntityCategoryId(int entityCategoryId){
		this.entityCategoryId = entityCategoryId;
	}

	public int getEntityCategoryId(){
		return entityCategoryId;
	}

	public void setCitySearched(String citySearched){
		this.citySearched = citySearched;
	}

	public String getCitySearched(){
		return citySearched;
	}

	public void setTaxPerQuantity(List<TaxPerQuantityItem> taxPerQuantity){
		this.taxPerQuantity = taxPerQuantity;
	}

	public List<TaxPerQuantityItem> getTaxPerQuantity(){
		return taxPerQuantity;
	}

	public void setTotalOtherCharges(int totalOtherCharges){
		this.totalOtherCharges = totalOtherCharges;
	}

	public int getTotalOtherCharges(){
		return totalOtherCharges;
	}

	public void setEntityPassengers(List<EntityPassengerItem> entityPassengers){
		this.entityPassengers = entityPassengers;
	}

	public List<EntityPassengerItem> getEntityPassengers(){
		return entityPassengers;
	}

	public void setTotalTicketPrice(int totalTicketPrice){
		this.totalTicketPrice = totalTicketPrice;
	}

	public int getTotalTicketPrice(){
		return totalTicketPrice;
	}

	public void setEntityCategoryName(String entityCategoryName){
		this.entityCategoryName = entityCategoryName;
	}

	public String getEntityCategoryName(){
		return entityCategoryName;
	}

	public void setEntityGroupId(int entityGroupId){
		this.entityGroupId = entityGroupId;
	}

	public int getEntityGroupId(){
		return entityGroupId;
	}

	public void setOtherCharges(List<OtherChargesItem> otherCharges){
		this.otherCharges = otherCharges;
	}

	public List<OtherChargesItem> getOtherCharges(){
		return otherCharges;
	}

	public void setEntityAddress(EntityAddress entityAddress){
		this.entityAddress = entityAddress;
	}

	public EntityAddress getEntityAddress(){
		return entityAddress;
	}

	public void setTotalTaxAmount(int totalTaxAmount){
		this.totalTaxAmount = totalTaxAmount;
	}

	public int getTotalTaxAmount(){
		return totalTaxAmount;
	}

	public void setEntityImage(String entityImage){
		this.entityImage = entityImage;
	}

	public String getEntityImage(){
		return entityImage;
	}

	public void setEntityScheduleId(int entityScheduleId){
		this.entityScheduleId = entityScheduleId;
	}

	public int getEntityScheduleId(){
		return entityScheduleId;
	}

	public void setEntityPackages(List<EntityPackageItem> entityPackages){
		this.entityPackages = entityPackages;
	}

	public List<EntityPackageItem> getEntityPackages(){
		return entityPackages;
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
			"MetaData{" + 
			"entity_product_id = '" + entityProductId + '\'' + 
			",entity_end_time = '" + entityEndTime + '\'' + 
			",entity_start_time = '" + entityStartTime + '\'' + 
			",entity_category_id = '" + entityCategoryId + '\'' + 
			",city_searched = '" + citySearched + '\'' + 
			",tax_per_quantity = '" + taxPerQuantity + '\'' + 
			",total_other_charges = '" + totalOtherCharges + '\'' + 
			",entity_passengers = '" + entityPassengers + '\'' + 
			",total_ticket_price = '" + totalTicketPrice + '\'' + 
			",entity_category_name = '" + entityCategoryName + '\'' + 
			",entity_group_id = '" + entityGroupId + '\'' + 
			",other_charges = '" + otherCharges + '\'' + 
			",entity_address = '" + entityAddress + '\'' + 
			",total_tax_amount = '" + totalTaxAmount + '\'' + 
			",entity_image = '" + entityImage + '\'' + 
			",entity_schedule_id = '" + entityScheduleId + '\'' + 
			",entity_packages = '" + entityPackages + '\'' + 
			",total_ticket_count = '" + totalTicketCount + '\'' + 
			"}";
		}
}