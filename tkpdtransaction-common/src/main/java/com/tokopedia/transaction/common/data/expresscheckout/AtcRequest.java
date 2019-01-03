package com.tokopedia.transaction.common.data.expresscheckout;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 03/01/19.
 */

public class AtcRequest implements Parcelable {
    @SerializedName("shop_id")
    public int shopId;

    @SerializedName("quantity")
    public int quantity;

    @SerializedName("notes")
    public String notes;

    @SerializedName("product_id")
    public int productId;

    public AtcRequest() {
    }

    protected AtcRequest(Parcel in) {
        shopId = in.readInt();
        quantity = in.readInt();
        notes = in.readString();
        productId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(shopId);
        dest.writeInt(quantity);
        dest.writeString(notes);
        dest.writeInt(productId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AtcRequest> CREATOR = new Creator<AtcRequest>() {
        @Override
        public AtcRequest createFromParcel(Parcel in) {
            return new AtcRequest(in);
        }

        @Override
        public AtcRequest[] newArray(int size) {
            return new AtcRequest[size];
        }
    };

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
