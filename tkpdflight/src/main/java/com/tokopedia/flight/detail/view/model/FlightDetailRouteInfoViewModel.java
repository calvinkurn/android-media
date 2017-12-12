package com.tokopedia.flight.detail.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alvarisi on 12/8/17.
 */

public class FlightDetailRouteInfoViewModel implements Parcelable {
    public static final Creator<FlightDetailRouteInfoViewModel> CREATOR = new Creator<FlightDetailRouteInfoViewModel>() {
        @Override
        public FlightDetailRouteInfoViewModel createFromParcel(Parcel in) {
            return new FlightDetailRouteInfoViewModel(in);
        }

        @Override
        public FlightDetailRouteInfoViewModel[] newArray(int size) {
            return new FlightDetailRouteInfoViewModel[size];
        }
    };
    private String label;
    private String value;

    public FlightDetailRouteInfoViewModel() {
    }

    protected FlightDetailRouteInfoViewModel(Parcel in) {
        label = in.readString();
        value = in.readString();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(label);
        parcel.writeString(value);
    }
}
