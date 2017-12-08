package com.tokopedia.flight.detail.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.type.ItemType;
import com.tokopedia.flight.search.data.cloud.model.response.Amenity;

import java.util.List;

/**
 * @author alvarisi
 */

public class FlightDetailRouteViewModel implements ItemType, Parcelable {
    public static final Creator<FlightDetailRouteViewModel> CREATOR = new Creator<FlightDetailRouteViewModel>() {
        @Override
        public FlightDetailRouteViewModel createFromParcel(Parcel in) {
            return new FlightDetailRouteViewModel(in);
        }

        @Override
        public FlightDetailRouteViewModel[] newArray(int size) {
            return new FlightDetailRouteViewModel[size];
        }
    };
    private static final int TYPE = 12312;
    private String airlineName;
    private String airlineCode;
    private String airlineLogo;
    private String flightNumber;
    private String departureTimestamp;
    private String departureAirportCity;
    private String departureAirportCode;
    private String departureAirportName;
    private boolean isRefundable;
    private String duration;
    private String arrivalTimestamp;
    private String arrivalAirportCity;
    private String arrivalAirportCode;
    private String arrivalAirportName;
    private String layover;
    private List<FlightDetailRouteInfoViewModel> infos;
    private List<Amenity> amenities = null;

    public FlightDetailRouteViewModel() {
    }

    protected FlightDetailRouteViewModel(Parcel in) {
        airlineName = in.readString();
        airlineCode = in.readString();
        airlineLogo = in.readString();
        flightNumber = in.readString();
        departureTimestamp = in.readString();
        departureAirportCity = in.readString();
        departureAirportCode = in.readString();
        departureAirportName = in.readString();
        isRefundable = in.readByte() != 0;
        duration = in.readString();
        arrivalTimestamp = in.readString();
        arrivalAirportCity = in.readString();
        arrivalAirportCode = in.readString();
        arrivalAirportName = in.readString();
        layover = in.readString();
        infos = in.createTypedArrayList(FlightDetailRouteInfoViewModel.CREATOR);
        amenities = in.createTypedArrayList(Amenity.CREATOR);
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getAirlineLogo() {
        return airlineLogo;
    }

    public void setAirlineLogo(String airlineLogo) {
        this.airlineLogo = airlineLogo;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureTimestamp() {
        return departureTimestamp;
    }

    public void setDepartureTimestamp(String departureTimestamp) {
        this.departureTimestamp = departureTimestamp;
    }

    public String getDepartureAirportCity() {
        return departureAirportCity;
    }

    public void setDepartureAirportCity(String departureAirportCity) {
        this.departureAirportCity = departureAirportCity;
    }

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    public void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
    }

    public String getDepartureAirportName() {
        return departureAirportName;
    }

    public void setDepartureAirportName(String departureAirportName) {
        this.departureAirportName = departureAirportName;
    }

    public boolean isRefundable() {
        return isRefundable;
    }

    public void setRefundable(boolean refundable) {
        isRefundable = refundable;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getArrivalTimestamp() {
        return arrivalTimestamp;
    }

    public void setArrivalTimestamp(String arrivalTimestamp) {
        this.arrivalTimestamp = arrivalTimestamp;
    }

    public String getArrivalAirportCity() {
        return arrivalAirportCity;
    }

    public void setArrivalAirportCity(String arrivalAirportCity) {
        this.arrivalAirportCity = arrivalAirportCity;
    }

    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }

    public void setArrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode;
    }

    public String getArrivalAirportName() {
        return arrivalAirportName;
    }

    public void setArrivalAirportName(String arrivalAirportName) {
        this.arrivalAirportName = arrivalAirportName;
    }

    public String getLayover() {
        return layover;
    }

    public void setLayover(String layover) {
        this.layover = layover;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    public List<FlightDetailRouteInfoViewModel> getInfos() {
        return infos;
    }

    public void setInfos(List<FlightDetailRouteInfoViewModel> infos) {
        this.infos = infos;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Amenity> amenities) {
        this.amenities = amenities;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(airlineName);
        parcel.writeString(airlineCode);
        parcel.writeString(airlineLogo);
        parcel.writeString(flightNumber);
        parcel.writeString(departureTimestamp);
        parcel.writeString(departureAirportCity);
        parcel.writeString(departureAirportCode);
        parcel.writeString(departureAirportName);
        parcel.writeByte((byte) (isRefundable ? 1 : 0));
        parcel.writeString(duration);
        parcel.writeString(arrivalTimestamp);
        parcel.writeString(arrivalAirportCity);
        parcel.writeString(arrivalAirportCode);
        parcel.writeString(arrivalAirportName);
        parcel.writeString(layover);
        parcel.writeTypedList(infos);
        parcel.writeTypedList(amenities);
    }
}
