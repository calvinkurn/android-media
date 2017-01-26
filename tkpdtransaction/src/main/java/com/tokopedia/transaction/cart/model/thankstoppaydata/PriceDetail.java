package com.tokopedia.transaction.cart.model.thankstoppaydata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 1/23/17.
 */

public class PriceDetail implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("id")
    @Expose
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.quantity);
        dest.writeString(this.price);
        dest.writeString(this.id);
    }

    public PriceDetail() {
    }

    protected PriceDetail(Parcel in) {
        this.name = in.readString();
        this.quantity = in.readString();
        this.price = in.readString();
        this.id = in.readString();
    }

    public static final Parcelable.Creator<PriceDetail> CREATOR
            = new Parcelable.Creator<PriceDetail>() {
        @Override
        public PriceDetail createFromParcel(Parcel source) {
            return new PriceDetail(source);
        }

        @Override
        public PriceDetail[] newArray(int size) {
            return new PriceDetail[size];
        }
    };
}
