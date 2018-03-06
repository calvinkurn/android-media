package com.tokopedia.events.data.entity.response.verifyresponse;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MetaData{

	@SerializedName("end_date")
	private String endDate;

	@SerializedName("entity_product_id")
	private int entityProductId;

	@SerializedName("entity_provider_id")
	private int entityProviderId;

	@SerializedName("entity_category_id")
	private int entityCategoryId;

	@SerializedName("tax_per_quantity")
	private List<Object> taxPerQuantity;

	@SerializedName("use_pdf")
	private int usePdf;

	@SerializedName("total_other_charges")
	private int totalOtherCharges;

	@SerializedName("total_ticket_price")
	private int totalTicketPrice;

	@SerializedName("other_charges")
	private List<OtherChargesItem> otherCharges;

	@SerializedName("entity_image")
	private String entityImage;

	@SerializedName("links")
	private String links;

	@SerializedName("total_tax_amount")
	private int totalTaxAmount;

	@SerializedName("entity_schedule_id")
	private int entityScheduleId;

	@SerializedName("entity_packages")
	private List<EntityPackagesItem> entityPackages;

	@SerializedName("start_date")
	private String startDate;

	@SerializedName("total_ticket_count")
	private int totalTicketCount;

	@SerializedName("entity_end_time")
	private String entityEndTime;

	@SerializedName("entity_start_time")
	private String entityStartTime;

	@SerializedName("city_searched")
	private String citySearched;

	@SerializedName("entity_passengers")
	private List<EntityPassengersItem> entityPassengers;

	@SerializedName("entity_category_name")
	private String entityCategoryName;

	@SerializedName("entity_group_id")
	private int entityGroupId;

	@SerializedName("entity_invoice_id")
	private int entityInvoiceId;

	@SerializedName("entity_address")
	private EntityAddress entityAddress;

	@SerializedName("entity_product_name")
	private String entityProductName;

	public void setEndDate(String endDate){
		this.endDate = endDate;
	}

	public String getEndDate(){
		return endDate;
	}

	public void setEntityProductId(int entityProductId){
		this.entityProductId = entityProductId;
	}

	public int getEntityProductId(){
		return entityProductId;
	}

	public void setEntityProviderId(int entityProviderId){
		this.entityProviderId = entityProviderId;
	}

	public int getEntityProviderId(){
		return entityProviderId;
	}

	public void setEntityCategoryId(int entityCategoryId){
		this.entityCategoryId = entityCategoryId;
	}

	public int getEntityCategoryId(){
		return entityCategoryId;
	}

	public void setTaxPerQuantity(List<Object> taxPerQuantity){
		this.taxPerQuantity = taxPerQuantity;
	}

	public List<Object> getTaxPerQuantity(){
		return taxPerQuantity;
	}

	public void setUsePdf(int usePdf){
		this.usePdf = usePdf;
	}

	public int getUsePdf(){
		return usePdf;
	}

	public void setTotalOtherCharges(int totalOtherCharges){
		this.totalOtherCharges = totalOtherCharges;
	}

	public int getTotalOtherCharges(){
		return totalOtherCharges;
	}

	public void setTotalTicketPrice(int totalTicketPrice){
		this.totalTicketPrice = totalTicketPrice;
	}

	public int getTotalTicketPrice(){
		return totalTicketPrice;
	}

	public void setOtherCharges(List<OtherChargesItem> otherCharges){
		this.otherCharges = otherCharges;
	}

	public List<OtherChargesItem> getOtherCharges(){
		return otherCharges;
	}

	public void setEntityImage(String entityImage){
		this.entityImage = entityImage;
	}

	public String getEntityImage(){
		return entityImage;
	}

	public void setLinks(String links){
		this.links = links;
	}

	public String getLinks(){
		return links;
	}

	public void setTotalTaxAmount(int totalTaxAmount){
		this.totalTaxAmount = totalTaxAmount;
	}

	public int getTotalTaxAmount(){
		return totalTaxAmount;
	}

	public void setEntityScheduleId(int entityScheduleId){
		this.entityScheduleId = entityScheduleId;
	}

	public int getEntityScheduleId(){
		return entityScheduleId;
	}

	public void setEntityPackages(List<EntityPackagesItem> entityPackages){
		this.entityPackages = entityPackages;
	}

	public List<EntityPackagesItem> getEntityPackages(){
		return entityPackages;
	}

	public void setStartDate(String startDate){
		this.startDate = startDate;
	}

	public String getStartDate(){
		return startDate;
	}

	public void setTotalTicketCount(int totalTicketCount){
		this.totalTicketCount = totalTicketCount;
	}

	public int getTotalTicketCount(){
		return totalTicketCount;
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

	public void setCitySearched(String citySearched){
		this.citySearched = citySearched;
	}

	public String getCitySearched(){
		return citySearched;
	}

	public void setEntityPassengers(List<EntityPassengersItem> entityPassengers){
		this.entityPassengers = entityPassengers;
	}

	public List<EntityPassengersItem> getEntityPassengers(){
		return entityPassengers;
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

	public void setEntityInvoiceId(int entityInvoiceId){
		this.entityInvoiceId = entityInvoiceId;
	}

	public int getEntityInvoiceId(){
		return entityInvoiceId;
	}

	public void setEntityAddress(EntityAddress entityAddress){
		this.entityAddress = entityAddress;
	}

	public EntityAddress getEntityAddress(){
		return entityAddress;
	}

	public void setEntityProductName(String entityProductName){
		this.entityProductName = entityProductName;
	}

	public String getEntityProductName(){
		return entityProductName;
	}

	@Override
 	public String toString(){
		return 
			"MetaData{" + 
			"end_date = '" + endDate + '\'' + 
			",entity_product_id = '" + entityProductId + '\'' + 
			",entity_provider_id = '" + entityProviderId + '\'' + 
			",entity_category_id = '" + entityCategoryId + '\'' + 
			",tax_per_quantity = '" + taxPerQuantity + '\'' + 
			",use_pdf = '" + usePdf + '\'' + 
			",total_other_charges = '" + totalOtherCharges + '\'' + 
			",total_ticket_price = '" + totalTicketPrice + '\'' + 
			",other_charges = '" + otherCharges + '\'' + 
			",entity_image = '" + entityImage + '\'' + 
			",links = '" + links + '\'' + 
			",total_tax_amount = '" + totalTaxAmount + '\'' + 
			",entity_schedule_id = '" + entityScheduleId + '\'' + 
			",entity_packages = '" + entityPackages + '\'' + 
			",start_date = '" + startDate + '\'' + 
			",total_ticket_count = '" + totalTicketCount + '\'' + 
			",entity_end_time = '" + entityEndTime + '\'' + 
			",entity_start_time = '" + entityStartTime + '\'' + 
			",city_searched = '" + citySearched + '\'' + 
			",entity_passengers = '" + entityPassengers + '\'' + 
			",entity_category_name = '" + entityCategoryName + '\'' + 
			",entity_group_id = '" + entityGroupId + '\'' + 
			",entity_invoice_id = '" + entityInvoiceId + '\'' + 
			",entity_address = '" + entityAddress + '\'' + 
			",entity_product_name = '" + entityProductName + '\'' + 
			"}";
		}
}