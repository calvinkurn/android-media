package com.tokopedia.events.domain.model.request.cart;

import com.google.gson.annotations.SerializedName;

public class EntityPassengerItem {

	@SerializedName("error_message")
	private String errorMessage;

	@SerializedName("product_id")
	private int productId;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("element_type")
	private String elementType;

	@SerializedName("title")
	private String title;

	@SerializedName("value")
	private String value;

	@SerializedName("validator_regex")
	private String validatorRegex;

	@SerializedName("required")
	private String required;

	public void setErrorMessage(String errorMessage){
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage(){
		return errorMessage;
	}

	public void setProductId(int productId){
		this.productId = productId;
	}

	public int getProductId(){
		return productId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setElementType(String elementType){
		this.elementType = elementType;
	}

	public String getElementType(){
		return elementType;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setValue(String value){
		this.value = value;
	}

	public String getValue(){
		return value;
	}

	public void setValidatorRegex(String validatorRegex){
		this.validatorRegex = validatorRegex;
	}

	public String getValidatorRegex(){
		return validatorRegex;
	}

	public void setRequired(String required){
		this.required = required;
	}

	public String getRequired(){
		return required;
	}

	@Override
 	public String toString(){
		return 
			"EntityPassengerItem{" +
			"error_message = '" + errorMessage + '\'' + 
			",product_id = '" + productId + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",element_type = '" + elementType + '\'' + 
			",title = '" + title + '\'' + 
			",value = '" + value + '\'' + 
			",validator_regex = '" + validatorRegex + '\'' + 
			",required = '" + required + '\'' + 
			"}";
		}
}