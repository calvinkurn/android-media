package com.tokopedia.transaction.cart.model.cartdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CartShop implements Parcelable {

    @SerializedName("shop_pay_gateway")
    @Expose
    private List<Object> shopPayGateway = new ArrayList<Object>();
    @SerializedName("shop_url")
    @Expose
    private String shopUrl;
    @SerializedName("shop_id")
    @Expose
    private String shopId;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("shop_image")
    @Expose
    private String shopImage;
    @SerializedName("shop_status")
    @Expose
    private Integer shopStatus;
    @SerializedName("lucky_merchant")
    @Expose
    private Integer luckyMerchant;
    @SerializedName("address_id")
    @Expose
    private String addressId;
    @SerializedName("postal_code")
    @Expose
    private String postalCode;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("shop_type")
    @Expose
    private String shopType;

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
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

    public Integer getShopStatus() {
        return shopStatus;
    }

    public void setShopStatus(Integer shopStatus) {
        this.shopStatus = shopStatus;
    }

    public Integer getLuckyMerchant() {
        return luckyMerchant;
    }

    public void setLuckyMerchant(Integer luckyMerchant) {
        this.luckyMerchant = luckyMerchant;
    }

    public List<Object> getShopPayGateway() {
        return shopPayGateway;
    }

    public void setShopPayGateway(List<Object> shopPayGateway) {
        this.shopPayGateway = shopPayGateway;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
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

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.shopPayGateway);
        dest.writeString(this.shopUrl);
        dest.writeString(this.shopId);
        dest.writeString(this.shopName);
        dest.writeString(this.shopImage);
        dest.writeValue(this.shopStatus);
        dest.writeValue(this.luckyMerchant);
        dest.writeString(this.addressId);
        dest.writeString(this.postalCode);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.shopType);
    }

    public CartShop() {
    }

    protected CartShop(Parcel in) {
        this.shopPayGateway = new ArrayList<Object>();
        in.readList(this.shopPayGateway, Object.class.getClassLoader());
        this.shopUrl = in.readString();
        this.shopId = in.readString();
        this.shopName = in.readString();
        this.shopImage = in.readString();
        this.shopStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.luckyMerchant = (Integer) in.readValue(Integer.class.getClassLoader());
        this.addressId = in.readString();
        this.postalCode = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.shopType = in.readString();
    }

    public static final Creator<CartShop> CREATOR = new Creator<CartShop>() {
        @Override
        public CartShop createFromParcel(Parcel source) {
            return new CartShop(source);
        }

        @Override
        public CartShop[] newArray(int size) {
            return new CartShop[size];
        }
    };
}
