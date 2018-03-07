package com.tokopedia.inbox.attachproduct.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.core.shopinfo.models.productmodel.Badge;
import com.tokopedia.core.shopinfo.models.productmodel.Label;
import com.tokopedia.inbox.attachproduct.view.adapter.AttachProductListAdapterTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hendri on 13/02/18.
 */

public class AttachProductItemViewModel implements Parcelable, Visitable<AttachProductListAdapterTypeFactory>  {
    private String productUrl;
    private String productName;
    private int productId;
    private String productImageFull;
    private String productImage;
    private String productPrice;

    public AttachProductItemViewModel(String productUrl, String productName, int productId, String productImageFull, String productImage, String productPrice) {
        this.productUrl = productUrl;
        this.productName = productName;
        this.productId = productId;
        this.productImageFull = productImageFull;
        this.productImage = productImage;
        this.productPrice = productPrice;
    }

    public AttachProductItemViewModel(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AttachProductItemViewModel> CREATOR = new Creator<AttachProductItemViewModel>() {
        @Override
        public AttachProductItemViewModel createFromParcel(Parcel in) {
            return new AttachProductItemViewModel(in);
        }

        @Override
        public AttachProductItemViewModel[] newArray(int size) {
            return new AttachProductItemViewModel[size];
        }
    };

    public String getProductUrl() {
        return productUrl;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductImageFull() {
        return productImageFull;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductPrice() {
        return productPrice;
    }

    @Override
    public int type(AttachProductListAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
