package com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.tkpdtrain.station.presentation.adapter.TrainStationTypeFactory;

/**
 * Created by Rizky on 21/02/18.
 */

public class TrainStationViewModel implements Parcelable, Visitable<TrainStationTypeFactory> {
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

    protected TrainStationViewModel(Parcel in) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
    @Override
    public int type(TrainStationTypeFactory typeFactory) {
        return typeFactory.type(this);
    }


}
