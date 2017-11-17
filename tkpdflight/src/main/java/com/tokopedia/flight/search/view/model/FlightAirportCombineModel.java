package com.tokopedia.flight.search.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 11/16/2017.
 */

public class FlightAirportCombineModel implements Parcelable {
    private String depAirport;
    private String arrAirport;
    private boolean hasLoad;
    private boolean needRefresh;

    public FlightAirportCombineModel(String depAirport, String arrAirport) {
        this.depAirport = depAirport;
        this.arrAirport = arrAirport;
        this.hasLoad = false;
        this.needRefresh = true;
    }

    public String getDepAirport() {
        return depAirport;
    }

    public String getArrAirport() {
        return arrAirport;
    }

    public boolean isHasLoad() {
        return hasLoad;
    }

    public boolean isNeedRefresh() {
        return needRefresh;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.depAirport);
        dest.writeString(this.arrAirport);
        dest.writeByte(this.hasLoad ? (byte) 1 : (byte) 0);
        dest.writeByte(this.needRefresh ? (byte) 1 : (byte) 0);
    }

    protected FlightAirportCombineModel(Parcel in) {
        this.depAirport = in.readString();
        this.arrAirport = in.readString();
        this.hasLoad = in.readByte() != 0;
        this.needRefresh = in.readByte() != 0;
    }

    public static final Parcelable.Creator<FlightAirportCombineModel> CREATOR = new Parcelable.Creator<FlightAirportCombineModel>() {
        @Override
        public FlightAirportCombineModel createFromParcel(Parcel source) {
            return new FlightAirportCombineModel(source);
        }

        @Override
        public FlightAirportCombineModel[] newArray(int size) {
            return new FlightAirportCombineModel[size];
        }
    };
}
