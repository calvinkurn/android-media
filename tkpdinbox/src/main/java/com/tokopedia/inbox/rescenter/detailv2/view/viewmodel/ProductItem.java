package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hangnadi on 3/10/17.
 */

public class ProductItem implements Parcelable {

    public static final Parcelable.Creator<ProductItem> CREATOR = new Parcelable.Creator<ProductItem>() {
        @Override
        public ProductItem createFromParcel(Parcel source) {
            return new ProductItem(source);
        }

        @Override
        public ProductItem[] newArray(int size) {
            return new ProductItem[size];
        }
    };
    String productImageUrl;
    String productName;
    private String productID;

    public ProductItem() {
    }

    protected ProductItem(Parcel in) {
        this.productImageUrl = in.readString();
        this.productName = in.readString();
        this.productID = in.readString();
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productImageUrl);
        dest.writeString(this.productName);
        dest.writeString(this.productID);
    }
}
