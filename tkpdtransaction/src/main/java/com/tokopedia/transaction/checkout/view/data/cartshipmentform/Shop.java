package com.tokopedia.transaction.checkout.view.data.cartshipmentform;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class Shop implements Parcelable {
    private int shopId;
    private int userId;
    private String shopName;
    private String shopImage;
    private String shopUrl;
    private int shopStatus;
    private boolean isGold;
    private boolean isGoldBadge;
    private boolean isOfficial;
    private boolean isFreeReturns;
    private int addressId;
    private String postalCode;
    private String latitude;
    private String longitude;
    private int districtId;
    private String districtName;
    private int origin;
    private String addressStreet;
    private int provinceId;
    private int cityId;
    private String cityName;

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public int getShopStatus() {
        return shopStatus;
    }

    public void setShopStatus(int shopStatus) {
        this.shopStatus = shopStatus;
    }

    public boolean isGold() {
        return isGold;
    }

    public void setGold(boolean gold) {
        isGold = gold;
    }

    public boolean isGoldBadge() {
        return isGoldBadge;
    }

    public void setGoldBadge(boolean goldBadge) {
        isGoldBadge = goldBadge;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setOfficial(boolean official) {
        isOfficial = official;
    }

    public boolean isFreeReturns() {
        return isFreeReturns;
    }

    public void setFreeReturns(boolean freeReturns) {
        isFreeReturns = freeReturns;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.shopId);
        dest.writeInt(this.userId);
        dest.writeString(this.shopName);
        dest.writeString(this.shopImage);
        dest.writeString(this.shopUrl);
        dest.writeInt(this.shopStatus);
        dest.writeByte(this.isGold ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isGoldBadge ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isOfficial ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isFreeReturns ? (byte) 1 : (byte) 0);
        dest.writeInt(this.addressId);
        dest.writeString(this.postalCode);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeInt(this.districtId);
        dest.writeString(this.districtName);
        dest.writeInt(this.origin);
        dest.writeString(this.addressStreet);
        dest.writeInt(this.provinceId);
        dest.writeInt(this.cityId);
        dest.writeString(this.cityName);
    }

    public Shop() {
    }

    protected Shop(Parcel in) {
        this.shopId = in.readInt();
        this.userId = in.readInt();
        this.shopName = in.readString();
        this.shopImage = in.readString();
        this.shopUrl = in.readString();
        this.shopStatus = in.readInt();
        this.isGold = in.readByte() != 0;
        this.isGoldBadge = in.readByte() != 0;
        this.isOfficial = in.readByte() != 0;
        this.isFreeReturns = in.readByte() != 0;
        this.addressId = in.readInt();
        this.postalCode = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.districtId = in.readInt();
        this.districtName = in.readString();
        this.origin = in.readInt();
        this.addressStreet = in.readString();
        this.provinceId = in.readInt();
        this.cityId = in.readInt();
        this.cityName = in.readString();
    }

    public static final Parcelable.Creator<Shop> CREATOR = new Parcelable.Creator<Shop>() {
        @Override
        public Shop createFromParcel(Parcel source) {
            return new Shop(source);
        }

        @Override
        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };
}
