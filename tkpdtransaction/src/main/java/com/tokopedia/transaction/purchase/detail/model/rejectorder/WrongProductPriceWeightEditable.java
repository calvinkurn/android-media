package com.tokopedia.transaction.purchase.detail.model.rejectorder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kris on 1/11/18. Tokopedia
 */

public class WrongProductPriceWeightEditable implements Parcelable{

    private String orderId;

    private String shopId;

    private String productId;

    private String productName;

    private String productImage;

    private String productPrice;

    private String productWeight;

    private int weightMode;

    private int currencyMode;

    private String productPriceUnformatted;

    private String productWeightUnformatted;

    public WrongProductPriceWeightEditable() {

    }

    protected WrongProductPriceWeightEditable(Parcel in) {
        orderId = in.readString();
        shopId = in.readString();
        productId = in.readString();
        productName = in.readString();
        productImage = in.readString();
        productPrice = in.readString();
        productWeight = in.readString();
        weightMode = in.readInt();
        currencyMode = in.readInt();
        productPriceUnformatted = in.readString();
        productWeightUnformatted = in.readString();
    }

    public static final Creator<WrongProductPriceWeightEditable> CREATOR = new Creator<WrongProductPriceWeightEditable>() {
        @Override
        public WrongProductPriceWeightEditable createFromParcel(Parcel in) {
            return new WrongProductPriceWeightEditable(in);
        }

        @Override
        public WrongProductPriceWeightEditable[] newArray(int size) {
            return new WrongProductPriceWeightEditable[size];
        }
    };

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
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

    public int getWeightMode() {
        return weightMode;
    }

    public void setWeightMode(int weightMode) {
        this.weightMode = weightMode;
    }

    public int getCurrencyMode() {
        return currencyMode;
    }

    public void setCurrencyMode(int currencyMode) {
        this.currencyMode = currencyMode;
    }

    public String getProductPriceUnformatted() {
        return productPriceUnformatted;
    }

    public void setProductPriceUnformatted(String productPriceUnformatted) {
        this.productPriceUnformatted = productPriceUnformatted;
    }

    public String getProductWeightUnformatted() {
        return productWeightUnformatted;
    }

    public void setProductWeightUnformatted(String productWeightUnformatted) {
        this.productWeightUnformatted = productWeightUnformatted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productId);
        parcel.writeString(productName);
        parcel.writeString(productImage);
        parcel.writeString(productPrice);
        parcel.writeString(productWeight);
        parcel.writeInt(weightMode);
        parcel.writeInt(currencyMode);
        parcel.writeString(productPriceUnformatted);
        parcel.writeString(productWeightUnformatted);
    }
}
