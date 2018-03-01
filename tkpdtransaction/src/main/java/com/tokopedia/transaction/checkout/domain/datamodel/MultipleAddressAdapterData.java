package com.tokopedia.transaction.checkout.domain.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by kris on 1/23/18. Tokopedia
 */

public class MultipleAddressAdapterData implements Parcelable{

    private String senderName;

    private String productImageUrl;

    private String productName;

    private String productPrice;

    private List<MultipleAddressItemData> itemListData;

    public MultipleAddressAdapterData() {
    }

    protected MultipleAddressAdapterData(Parcel in) {
        senderName = in.readString();
        productImageUrl = in.readString();
        productName = in.readString();
        productPrice = in.readString();
    }

    public static final Creator<MultipleAddressAdapterData> CREATOR = new Creator<MultipleAddressAdapterData>() {
        @Override
        public MultipleAddressAdapterData createFromParcel(Parcel in) {
            return new MultipleAddressAdapterData(in);
        }

        @Override
        public MultipleAddressAdapterData[] newArray(int size) {
            return new MultipleAddressAdapterData[size];
        }
    };

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
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

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public List<MultipleAddressItemData> getItemListData() {
        return itemListData;
    }

    public void setItemListData(List<MultipleAddressItemData> itemListData) {
        this.itemListData = itemListData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(senderName);
        parcel.writeString(productImageUrl);
        parcel.writeString(productName);
        parcel.writeString(productPrice);
    }
}
