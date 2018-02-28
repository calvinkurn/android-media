package com.tokopedia.events.domain.model.request.verify;

import com.google.gson.annotations.SerializedName;

public class ValidateShow{

	@SerializedName("quantity")
	private int quantity;

	@SerializedName("group_id")
	private int groupId;

	@SerializedName("product_id")
	private int productId;

	@SerializedName("package_id")
	private int packageId;

	@SerializedName("schedule_id")
	private int scheduleId;

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

	public void setProductId(int productId){
		this.productId = productId;
	}

	public int getProductId(){
		return productId;
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

	@Override
 	public String toString(){
		return 
			"ValidateShow{" + 
			"quantity = '" + quantity + '\'' + 
			",group_id = '" + groupId + '\'' + 
			",product_id = '" + productId + '\'' + 
			",package_id = '" + packageId + '\'' + 
			",schedule_id = '" + scheduleId + '\'' + 
			"}";
		}
}