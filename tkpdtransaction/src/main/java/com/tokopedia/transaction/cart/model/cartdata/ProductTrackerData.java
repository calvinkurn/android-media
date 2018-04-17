package com.tokopedia.transaction.cart.model.cartdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 4/4/18. Tokopedia
 */

public class ProductTrackerData implements Parcelable {

    @SerializedName("attribution")
    @Expose
    private String attribution;

    @SerializedName("tracker_list_name")
    @Expose
    private String listDataName;

    protected ProductTrackerData(Parcel in) {
        attribution = in.readString();
        listDataName = in.readString();
    }

    public static final Creator<ProductTrackerData> CREATOR = new Creator<ProductTrackerData>() {
        @Override
        public ProductTrackerData createFromParcel(Parcel in) {
            return new ProductTrackerData(in);
        }

        @Override
        public ProductTrackerData[] newArray(int size) {
            return new ProductTrackerData[size];
        }
    };

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public String getListDataName() {
        return listDataName;
    }

    public void setListDataName(String listDataName) {
        this.listDataName = listDataName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(attribution);
        parcel.writeString(listDataName);
    }
}
