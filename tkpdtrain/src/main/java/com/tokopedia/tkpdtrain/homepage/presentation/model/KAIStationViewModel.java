package com.tokopedia.tkpdtrain.homepage.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rizky on 21/02/18.
 */

public class KAIStationViewModel implements Parcelable {

    private String stationId;
    private String stationCode;
    private String cityCode;
    private String cityName;

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    protected KAIStationViewModel(Parcel in) {
    }

    public static final Creator<KAIStationViewModel> CREATOR = new Creator<KAIStationViewModel>() {
        @Override
        public KAIStationViewModel createFromParcel(Parcel in) {
            return new KAIStationViewModel(in);
        }

        @Override
        public KAIStationViewModel[] newArray(int size) {
            return new KAIStationViewModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }



}
