package com.tokopedia.flight.search.view.model.resultstatistics;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory;
import com.tokopedia.flight.search.view.model.filter.TransitEnum;

/**
 * Created by User on 11/1/2017.
 */

public class TransitStat implements Parcelable, Visitable<BaseListCheckableTypeFactory<TransitStat>> {
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
    private TransitEnum transitType;
    private int minPrice;
    private String minPriceString;

    public TransitStat(TransitEnum transitType, int minPrice, String minPriceString) {
        this.transitType = transitType;
        this.minPrice = minPrice;
        this.minPriceString = minPriceString;
    }

    protected TransitStat(Parcel in) {
        int tmpTransitType = in.readInt();
        this.transitType = tmpTransitType == -1 ? null : TransitEnum.values()[tmpTransitType];
        this.minPrice = in.readInt();
        this.minPriceString = in.readString();
    }

    public String getMinPriceString() {
        return minPriceString;
    }

    public void setMinPriceString(String minPriceString) {
        this.minPriceString = minPriceString;
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
        dest.writeString(this.minPriceString);
    }

    @Override
    public int type(BaseListCheckableTypeFactory<TransitStat> typeFactory) {
        return typeFactory.type(this);
    }
}
