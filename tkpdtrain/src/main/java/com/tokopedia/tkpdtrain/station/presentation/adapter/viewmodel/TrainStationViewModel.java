package com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdtrain.station.presentation.adapter.TrainStationTypeFactory;

/**
 * Created by Rizky on 21/02/18.
 */

public class TrainStationViewModel implements Parcelable, Visitable<TrainStationTypeFactory> {
    private int stationId;
    private String stationCode;
    private String stationName;
    private String cityCode;
    private String cityName;
    private String islandId;

    public TrainStationViewModel() {
    }

    protected TrainStationViewModel(Parcel in) {
        stationId = in.readInt();
        stationCode = in.readString();
        stationName = in.readString();
        cityCode = in.readString();
        cityName = in.readString();
        islandId = in.readString();
    }

    public static final Creator<TrainStationViewModel> CREATOR = new Creator<TrainStationViewModel>() {
        @Override
        public TrainStationViewModel createFromParcel(Parcel in) {
            return new TrainStationViewModel(in);
        }

        @Override
        public TrainStationViewModel[] newArray(int size) {
            return new TrainStationViewModel[size];
        }
    };

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
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

    @Override
    public int type(TrainStationTypeFactory typeFactory) {
        return typeFactory.type(this);
    }


    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getIslandId() {
        return islandId;
    }

    public void setIslandId(String islandId) {
        this.islandId = islandId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(stationId);
        parcel.writeString(stationCode);
        parcel.writeString(stationName);
        parcel.writeString(cityCode);
        parcel.writeString(cityName);
        parcel.writeString(islandId);
    }
}
