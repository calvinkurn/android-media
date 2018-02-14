package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Aghny A. Putra on 25/01/18
 */
public class CartItemModel implements Parcelable {

    private String shopId;
    private String shopName;
    private String id;
    private String name;
    private double price;
    private int currency;

    private int weightUnit;
    private double weight;

    private int quantity;
    private String noteToSeller;

    private String imageUrl;

    private String cashback;

    private boolean isCashback;
    private boolean isPreOrder;
    private boolean isFreeReturn;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public int getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(int weightUnit) {
        this.weightUnit = weightUnit;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNoteToSeller() {
        return noteToSeller;
    }

    public void setNoteToSeller(String noteToSeller) {
        this.noteToSeller = noteToSeller;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }

    public boolean isCashback() {
        return isCashback;
    }

    public void setCashback(boolean cashback) {
        isCashback = cashback;
    }

    public boolean isPreOrder() {
        return isPreOrder;
    }

    public void setPreOrder(boolean preOrder) {
        isPreOrder = preOrder;
    }

    public boolean isFreeReturn() {
        return isFreeReturn;
    }

    public void setFreeReturn(boolean freeReturn) {
        isFreeReturn = freeReturn;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shopId);
        dest.writeString(this.shopName);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeDouble(this.price);
        dest.writeInt(this.currency);
        dest.writeInt(this.weightUnit);
        dest.writeDouble(this.weight);
        dest.writeInt(this.quantity);
        dest.writeString(this.noteToSeller);
        dest.writeString(this.imageUrl);
        dest.writeString(this.cashback);
        dest.writeByte(this.isCashback ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isPreOrder ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isFreeReturn ? (byte) 1 : (byte) 0);
    }

    public CartItemModel() {
    }

    protected CartItemModel(Parcel in) {
        this.shopId = in.readString();
        this.shopName = in.readString();
        this.id = in.readString();
        this.name = in.readString();
        this.price = in.readDouble();
        this.currency = in.readInt();
        this.weightUnit = in.readInt();
        this.weight = in.readDouble();
        this.quantity = in.readInt();
        this.noteToSeller = in.readString();
        this.imageUrl = in.readString();
        this.cashback = in.readString();
        this.isCashback = in.readByte() != 0;
        this.isPreOrder = in.readByte() != 0;
        this.isFreeReturn = in.readByte() != 0;
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
