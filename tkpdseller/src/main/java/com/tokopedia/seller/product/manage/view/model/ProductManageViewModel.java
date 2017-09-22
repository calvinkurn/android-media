package com.tokopedia.seller.product.manage.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.base.view.adapter.ItemIdType;
import com.tokopedia.seller.base.view.adapter.ItemType;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public class ProductManageViewModel implements ItemIdType, Parcelable {
    public static final int TYPE = 432;
    public static final String STOCK_READY_VALUE = "1";

    String productName;
    String imageUrl;
    String productId;
    String productPrice;
    String productStatus;

    @Override
    public int getType() {
        return TYPE;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    @Override
    public String getId() {
        return productId;
    }

    public void setTitle(String productName) {
        this.productName = productName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setId(String productId) {
        this.productId = productId;
    }

    public boolean isStockOrImageEmpty(){
        return productStatus != null && !productStatus.equals(STOCK_READY_VALUE);
    }

    public ProductManageViewModel() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productName);
        dest.writeString(this.imageUrl);
        dest.writeString(this.productId);
        dest.writeString(this.productPrice);
        dest.writeString(this.productStatus);
    }

    protected ProductManageViewModel(Parcel in) {
        this.productName = in.readString();
        this.imageUrl = in.readString();
        this.productId = in.readString();
        this.productPrice = in.readString();
        this.productStatus = in.readString();
    }

    public static final Creator<ProductManageViewModel> CREATOR = new Creator<ProductManageViewModel>() {
        @Override
        public ProductManageViewModel createFromParcel(Parcel source) {
            return new ProductManageViewModel(source);
        }

        @Override
        public ProductManageViewModel[] newArray(int size) {
            return new ProductManageViewModel[size];
        }
    };
}
