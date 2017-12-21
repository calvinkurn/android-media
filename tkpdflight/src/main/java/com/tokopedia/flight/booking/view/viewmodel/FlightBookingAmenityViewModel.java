package com.tokopedia.flight.booking.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.type.ItemIdType;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;
import com.tokopedia.flight.booking.view.adapter.FlightAmenityAdapterTypeFactory;

/**
 * Created by alvarisi on 11/7/17.
 */

public class FlightBookingAmenityViewModel implements Parcelable, ItemType, ItemIdType, Visitable<FlightAmenityAdapterTypeFactory> {
    public static final int TYPE = 532;
    private String id;
    private String title;
    private String price;
    private int priceNumeric;
    private String departureId;
    private String arrivalId;
    private String amenityType;

    public FlightBookingAmenityViewModel() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FlightBookingAmenityViewModel && ((FlightBookingAmenityViewModel) obj).getId().equalsIgnoreCase(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public int getPriceNumeric() {
        return priceNumeric;
    }

    public void setPriceNumeric(int priceNumeric) {
        this.priceNumeric = priceNumeric;
    }

    public void setDepartureId(String departureId) {
        this.departureId = departureId;
    }

    public String getDepartureId() {
        return departureId;
    }

    public String getArrivalId() {
        return arrivalId;
    }

    public void setArrivalId(String arrivalId) {
        this.arrivalId = arrivalId;
    }

    public void setAmenityType(String amenityType) {
        this.amenityType = amenityType;
    }

    public String getAmenityType() {
        return amenityType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.price);
        dest.writeInt(this.priceNumeric);
        dest.writeString(this.departureId);
        dest.writeString(this.arrivalId);
        dest.writeString(this.amenityType);
    }

    protected FlightBookingAmenityViewModel(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.price = in.readString();
        this.priceNumeric = in.readInt();
        this.departureId = in.readString();
        this.arrivalId = in.readString();
        this.amenityType = in.readString();
    }

    public static final Creator<FlightBookingAmenityViewModel> CREATOR = new Creator<FlightBookingAmenityViewModel>() {
        @Override
        public FlightBookingAmenityViewModel createFromParcel(Parcel source) {
            return new FlightBookingAmenityViewModel(source);
        }

        @Override
        public FlightBookingAmenityViewModel[] newArray(int size) {
            return new FlightBookingAmenityViewModel[size];
        }
    };

    @Override
    public int type(FlightAmenityAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
