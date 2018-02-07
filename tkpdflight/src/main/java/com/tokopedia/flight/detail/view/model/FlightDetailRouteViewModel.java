package com.tokopedia.flight.detail.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.detail.view.adapter.FlightDetailRouteTypeFactory;
import com.tokopedia.flight.search.data.cloud.model.response.Amenity;

import java.util.List;

/**
 * @author alvarisi
 */

public class FlightDetailRouteViewModel implements Parcelable, Visitable<FlightDetailRouteTypeFactory> {

    private String pnr;
    private String airlineName;
    private String airlineCode;
    private String airlineLogo;
    private int airlineMandatoryDOB;
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
        pnr = in.readString();
        airlineName = in.readString();
        airlineCode = in.readString();
        airlineLogo = in.readString();
        airlineMandatoryDOB = in.readInt();
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

    public int isAirlineMandatoryDOB() {
        return airlineMandatoryDOB;
    }

    public void setAirlineMandatoryDOB(int airlineMandatoryDOB) {
        this.airlineMandatoryDOB = airlineMandatoryDOB;
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
    public int type(FlightDetailRouteTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pnr);
        dest.writeString(airlineName);
        dest.writeString(airlineCode);
        dest.writeString(airlineLogo);
        dest.writeInt(airlineMandatoryDOB);
        dest.writeString(flightNumber);
        dest.writeString(departureTimestamp);
        dest.writeString(departureAirportCity);
        dest.writeString(departureAirportCode);
        dest.writeString(departureAirportName);
        dest.writeByte((byte) (isRefundable ? 1 : 0));
        dest.writeString(duration);
        dest.writeString(arrivalTimestamp);
        dest.writeString(arrivalAirportCity);
        dest.writeString(arrivalAirportCode);
        dest.writeString(arrivalAirportName);
        dest.writeString(layover);
        dest.writeTypedList(infos);
        dest.writeTypedList(amenities);
    }
}
