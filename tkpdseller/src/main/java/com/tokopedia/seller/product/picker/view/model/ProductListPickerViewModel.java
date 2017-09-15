package com.tokopedia.seller.product.picker.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.base.view.adapter.ItemPickerType;

/**
 * Created by zulfikarrahman on 9/7/17.
 */

public class ProductListPickerViewModel implements ItemPickerType, Parcelable {

    public static final int TYPE = 432;

    String productName;
    String imageUrl;
    String productId;
    String productPrice;

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

    @Override
    public String getTitle() {
        return productName;
    }

    @Override
    public String getIcon() {
        return imageUrl;
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

    public ProductListPickerViewModel() {
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
    }

    protected ProductListPickerViewModel(Parcel in) {
        this.productName = in.readString();
        this.imageUrl = in.readString();
        this.productId = in.readString();
        this.productPrice = in.readString();
    }

    public static final Creator<ProductListPickerViewModel> CREATOR = new Creator<ProductListPickerViewModel>() {
        @Override
        public ProductListPickerViewModel createFromParcel(Parcel source) {
            return new ProductListPickerViewModel(source);
        }

        @Override
        public ProductListPickerViewModel[] newArray(int size) {
            return new ProductListPickerViewModel[size];
        }
    };
}
