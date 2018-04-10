
package com.tokopedia.seller.product.edit.view.model.edit;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductEtalaseViewModel implements Parcelable{

    @SerializedName("etalase_id")
    @Expose
    private long etalaseId;
    @SerializedName("etalase_name")
    @Expose
    private String etalaseName;

    public long getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(long etalaseId) {
        this.etalaseId = etalaseId;
    }

    public String getEtalaseName() {
        return etalaseName;
    }

    public void setEtalaseName(String etalaseName) {
        this.etalaseName = etalaseName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.etalaseId);
        dest.writeString(this.etalaseName);
    }

    public ProductEtalaseViewModel() {
    }

    protected ProductEtalaseViewModel(Parcel in) {
        this.etalaseId = in.readLong();
        this.etalaseName = in.readString();
    }

    public static final Creator<ProductEtalaseViewModel> CREATOR = new Creator<ProductEtalaseViewModel>() {
        @Override
        public ProductEtalaseViewModel createFromParcel(Parcel source) {
            return new ProductEtalaseViewModel(source);
        }

        @Override
        public ProductEtalaseViewModel[] newArray(int size) {
            return new ProductEtalaseViewModel[size];
        }
    };
}
