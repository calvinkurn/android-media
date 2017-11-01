package com.tokopedia.flight.search.view.model.statistic;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.search.view.model.DepartureTimeEnum;

/**
 * Created by User on 11/1/2017.
 */

public class DepartureStat implements Parcelable {
    private DepartureTimeEnum departureTimeEnum;
    private int minPrice;

    public DepartureStat(DepartureTimeEnum departureTimeEnum, int minPrice) {
        this.departureTimeEnum = departureTimeEnum;
        this.minPrice = minPrice;
    }

    public DepartureTimeEnum getDepartureTime() {
        return departureTimeEnum;
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
        dest.writeInt(this.departureTimeEnum == null ? -1 : this.departureTimeEnum.ordinal());
        dest.writeInt(this.minPrice);
    }

    protected DepartureStat(Parcel in) {
        int tmpDepartureTimeEnum = in.readInt();
        this.departureTimeEnum = tmpDepartureTimeEnum == -1 ? null : DepartureTimeEnum.values()[tmpDepartureTimeEnum];
        this.minPrice = in.readInt();
    }

    public static final Parcelable.Creator<DepartureStat> CREATOR = new Parcelable.Creator<DepartureStat>() {
        @Override
        public DepartureStat createFromParcel(Parcel source) {
            return new DepartureStat(source);
        }

        @Override
        public DepartureStat[] newArray(int size) {
            return new DepartureStat[size];
        }
    };
}
