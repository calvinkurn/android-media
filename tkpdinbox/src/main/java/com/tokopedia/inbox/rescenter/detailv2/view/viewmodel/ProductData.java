package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by hangnadi on 3/10/17.
 */

public class ProductData implements Parcelable {

    public static final Parcelable.Creator<ProductData> CREATOR = new Parcelable.Creator<ProductData>() {
        @Override
        public ProductData createFromParcel(Parcel source) {
            return new ProductData(source);
        }

        @Override
        public ProductData[] newArray(int size) {
            return new ProductData[size];
        }
    };
    private List<ProductItem> productList;

    public ProductData() {
    }

    protected ProductData(Parcel in) {
        this.productList = in.createTypedArrayList(ProductItem.CREATOR);
    }

    public List<ProductItem> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductItem> productList) {
        this.productList = productList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.productList);
    }
}
