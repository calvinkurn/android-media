package com.tokopedia.inbox.attachproduct.view.resultmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.inbox.attachproduct.view.viewmodel.AttachProductItemViewModel;

/**
 * Created by Hendri on 19/02/18.
 */

public class ResultProduct implements Parcelable {
    private Integer productId;
    private String productUrl;
    private String productImageThumbnail;
    private String price;
    private String name;
    public ResultProduct(Integer productId, String productUrl, String productImageThumbnail, String price, String name) {
        this.productId = productId;
        this.productUrl = productUrl;
        this.productImageThumbnail = productImageThumbnail;
        this.price = price;
        this.name = name;
    }

    public ResultProduct(AttachProductItemViewModel viewModel){
        this.productId = viewModel.getProductId();
        this.productUrl = viewModel.getProductUrl();
        this.productImageThumbnail = viewModel.getProductImage();
        this.price = viewModel.getProductPrice();
        this.name = viewModel.getProductName();
    }

    public Integer getProductId() {
        return productId;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public String getProductImageThumbnail() {
        return productImageThumbnail;
    }

    public String getPrice(){
        return price;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.productId);
        dest.writeString(this.productUrl);
        dest.writeString(this.productImageThumbnail);
        dest.writeString(this.price);
        dest.writeString(this.name);
    }

    protected ResultProduct(Parcel in) {
        this.productId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productUrl = in.readString();
        this.productImageThumbnail = in.readString();
        this.price = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<ResultProduct> CREATOR = new Parcelable.Creator<ResultProduct>() {
        @Override
        public ResultProduct createFromParcel(Parcel source) {
            return new ResultProduct(source);
        }

        @Override
        public ResultProduct[] newArray(int size) {
            return new ResultProduct[size];
        }
    };
}
