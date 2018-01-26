package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kris on 1/24/18. Tokopedia
 */

public class MultipleAddressItemData implements Parcelable{

    private String productWeight = "";

    private String productQty = "";

    private String productNotes = "";

    private String addressTitle = "";

    private String addressReceiverName = "";

    private String address = "";

    public MultipleAddressItemData() {
    }

    protected MultipleAddressItemData(Parcel in) {
        productWeight = in.readString();
        productQty = in.readString();
        productNotes = in.readString();
        addressTitle = in.readString();
        addressReceiverName = in.readString();
        address = in.readString();
    }

    public static final Creator<MultipleAddressItemData> CREATOR = new Creator<MultipleAddressItemData>() {
        @Override
        public MultipleAddressItemData createFromParcel(Parcel in) {
            return new MultipleAddressItemData(in);
        }

        @Override
        public MultipleAddressItemData[] newArray(int size) {
            return new MultipleAddressItemData[size];
        }
    };

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    public String getProductNotes() {
        return productNotes;
    }

    public void setProductNotes(String productNotes) {
        this.productNotes = productNotes;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public String getAddressReceiverName() {
        return addressReceiverName;
    }

    public void setAddressReceiverName(String addressReceiverName) {
        this.addressReceiverName = addressReceiverName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productWeight);
        parcel.writeString(productQty);
        parcel.writeString(productNotes);
        parcel.writeString(addressTitle);
        parcel.writeString(addressReceiverName);
        parcel.writeString(address);
    }
}
