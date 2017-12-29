package com.tokopedia.flight.booking.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author by alvarisi on 11/22/17.
 */

public class FlightBookingAmenityMetaViewModel implements Parcelable {
    public static final Creator<FlightBookingAmenityMetaViewModel> CREATOR = new Creator<FlightBookingAmenityMetaViewModel>() {
        @Override
        public FlightBookingAmenityMetaViewModel createFromParcel(Parcel in) {
            return new FlightBookingAmenityMetaViewModel(in);
        }

        @Override
        public FlightBookingAmenityMetaViewModel[] newArray(int size) {
            return new FlightBookingAmenityMetaViewModel[size];
        }
    };
    private String arrivalId;
    private String departureId;
    private String journeyId;
    private String key;
    private String description;
    private List<FlightBookingAmenityViewModel> amenities;

    public FlightBookingAmenityMetaViewModel() {
    }

    protected FlightBookingAmenityMetaViewModel(Parcel in) {
        arrivalId = in.readString();
        departureId = in.readString();
        journeyId = in.readString();
        key = in.readString();
        description = in.readString();
        amenities = in.createTypedArrayList(FlightBookingAmenityViewModel.CREATOR);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FlightBookingAmenityViewModel> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<FlightBookingAmenityViewModel> amenities) {
        this.amenities = amenities;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FlightBookingAmenityMetaViewModel &&
                ((FlightBookingAmenityMetaViewModel) obj).getKey().equalsIgnoreCase(key);
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public String getArrivalId() {
        return arrivalId;
    }

    public void setArrivalId(String arrivalId) {
        this.arrivalId = arrivalId;
    }

    public String getDepartureId() {
        return departureId;
    }

    public void setDepartureId(String departureId) {
        this.departureId = departureId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(arrivalId);
        parcel.writeString(departureId);
        parcel.writeString(journeyId);
        parcel.writeString(key);
        parcel.writeString(description);
        parcel.writeTypedList(amenities);
    }
}
