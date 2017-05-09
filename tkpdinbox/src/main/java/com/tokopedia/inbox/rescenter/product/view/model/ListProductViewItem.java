package com.tokopedia.inbox.rescenter.product.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hangnadi on 3/24/17.
 */

@SuppressWarnings("ALL")
public class ListProductViewItem implements Parcelable {

    private String productImageUrl;
    private String productName;
    private String resCenterProductID;

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

    public String getResCenterProductID() {
        return resCenterProductID;
    }

    public void setResCenterProductID(String resCenterProductID) {
        this.resCenterProductID = resCenterProductID;
    }

    public ListProductViewItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productImageUrl);
        dest.writeString(this.productName);
        dest.writeString(this.resCenterProductID);
    }

    protected ListProductViewItem(Parcel in) {
        this.productImageUrl = in.readString();
        this.productName = in.readString();
        this.resCenterProductID = in.readString();
    }

    public static final Creator<ListProductViewItem> CREATOR = new Creator<ListProductViewItem>() {
        @Override
        public ListProductViewItem createFromParcel(Parcel source) {
            return new ListProductViewItem(source);
        }

        @Override
        public ListProductViewItem[] newArray(int size) {
            return new ListProductViewItem[size];
        }
    };
}
