package com.tokopedia.flight.search.view.model.statistic;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.search.view.model.TransitEnum;

/**
 * Created by User on 11/1/2017.
 */

public class TransitStat implements Parcelable {
    private TransitEnum transitType;
    private int minPrice;

    public TransitStat(TransitEnum transitType, int minPrice) {
        this.transitType = transitType;
        this.minPrice = minPrice;
    }

    public TransitEnum getTransitType() {
        return transitType;
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
        dest.writeInt(this.transitType == null ? -1 : this.transitType.ordinal());
        dest.writeInt(this.minPrice);
    }

    protected TransitStat(Parcel in) {
        int tmpTransitType = in.readInt();
        this.transitType = tmpTransitType == -1 ? null : TransitEnum.values()[tmpTransitType];
        this.minPrice = in.readInt();
    }

    public static final Parcelable.Creator<TransitStat> CREATOR = new Parcelable.Creator<TransitStat>() {
        @Override
        public TransitStat createFromParcel(Parcel source) {
            return new TransitStat(source);
        }

        @Override
        public TransitStat[] newArray(int size) {
            return new TransitStat[size];
        }
    };
}
