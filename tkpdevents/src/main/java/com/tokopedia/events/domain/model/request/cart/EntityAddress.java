package com.tokopedia.events.domain.model.request.cart;

import com.google.gson.annotations.SerializedName;

public class EntityAddress{

	@SerializedName("address")
	private String address;

	@SerializedName("city")
	private String city;

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("mobile_number")
	private String mobileNumber;

	@SerializedName("email")
	private String email;

	@SerializedName("Name")
	private String name;

	@SerializedName("longitude")
	private String longitude;

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setMobileNumber(String mobileNumber){
		this.mobileNumber = mobileNumber;
	}

	public String getMobileNumber(){
		return mobileNumber;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setLongitude(String longitude){
		this.longitude = longitude;
	}

	public String getLongitude(){
		return longitude;
	}

	@Override
 	public String toString(){
		return 
			"EntityAddress{" + 
			"address = '" + address + '\'' + 
			",city = '" + city + '\'' + 
			",latitude = '" + latitude + '\'' + 
			",mobile_number = '" + mobileNumber + '\'' + 
			",email = '" + email + '\'' + 
			",name = '" + name + '\'' + 
			",longitude = '" + longitude + '\'' + 
			"}";
		}
}