package com.tokopedia.seller.product.edit.view.model.edit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 2/8/18.
 */

public class ProductPositionViewModel implements Parcelable{
    @SerializedName("position")
    @Expose
    private long productPosition;


    public long getProductPosition() {
        return productPosition;
    }

    public void setProductPosition(long productPosition) {
        this.productPosition = productPosition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.productPosition);
    }

    public ProductPositionViewModel() {
    }

    protected ProductPositionViewModel(Parcel in) {
        this.productPosition = in.readLong();
    }

    public static final Creator<ProductPositionViewModel> CREATOR = new Creator<ProductPositionViewModel>() {
        @Override
        public ProductPositionViewModel createFromParcel(Parcel source) {
            return new ProductPositionViewModel(source);
        }

        @Override
        public ProductPositionViewModel[] newArray(int size) {
            return new ProductPositionViewModel[size];
        }
    };
}
