package com.tokopedia.events.data.entity.response.verifyresponse;

import com.google.gson.annotations.SerializedName;

public class EntityPassengersItem{

	@SerializedName("error_message")
	private String errorMessage;

	@SerializedName("product_id")
	private int productId;

	@SerializedName("name")
	private String name;

	@SerializedName("element_type")
	private String elementType;

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private String title;

	@SerializedName("validator_regex")
	private String validatorRegex;

	@SerializedName("value")
	private String value;

	@SerializedName("required")
	private String required;

	@SerializedName("help_text")
	private String helpText;

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

	public void setElementType(String elementType){
		this.elementType = elementType;
	}

	public String getElementType(){
		return elementType;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setValidatorRegex(String validatorRegex){
		this.validatorRegex = validatorRegex;
	}

	public String getValidatorRegex(){
		return validatorRegex;
	}

	public void setValue(String value){
		this.value = value;
	}

	public String getValue(){
		return value;
	}

	public void setRequired(String required){
		this.required = required;
	}

	public String getRequired(){
		return required;
	}

	public void setHelpText(String helpText){
		this.helpText = helpText;
	}

	public String getHelpText(){
		return helpText;
	}

	@Override
 	public String toString(){
		return 
			"EntityPassengersItem{" + 
			"error_message = '" + errorMessage + '\'' + 
			",product_id = '" + productId + '\'' + 
			",name = '" + name + '\'' + 
			",element_type = '" + elementType + '\'' + 
			",id = '" + id + '\'' + 
			",title = '" + title + '\'' + 
			",validator_regex = '" + validatorRegex + '\'' + 
			",value = '" + value + '\'' + 
			",required = '" + required + '\'' + 
			",help_text = '" + helpText + '\'' + 
			"}";
		}
}