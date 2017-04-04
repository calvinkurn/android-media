package com.tokopedia.ride.completetrip.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alvarisi on 3/31/17.
 */

public class Receipt implements Parcelable {
    String requestId;
    String currency;
    String distance;
    String distanceUnit;
    String duration;
    String subtotal;
    String totalCharged;
    String totalFare;
    String totalOwe;
    int duratuinInMinute;

    public Receipt() {
    }

    protected Receipt(Parcel in) {
        requestId = in.readString();
        currency = in.readString();
        distance = in.readString();
        distanceUnit = in.readString();
        duration = in.readString();
        subtotal = in.readString();
        totalCharged = in.readString();
        totalFare = in.readString();
        totalOwe = in.readString();
        duratuinInMinute = in.readInt();
    }

    public static final Creator<Receipt> CREATOR = new Creator<Receipt>() {
        @Override
        public Receipt createFromParcel(Parcel in) {
            return new Receipt(in);
        }

        @Override
        public Receipt[] newArray(int size) {
            return new Receipt[size];
        }
    };

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTotalCharged() {
        return totalCharged;
    }

    public void setTotalCharged(String totalCharged) {
        this.totalCharged = totalCharged;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public String getTotalOwe() {
        return totalOwe;
    }

    public void setTotalOwe(String totalOwe) {
        this.totalOwe = totalOwe;
    }

    public int getDuratuinInMinute() {
        return duratuinInMinute;
    }

    public void setDuratuinInMinute(int duratuinInMinute) {
        this.duratuinInMinute = duratuinInMinute;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(requestId);
        parcel.writeString(currency);
        parcel.writeString(distance);
        parcel.writeString(distanceUnit);
        parcel.writeString(duration);
        parcel.writeString(subtotal);
        parcel.writeString(totalCharged);
        parcel.writeString(totalFare);
        parcel.writeString(totalOwe);
        parcel.writeInt(duratuinInMinute);
    }
}
