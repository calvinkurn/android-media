package com.tokopedia.flight.dashboard.view.fragment.passdata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alvarisi on 10/25/17.
 */

public class SelectFlightPassengerPassData implements Parcelable {
    private int adult;
    private int children;
    private int infant;

    public SelectFlightPassengerPassData() {
    }


    public SelectFlightPassengerPassData(int adult, int children, int infant) {
        this.adult = adult;
        this.children = children;
        this.infant = infant;
    }

    protected SelectFlightPassengerPassData(Parcel in) {
        adult = in.readInt();
        children = in.readInt();
        infant = in.readInt();
    }

    public static final Creator<SelectFlightPassengerPassData> CREATOR = new Creator<SelectFlightPassengerPassData>() {
        @Override
        public SelectFlightPassengerPassData createFromParcel(Parcel in) {
            return new SelectFlightPassengerPassData(in);
        }

        @Override
        public SelectFlightPassengerPassData[] newArray(int size) {
            return new SelectFlightPassengerPassData[size];
        }
    };

    public int getAdult() {
        return adult;
    }

    public void setAdult(int adult) {
        this.adult = adult;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public int getInfant() {
        return infant;
    }

    public void setInfant(int infant) {
        this.infant = infant;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(adult);
        dest.writeInt(children);
        dest.writeInt(infant);
    }

    public static class Builder {
        private int adult;
        private int children;
        private int infant;

        public Builder() {
            adult = 0;
            children = 0;
            infant = 0;
        }

        public SelectFlightPassengerPassData.Builder setAdult(int adult) {
            this.adult = adult;
            return this;
        }

        public SelectFlightPassengerPassData.Builder setChildren(int children) {
            this.children = children;
            return this;
        }

        public SelectFlightPassengerPassData.Builder setInfant(int infant) {
            this.infant = infant;
            return this;
        }

        public SelectFlightPassengerPassData build() {
            return new SelectFlightPassengerPassData(adult, children, infant);
        }
    }
}
