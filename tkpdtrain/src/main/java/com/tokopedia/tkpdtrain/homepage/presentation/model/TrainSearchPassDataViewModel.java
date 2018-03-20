package com.tokopedia.tkpdtrain.homepage.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by alvarisi on 3/8/18.
 */

public class TrainSearchPassDataViewModel implements Parcelable {
    /*
        "date": "2018-01-01",
        "adult": 1,
        "infant": 0,
        "origin": "", -->station code
        "origin_city": "Bandung",
        "destination": "GMR",
        "destination_city": "Jakarta"
    */

    private String departureDate;
    private String returnDate;
    private String originStationCode;
    private String destinationStationCode;
    private String originCityName;
    private String destinationCityName;
    private int adult;
    private int infant;
    private boolean isOneWay;

    public TrainSearchPassDataViewModel() {
    }


    protected TrainSearchPassDataViewModel(Parcel in) {
        departureDate = in.readString();
        returnDate = in.readString();
        originStationCode = in.readString();
        destinationStationCode = in.readString();
        originCityName = in.readString();
        destinationCityName = in.readString();
        adult = in.readInt();
        infant = in.readInt();
        isOneWay = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(departureDate);
        dest.writeString(returnDate);
        dest.writeString(originStationCode);
        dest.writeString(destinationStationCode);
        dest.writeString(originCityName);
        dest.writeString(destinationCityName);
        dest.writeInt(adult);
        dest.writeInt(infant);
        dest.writeByte((byte) (isOneWay ? 1 : 0));
    }

    public static final Creator<TrainSearchPassDataViewModel> CREATOR = new Creator<TrainSearchPassDataViewModel>() {
        @Override
        public TrainSearchPassDataViewModel createFromParcel(Parcel in) {
            return new TrainSearchPassDataViewModel(in);
        }

        @Override
        public TrainSearchPassDataViewModel[] newArray(int size) {
            return new TrainSearchPassDataViewModel[size];
        }
    };

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getOriginStationCode() {
        return originStationCode;
    }

    public void setOriginStationCode(String originStationCode) {
        this.originStationCode = originStationCode;
    }

    public String getDestinationStationCode() {
        return destinationStationCode;
    }

    public void setDestinationStationCode(String destinationStationCode) {
        this.destinationStationCode = destinationStationCode;
    }

    public String getOriginCityName() {
        return originCityName;
    }

    public void setOriginCityName(String originCityName) {
        this.originCityName = originCityName;
    }

    public String getDestinationCityName() {
        return destinationCityName;
    }

    public void setDestinationCityName(String destinationCityName) {
        this.destinationCityName = destinationCityName;
    }

    public int getAdult() {
        return adult;
    }

    public void setAdult(int adult) {
        this.adult = adult;
    }

    public int getInfant() {
        return infant;
    }

    public void setInfant(int infant) {
        this.infant = infant;
    }

    public boolean isOneWay() {
        return isOneWay;
    }

    public void setOneWay(boolean oneWay) {
        isOneWay = oneWay;
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
