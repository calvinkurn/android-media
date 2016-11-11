package com.tokopedia.transaction.cart.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 11/2/16.
 */

public class ShipmentCartPassData implements Parcelable {

    private String weight;
    private String shopId;
    private String addressId;
    private String quantity;
    private String shippingId;
    private String shippingPackageId;
    private String addressTitle;
    private String addressName;
    private String latitude;
    private String longitude;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getShippingId() {
        return shippingId;
    }

    public void setShippingId(String shippingId) {
        this.shippingId = shippingId;
    }

    public String getShippingPackageId() {
        return shippingPackageId;
    }

    public void setShippingPackageId(String shippingPackageId) {
        this.shippingPackageId = shippingPackageId;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.weight);
        dest.writeString(this.shopId);
        dest.writeString(this.addressId);
        dest.writeString(this.quantity);
        dest.writeString(this.shippingId);
        dest.writeString(this.shippingPackageId);
        dest.writeString(this.addressTitle);
        dest.writeString(this.addressName);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
    }

    public ShipmentCartPassData() {
    }

    protected ShipmentCartPassData(Parcel in) {
        this.weight = in.readString();
        this.shopId = in.readString();
        this.addressId = in.readString();
        this.quantity = in.readString();
        this.shippingId = in.readString();
        this.shippingPackageId = in.readString();
        this.addressTitle = in.readString();
        this.addressName = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
    }

    public static final Creator<ShipmentCartPassData> CREATOR =
            new Creator<ShipmentCartPassData>() {
                @Override
                public ShipmentCartPassData createFromParcel(Parcel source) {
                    return new ShipmentCartPassData(source);
                }

                @Override
                public ShipmentCartPassData[] newArray(int size) {
                    return new ShipmentCartPassData[size];
                }
            };


}
