package com.tokopedia.flight.search.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 11/16/2017.
 */

public class FlightSearchApiRequestModel implements Parcelable {
    private String depAirport;
    private String arrAirport;
    private String date;
    private int adult;
    private int children;
    private int infant;
    private int classID;

    public FlightSearchApiRequestModel(String depAirport, String arrAirport,
                                       String date, int adult, int children, int infant, int classID) {
        this.depAirport = depAirport;
        this.arrAirport = arrAirport;
        this.date = date;
        this.adult = adult;
        this.children = children;
        this.infant = infant;
        this.classID = classID;
    }

    public String getDepAirport() {
        return depAirport;
    }

    public String getArrAirport() {
        return arrAirport;
    }

    public String getDate() {
        return date;
    }

    public int getAdult() {
        return adult;
    }

    public int getChildren() {
        return children;
    }

    public int getInfant() {
        return infant;
    }

    public int getClassID() {
        return classID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.depAirport);
        dest.writeString(this.arrAirport);
        dest.writeString(this.date);
        dest.writeInt(this.adult);
        dest.writeInt(this.children);
        dest.writeInt(this.infant);
        dest.writeInt(this.classID);
    }

    protected FlightSearchApiRequestModel(Parcel in) {
        this.depAirport = in.readString();
        this.arrAirport = in.readString();
        this.date = in.readString();
        this.adult = in.readInt();
        this.children = in.readInt();
        this.infant = in.readInt();
        this.classID = in.readInt();
    }

    public static final Parcelable.Creator<FlightSearchApiRequestModel> CREATOR = new Parcelable.Creator<FlightSearchApiRequestModel>() {
        @Override
        public FlightSearchApiRequestModel createFromParcel(Parcel source) {
            return new FlightSearchApiRequestModel(source);
        }

        @Override
        public FlightSearchApiRequestModel[] newArray(int size) {
            return new FlightSearchApiRequestModel[size];
        }
    };
}
