package com.tokopedia.flight.search.view.model.statistic;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;

/**
 * Created by User on 11/1/2017.
 */

public class AirlineStat implements Parcelable {
    private FlightAirlineDB airlineDB;
    private int minPrice;

    public AirlineStat(FlightAirlineDB airlineDB, int minPrice) {
        this.airlineDB = airlineDB;
        this.minPrice = minPrice;
    }

    public FlightAirlineDB getAirlineDB() {
        return airlineDB;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.airlineDB, flags);
        dest.writeInt(this.minPrice);
    }

    protected AirlineStat(Parcel in) {
        this.airlineDB = in.readParcelable(FlightAirlineDB.class.getClassLoader());
        this.minPrice = in.readInt();
    }

    public static final Parcelable.Creator<AirlineStat> CREATOR = new Parcelable.Creator<AirlineStat>() {
        @Override
        public AirlineStat createFromParcel(Parcel source) {
            return new AirlineStat(source);
        }

        @Override
        public AirlineStat[] newArray(int size) {
            return new AirlineStat[size];
        }
    };
}
