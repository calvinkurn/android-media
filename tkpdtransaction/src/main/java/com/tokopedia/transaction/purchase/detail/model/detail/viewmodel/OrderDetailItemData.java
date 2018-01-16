package com.tokopedia.transaction.purchase.detail.model.detail.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kris on 11/2/17. Tokopedia
 */

public class OrderDetailItemData implements Parcelable{

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

    public OrderDetailItemData() {
    }

    protected OrderDetailItemData(Parcel in) {
        productId = in.readString();
        orderDetailId = in.readString();
        itemName = in.readString();
        itemQuantity = in.readString();
        price = in.readString();
        weight = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        currencyType = in.readInt();
        currencyRate = in.readInt();
        priceUnformatted = in.readString();
        weightUnformatted = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(orderDetailId);
        dest.writeString(itemName);
        dest.writeString(itemQuantity);
        dest.writeString(price);
        dest.writeString(weight);
        dest.writeString(description);
        dest.writeString(imageUrl);
        dest.writeInt(currencyType);
        dest.writeInt(currencyRate);
        dest.writeString(priceUnformatted);
        dest.writeString(weightUnformatted);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderDetailItemData> CREATOR = new Creator<OrderDetailItemData>() {
        @Override
        public OrderDetailItemData createFromParcel(Parcel in) {
            return new OrderDetailItemData(in);
        }

        @Override
        public OrderDetailItemData[] newArray(int size) {
            return new OrderDetailItemData[size];
        }
    };

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
}
