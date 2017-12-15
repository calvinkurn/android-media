package com.tokopedia.flight.orderlist.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by alvarisi on 12/13/17.
 */

public class FlightOrderDetailPassData implements Parcelable {
    public static final Creator<FlightOrderDetailPassData> CREATOR = new Creator<FlightOrderDetailPassData>() {
        @Override
        public FlightOrderDetailPassData createFromParcel(Parcel in) {
            return new FlightOrderDetailPassData(in);
        }

        @Override
        public FlightOrderDetailPassData[] newArray(int size) {
            return new FlightOrderDetailPassData[size];
        }
    };
    private String orderId;
    private String departureCity;
    private String departureAiportId;
    private String departureTime;
    private String arrivalCity;
    private String arrivalAirportId;
    private String arrivalTime;
    private String status;

    public FlightOrderDetailPassData() {
    }

    protected FlightOrderDetailPassData(Parcel in) {
        orderId = in.readString();
        departureCity = in.readString();
        departureAiportId = in.readString();
        departureTime = in.readString();
        arrivalCity = in.readString();
        arrivalAirportId = in.readString();
        arrivalTime = in.readString();
        status = in.readString();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(orderId);
        parcel.writeString(departureCity);
        parcel.writeString(departureAiportId);
        parcel.writeString(departureTime);
        parcel.writeString(arrivalCity);
        parcel.writeString(arrivalAirportId);
        parcel.writeString(arrivalTime);
        parcel.writeString(status);
    }
}
