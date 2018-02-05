package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Aghny A. Putra on 25/01/18
 */
public class CartItemModel implements Parcelable {

    private String senderName;
    private String productName;
    private String productPrice;
    private String productWeight;
    private String cashback;
    private String totalProductItem;
    private String noteToSeller;
    private String shipmentOption;
    private String totalPrice;

    private String productImageUrl;
    private boolean poAvailable;
    private boolean freeReturn;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
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

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }

    public String getTotalProductItem() {
        return totalProductItem;
    }

    public void setTotalProductItem(String totalProductItem) {
        this.totalProductItem = totalProductItem;
    }

    public String getNoteToSeller() {
        return noteToSeller;
    }

    public void setNoteToSeller(String noteToSeller) {
        this.noteToSeller = noteToSeller;
    }

    public String getShipmentOption() {
        return shipmentOption;
    }

    public void setShipmentOption(String shipmentOption) {
        this.shipmentOption = shipmentOption;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public boolean isPoAvailable() {
        return poAvailable;
    }

    public void setPoAvailable(boolean poAvailable) {
        this.poAvailable = poAvailable;
    }

    public boolean isFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(boolean freeReturn) {
        this.freeReturn = freeReturn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.senderName);
        dest.writeString(this.productName);
        dest.writeString(this.productPrice);
        dest.writeString(this.productWeight);
        dest.writeString(this.cashback);
        dest.writeString(this.totalProductItem);
        dest.writeString(this.noteToSeller);
        dest.writeString(this.shipmentOption);
        dest.writeString(this.totalPrice);
        dest.writeString(this.productImageUrl);
        dest.writeByte(this.poAvailable ? (byte) 1 : (byte) 0);
        dest.writeByte(this.freeReturn ? (byte) 1 : (byte) 0);
    }

    public CartItemModel() {
    }

    protected CartItemModel(Parcel in) {
        this.senderName = in.readString();
        this.productName = in.readString();
        this.productPrice = in.readString();
        this.productWeight = in.readString();
        this.cashback = in.readString();
        this.totalProductItem = in.readString();
        this.noteToSeller = in.readString();
        this.shipmentOption = in.readString();
        this.totalPrice = in.readString();
        this.productImageUrl = in.readString();
        this.poAvailable = in.readByte() != 0;
        this.freeReturn = in.readByte() != 0;
    }

    public static final Creator<CartItemModel> CREATOR = new Creator<CartItemModel>() {
        @Override
        public CartItemModel createFromParcel(Parcel source) {
            return new CartItemModel(source);
        }

        @Override
        public CartItemModel[] newArray(int size) {
            return new CartItemModel[size];
        }
    };
}
