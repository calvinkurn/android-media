package com.tokopedia.ride.common.ride.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by alvarisi on 3/20/17.
 */

public class PriceDetail implements Parcelable {
    private String base;
    private String cancellationFee;
    private String costPerDistance;
    private String costPerMinute;
    private String currencyCode;
    private String distanceUnit;
    private String minimum;
    private List<String> serviceFees;

    public PriceDetail() {
    }

    protected PriceDetail(Parcel in) {
        base = in.readString();
        cancellationFee = in.readString();
        costPerDistance = in.readString();
        costPerMinute = in.readString();
        currencyCode = in.readString();
        distanceUnit = in.readString();
        minimum = in.readString();
        serviceFees = in.createStringArrayList();
    }

    public static final Creator<PriceDetail> CREATOR = new Creator<PriceDetail>() {
        @Override
        public PriceDetail createFromParcel(Parcel in) {
            return new PriceDetail(in);
        }

        @Override
        public PriceDetail[] newArray(int size) {
            return new PriceDetail[size];
        }
    };

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getCancellationFee() {
        return cancellationFee;
    }

    public void setCancellationFee(String cancellationFee) {
        this.cancellationFee = cancellationFee;
    }

    public String getCostPerDistance() {
        return costPerDistance;
    }

    public void setCostPerDistance(String costPerDistance) {
        this.costPerDistance = costPerDistance;
    }

    public String getCostPerMinute() {
        return costPerMinute;
    }

    public void setCostPerMinute(String costPerMinute) {
        this.costPerMinute = costPerMinute;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    public List<String> getServiceFees() {
        return serviceFees;
    }

    public void setServiceFees(List<String> serviceFees) {
        this.serviceFees = serviceFees;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(base);
        dest.writeString(cancellationFee);
        dest.writeString(costPerDistance);
        dest.writeString(costPerMinute);
        dest.writeString(currencyCode);
        dest.writeString(distanceUnit);
        dest.writeString(minimum);
        dest.writeStringList(serviceFees);
    }
}
