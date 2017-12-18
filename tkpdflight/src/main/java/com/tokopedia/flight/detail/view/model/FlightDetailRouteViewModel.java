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
    private static final int TYPE = 12312;
    private String pnr;
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

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pnr);
        dest.writeString(this.airlineName);
        dest.writeString(this.airlineCode);
        dest.writeString(this.airlineLogo);
        dest.writeString(this.flightNumber);
        dest.writeString(this.departureTimestamp);
        dest.writeString(this.departureAirportCity);
        dest.writeString(this.departureAirportCode);
        dest.writeString(this.departureAirportName);
        dest.writeByte(this.isRefundable ? (byte) 1 : (byte) 0);
        dest.writeString(this.duration);
        dest.writeString(this.arrivalTimestamp);
        dest.writeString(this.arrivalAirportCity);
        dest.writeString(this.arrivalAirportCode);
        dest.writeString(this.arrivalAirportName);
        dest.writeString(this.layover);
        dest.writeTypedList(this.infos);
        dest.writeTypedList(this.amenities);
    }

    protected FlightDetailRouteViewModel(Parcel in) {
        this.pnr = in.readString();
        this.airlineName = in.readString();
        this.airlineCode = in.readString();
        this.airlineLogo = in.readString();
        this.flightNumber = in.readString();
        this.departureTimestamp = in.readString();
        this.departureAirportCity = in.readString();
        this.departureAirportCode = in.readString();
        this.departureAirportName = in.readString();
        this.isRefundable = in.readByte() != 0;
        this.duration = in.readString();
        this.arrivalTimestamp = in.readString();
        this.arrivalAirportCity = in.readString();
        this.arrivalAirportCode = in.readString();
        this.arrivalAirportName = in.readString();
        this.layover = in.readString();
        this.infos = in.createTypedArrayList(FlightDetailRouteInfoViewModel.CREATOR);
        this.amenities = in.createTypedArrayList(Amenity.CREATOR);
    }

    public static final Creator<FlightDetailRouteViewModel> CREATOR = new Creator<FlightDetailRouteViewModel>() {
        @Override
        public FlightDetailRouteViewModel createFromParcel(Parcel source) {
            return new FlightDetailRouteViewModel(source);
        }

        @Override
        public FlightDetailRouteViewModel[] newArray(int size) {
            return new FlightDetailRouteViewModel[size];
        }
    };
}
