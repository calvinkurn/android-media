package com.tokopedia.discovery.similarsearch.model;

import com.google.gson.annotations.SerializedName;

public class Shop{

	@SerializedName("city")
	private String city;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("is_gold_shop")
	private boolean isGoldShop;

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
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

	public boolean isGoldShop() {
		return isGoldShop;
	}

	public void setGoldShop(boolean goldShop) {
		isGoldShop = goldShop;
	}

	@Override
 	public String toString(){
		return 
			"Shop{" + 
			"city = '" + city + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}