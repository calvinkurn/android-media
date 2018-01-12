package com.tokopedia.transaction.purchase.detail.model.rejectorder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kris on 1/11/18. Tokopedia
 */

public class WrongProdcutPriceWeightEditable implements Parcelable{

    private String productId;

    private String productName;

    private String productImage;

    private String productPrice;

    private String productWeight;

    private int weightMode;

    private int currencyMode;

    public WrongProdcutPriceWeightEditable() {

    }

    protected WrongProdcutPriceWeightEditable(Parcel in) {
        productId = in.readString();
        productName = in.readString();
        productImage = in.readString();
        productPrice = in.readString();
        productWeight = in.readString();
        weightMode = in.readInt();
        currencyMode = in.readInt();
    }

    public static final Creator<WrongProdcutPriceWeightEditable> CREATOR = new Creator<WrongProdcutPriceWeightEditable>() {
        @Override
        public WrongProdcutPriceWeightEditable createFromParcel(Parcel in) {
            return new WrongProdcutPriceWeightEditable(in);
        }

        @Override
        public WrongProdcutPriceWeightEditable[] newArray(int size) {
            return new WrongProdcutPriceWeightEditable[size];
        }
    };

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
    }
}
