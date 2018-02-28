package com.tokopedia.transaction.checkout.domain.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kris on 1/24/18. Tokopedia
 */

public class MultipleAddressItemData implements Parcelable{

    private String cartId = "";

    private String productId = "";

    private String productWeight = "";

    private String productQty = "";

    private String productNotes = "";

    private String addressId = "";

    private String addressTitle = "";

    private String addressReceiverName = "";

    private String address = "";

    private int maxQuantity;

    private int minQuantity;

    public MultipleAddressItemData() {
    }

    protected MultipleAddressItemData(Parcel in) {
        cartId = in.readString();
        productId = in.readString();
        productWeight = in.readString();
        productQty = in.readString();
        productNotes = in.readString();
        addressId = in.readString();
        addressTitle = in.readString();
        addressReceiverName = in.readString();
        address = in.readString();
        maxQuantity = in.readInt();
        minQuantity = in.readInt();
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

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

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

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
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

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(cartId);
        parcel.writeString(productId);
        parcel.writeString(productWeight);
        parcel.writeString(productQty);
        parcel.writeString(productNotes);
        parcel.writeString(addressId);
        parcel.writeString(addressTitle);
        parcel.writeString(addressReceiverName);
        parcel.writeString(address);
        parcel.writeInt(maxQuantity);
        parcel.writeInt(minQuantity);
    }
}
