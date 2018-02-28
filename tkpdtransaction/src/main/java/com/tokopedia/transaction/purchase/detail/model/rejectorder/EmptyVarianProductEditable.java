package com.tokopedia.transaction.purchase.detail.model.rejectorder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kris on 1/11/18. Tokopedia
 */

public class EmptyVarianProductEditable implements Parcelable{

    private String orderId;

    private String shopId;

    private String productId;

    private String productName;

    private String productPrice;

    private String productImage;

    private String productDescription;

    public EmptyVarianProductEditable() {
    }

    protected EmptyVarianProductEditable(Parcel in) {
        orderId = in.readString();
        shopId = in.readString();
        productId = in.readString();
        productName = in.readString();
        productPrice = in.readString();
        productImage = in.readString();
        productDescription = in.readString();
    }

    public static final Creator<EmptyVarianProductEditable> CREATOR = new Creator<EmptyVarianProductEditable>() {
        @Override
        public EmptyVarianProductEditable createFromParcel(Parcel in) {
            return new EmptyVarianProductEditable(in);
        }

        @Override
        public EmptyVarianProductEditable[] newArray(int size) {
            return new EmptyVarianProductEditable[size];
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

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(orderId);
        parcel.writeString(shopId);
        parcel.writeString(productId);
        parcel.writeString(productName);
        parcel.writeString(productPrice);
        parcel.writeString(productImage);
        parcel.writeString(productDescription);
    }
}
