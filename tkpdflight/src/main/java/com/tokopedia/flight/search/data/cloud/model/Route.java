package com.tokopedia.flight.search.data.cloud.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 10/26/2017.
 */

public class Route implements ItemType, Parcelable {
    public static final int TYPE = 893;
    @SerializedName("airline")
    @Expose
    private String airline;
    @SerializedName("departure_airport")
    @Expose
    private String departureAirport;
    @SerializedName("departure_timestamp")
    @Expose
    private String departureTimestamp;
    @SerializedName("arrival_airport")
    @Expose
    private String arrivalAirport;
    @SerializedName("arrival_timestamp")
    @Expose
    private String arrivalTimestamp;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("layover")
    @Expose
    private String layover;
    @SerializedName("infos")
    @Expose
    private List<Info> infos = null;
    @SerializedName("flight_number")
    @Expose
    private String flightNumber;
    @SerializedName("is_refundable")
    @Expose
    private boolean isRefundable;
    @SerializedName("amenities")
    @Expose
    private List<Amenity> amenities = null;

    private String airlineName;
    private String airlineLogo;

    public String getAirline() {
        return airline;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getDepartureTimestamp() {
        return departureTimestamp;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public String getArrivalTimestamp() {
        return arrivalTimestamp;
    }

    public String getDuration() {
        return duration;
    }

    public String getLayover() {
        return layover;
    }

    public List<Info> getInfos() {
        return infos;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public Boolean getRefundable() {
        return isRefundable;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getAirlineLogo() {
        return airlineLogo;
    }

    public void setAirlineLogo(String airlineLogo) {
        this.airlineLogo = airlineLogo;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.airline);
        dest.writeString(this.departureAirport);
        dest.writeString(this.departureTimestamp);
        dest.writeString(this.arrivalAirport);
        dest.writeString(this.arrivalTimestamp);
        dest.writeString(this.duration);
        dest.writeString(this.layover);
        dest.writeList(this.infos);
        dest.writeString(this.flightNumber);
        dest.writeByte(this.isRefundable ? (byte) 1 : (byte) 0);
        dest.writeList(this.amenities);
    }

    public Route() {
    }

    protected Route(Parcel in) {
        this.airline = in.readString();
        this.departureAirport = in.readString();
        this.departureTimestamp = in.readString();
        this.arrivalAirport = in.readString();
        this.arrivalTimestamp = in.readString();
        this.duration = in.readString();
        this.layover = in.readString();
        this.infos = new ArrayList<Info>();
        in.readList(this.infos, Info.class.getClassLoader());
        this.flightNumber = in.readString();
        this.isRefundable = in.readByte() != 0;
        this.amenities = new ArrayList<Amenity>();
        in.readList(this.amenities, Amenity.class.getClassLoader());
    }

    public static final Parcelable.Creator<Route> CREATOR = new Parcelable.Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel source) {
            return new Route(source);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };
}
