package com.tokopedia.events.domain.model.request.cart;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class EntityAddress implements Parcelable {

	@SerializedName("address")
	private String address;

	@SerializedName("city")
	private String city;

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("mobile")
	private String mobile;

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

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return mobile;
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
			",mobile_number = '" + mobile + '\'' +
			",email = '" + email + '\'' + 
			",name = '" + name + '\'' + 
			",longitude = '" + longitude + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.address);
		dest.writeString(this.city);
		dest.writeString(this.latitude);
		dest.writeString(this.mobile);
		dest.writeString(this.email);
		dest.writeString(this.name);
		dest.writeString(this.longitude);
	}

	public EntityAddress() {
	}

	protected EntityAddress(Parcel in) {
		this.address = in.readString();
		this.city = in.readString();
		this.latitude = in.readString();
		this.mobile = in.readString();
		this.email = in.readString();
		this.name = in.readString();
		this.longitude = in.readString();
	}

	public static final Parcelable.Creator<EntityAddress> CREATOR = new Parcelable.Creator<EntityAddress>() {
		@Override
		public EntityAddress createFromParcel(Parcel source) {
			return new EntityAddress(source);
		}

		@Override
		public EntityAddress[] newArray(int size) {
			return new EntityAddress[size];
		}
	};
}