package com.tokopedia.events.domain.model.request.cart;

import com.google.gson.annotations.SerializedName;

public class CartItem {

	@SerializedName("quantity")
	private int quantity;

	@SerializedName("configuration")
	private Configuration configuration;

	@SerializedName("product_id")
	private int productId;

	@SerializedName("meta_data")
	private MetaData metaData;

	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	public int getQuantity(){
		return quantity;
	}

	public void setConfiguration(Configuration configuration){
		this.configuration = configuration;
	}

	public Configuration getConfiguration(){
		return configuration;
	}

	public void setProductId(int productId){
		this.productId = productId;
	}

	public int getProductId(){
		return productId;
	}

	public void setMetaData(MetaData metaData){
		this.metaData = metaData;
	}

	public MetaData getMetaData(){
		return metaData;
	}

	@Override
 	public String toString(){
		return 
			"CartItem{" +
			"quantity = '" + quantity + '\'' + 
			",configuration = '" + configuration + '\'' + 
			",product_id = '" + productId + '\'' + 
			",meta_data = '" + metaData + '\'' + 
			"}";
		}
}