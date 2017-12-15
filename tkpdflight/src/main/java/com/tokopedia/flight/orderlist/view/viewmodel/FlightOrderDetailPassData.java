package com.tokopedia.flight.orderlist.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by alvarisi on 12/13/17.
 */

public class FlightOrderDetailPassData implements Parcelable {
    private String orderId;
    private String departureCity;
    private String departureAiportId;
    private String departureTime;
    private String arrivalCity;
    private String arrivalAirportId;
    private String arrivalTime;
    private int status;

    public FlightOrderDetailPassData() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getDepartureAiportId() {
        return departureAiportId;
    }

    public void setDepartureAiportId(String departureAiportId) {
        this.departureAiportId = departureAiportId;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public String getArrivalAirportId() {
        return arrivalAirportId;
    }

    public void setArrivalAirportId(String arrivalAirportId) {
        this.arrivalAirportId = arrivalAirportId;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderId);
        dest.writeString(this.departureCity);
        dest.writeString(this.departureAiportId);
        dest.writeString(this.departureTime);
        dest.writeString(this.arrivalCity);
        dest.writeString(this.arrivalAirportId);
        dest.writeString(this.arrivalTime);
        dest.writeInt(this.status);
    }

    protected FlightOrderDetailPassData(Parcel in) {
        this.orderId = in.readString();
        this.departureCity = in.readString();
        this.departureAiportId = in.readString();
        this.departureTime = in.readString();
        this.arrivalCity = in.readString();
        this.arrivalAirportId = in.readString();
        this.arrivalTime = in.readString();
        this.status = in.readInt();
    }

    public static final Creator<FlightOrderDetailPassData> CREATOR = new Creator<FlightOrderDetailPassData>() {
        @Override
        public FlightOrderDetailPassData createFromParcel(Parcel source) {
            return new FlightOrderDetailPassData(source);
        }

        @Override
        public FlightOrderDetailPassData[] newArray(int size) {
            return new FlightOrderDetailPassData[size];
        }
    };
}
