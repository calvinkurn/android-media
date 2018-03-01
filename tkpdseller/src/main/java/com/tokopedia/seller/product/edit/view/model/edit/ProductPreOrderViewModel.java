
package com.tokopedia.seller.product.edit.view.model.edit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductPreOrderViewModel implements Parcelable {

    @SerializedName("preorder_process_time")
    @Expose
    private long preorderProcessTime;
    @SerializedName("preorder_time_unit")
    @Expose
    private long preorderTimeUnit; //1:day, 2:week, 3:month
    @SerializedName("preorder_status")
    @Expose
    private long preorderStatus; // 1:active; 0: not active

    public long getPreorderProcessTime() {
        return preorderProcessTime;
    }

    public void setPreorderProcessTime(long preorderProcessTime) {
        this.preorderProcessTime = preorderProcessTime;
    }

    public long getPreorderTimeUnit() {
        return preorderTimeUnit;
    }

    public void setPreorderTimeUnit(long preorderTimeUnit) {
        this.preorderTimeUnit = preorderTimeUnit;
    }

    public long getPreorderStatus() {
        return preorderStatus;
    }

    public void setPreorderStatus(long preorderStatus) {
        this.preorderStatus = preorderStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.preorderProcessTime);
        dest.writeLong(this.preorderTimeUnit);
        dest.writeLong(this.preorderStatus);
    }

    public ProductPreOrderViewModel() {
    }

    protected ProductPreOrderViewModel(Parcel in) {
        this.preorderProcessTime = in.readLong();
        this.preorderTimeUnit = in.readLong();
        this.preorderStatus = in.readLong();
    }

    public static final Creator<ProductPreOrderViewModel> CREATOR = new Creator<ProductPreOrderViewModel>() {
        @Override
        public ProductPreOrderViewModel createFromParcel(Parcel source) {
            return new ProductPreOrderViewModel(source);
        }

        @Override
        public ProductPreOrderViewModel[] newArray(int size) {
            return new ProductPreOrderViewModel[size];
        }
    };
}
