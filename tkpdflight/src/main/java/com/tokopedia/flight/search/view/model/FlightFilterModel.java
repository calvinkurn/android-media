package com.tokopedia.flight.search.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11/1/2017.
 */

public class FlightFilterModel implements Parcelable {
    private int priceMin = Integer.MIN_VALUE;
    private int priceMax = Integer.MAX_VALUE;
    private int durationMin = Integer.MIN_VALUE;
    private int durationMax = Integer.MAX_VALUE;
    private List<Integer> transitTypeList;
    private List<String> airlineList;
    private List<Integer> departureTimeList;
    private List<Integer> refundableTypeList;

    public int getPriceMin() {
        return priceMin;
    }

    public int getPriceMax() {
        return priceMax;
    }

    public int getDurationMin() {
        return durationMin;
    }

    public void setPriceMin(int priceMin) {
        this.priceMin = priceMin;
    }

    public void setPriceMax(int priceMax) {
        this.priceMax = priceMax;
    }

    public void setDurationMax(int durationMax) {
        this.durationMax = durationMax;
    }

    public void setDurationMin(int durationMin) {
        this.durationMin = durationMin;
    }

    public int getDurationMax() {
        return durationMax;
    }

    public List<Integer> getRefundableTypeList() {
        return refundableTypeList;
    }

    public List<Integer> getTransitTypeList() {
        return transitTypeList;
    }

    public List<String> getAirlineList() {
        return airlineList;
    }

    public List<Integer> getDepartureTimeList() {
        return departureTimeList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.priceMin);
        dest.writeInt(this.priceMax);
        dest.writeInt(this.durationMin);
        dest.writeInt(this.durationMax);
        dest.writeList(this.transitTypeList);
        dest.writeStringList(this.airlineList);
        dest.writeList(this.departureTimeList);
        dest.writeList(this.refundableTypeList);
    }

    public FlightFilterModel() {
    }

    protected FlightFilterModel(Parcel in) {
        this.priceMin = in.readInt();
        this.priceMax = in.readInt();
        this.durationMin = in.readInt();
        this.durationMax = in.readInt();
        this.transitTypeList = new ArrayList<Integer>();
        in.readList(this.transitTypeList, Integer.class.getClassLoader());
        this.airlineList = in.createStringArrayList();
        this.departureTimeList = new ArrayList<Integer>();
        in.readList(this.departureTimeList, Integer.class.getClassLoader());
        this.refundableTypeList = new ArrayList<Integer>();
        in.readList(this.refundableTypeList, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<FlightFilterModel> CREATOR = new Parcelable.Creator<FlightFilterModel>() {
        @Override
        public FlightFilterModel createFromParcel(Parcel source) {
            return new FlightFilterModel(source);
        }

        @Override
        public FlightFilterModel[] newArray(int size) {
            return new FlightFilterModel[size];
        }
    };
}
