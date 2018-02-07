package com.tokopedia.seller.shop.open.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by normansyahputa on 12/21/17.
 */

public class LocationViewModel implements Parcelable {
    public static final Parcelable.Creator<LocationViewModel> CREATOR = new Parcelable.Creator<LocationViewModel>() {
        @Override
        public LocationViewModel createFromParcel(Parcel in) {
            return new LocationViewModel(in);
        }

        @Override
        public LocationViewModel[] newArray(int size) {
            return new LocationViewModel[size];
        }
    };
    private int districtId;
    private String districtName;
    private int cityId;
    private String cityName;
    private int provinceId;
    private String provinceName;
    private ArrayList<String> zipCodes;

    public LocationViewModel() {
    }

    protected LocationViewModel(Parcel in) {
        districtId = in.readInt();
        districtName = in.readString();
        cityId = in.readInt();
        cityName = in.readString();
        provinceId = in.readInt();
        provinceName = in.readString();
        zipCodes = in.createStringArrayList();
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public ArrayList<String> getZipCodes() {
        return zipCodes;
    }

    public void setZipCodes(ArrayList<String> zipCodes) {
        this.zipCodes = zipCodes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(districtId);
        parcel.writeString(districtName);
        parcel.writeInt(cityId);
        parcel.writeString(cityName);
        parcel.writeInt(provinceId);
        parcel.writeString(provinceName);
        parcel.writeStringList(zipCodes);
    }
}
