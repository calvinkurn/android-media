package com.tokopedia.transaction.common.data.order;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kris on 11/2/17. Tokopedia
 */

public class OrderDetailItemData implements Parcelable {

    private String productId;

    private String orderDetailId;

    private String itemName;

    private String itemQuantity;

    private String price;

    private String weight;

    private String description;

    private String imageUrl;

    private int currencyType;

    private int currencyRate;

    private String priceUnformatted;

    private String weightUnformatted;
    private String notes;

    public OrderDetailItemData() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(int currencyType) {
        this.currencyType = currencyType;
    }

    public int getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(int currencyRate) {
        this.currencyRate = currencyRate;
    }

    public String getPriceUnformatted() {
        return priceUnformatted;
    }

    public void setPriceUnformatted(String priceUnformatted) {
        this.priceUnformatted = priceUnformatted;
    }

    public String getWeightUnformatted() {
        return weightUnformatted;
    }

    public void setWeightUnformatted(String weightUnformatted) {
        this.weightUnformatted = weightUnformatted;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productId);
        dest.writeString(this.orderDetailId);
        dest.writeString(this.itemName);
        dest.writeString(this.itemQuantity);
        dest.writeString(this.price);
        dest.writeString(this.weight);
        dest.writeString(this.description);
        dest.writeString(this.imageUrl);
        dest.writeInt(this.currencyType);
        dest.writeInt(this.currencyRate);
        dest.writeString(this.priceUnformatted);
        dest.writeString(this.weightUnformatted);
        dest.writeString(this.notes);
    }

    protected OrderDetailItemData(Parcel in) {
        this.productId = in.readString();
        this.orderDetailId = in.readString();
        this.itemName = in.readString();
        this.itemQuantity = in.readString();
        this.price = in.readString();
        this.weight = in.readString();
        this.description = in.readString();
        this.imageUrl = in.readString();
        this.currencyType = in.readInt();
        this.currencyRate = in.readInt();
        this.priceUnformatted = in.readString();
        this.weightUnformatted = in.readString();
        this.notes = in.readString();
    }

    public static final Creator<OrderDetailItemData> CREATOR = new Creator<OrderDetailItemData>() {
        @Override
        public OrderDetailItemData createFromParcel(Parcel source) {
            return new OrderDetailItemData(source);
        }

        @Override
        public OrderDetailItemData[] newArray(int size) {
            return new OrderDetailItemData[size];
        }
    };
}
