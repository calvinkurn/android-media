package com.tokopedia.flight.booking.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author by alvarisi on 11/22/17.
 */

public class FlightBookingAmenityMetaViewModel implements Parcelable {
    private String key;
    private String description;
    private List<FlightBookingAmenityViewModel> amenities;

    public FlightBookingAmenityMetaViewModel() {
    }

    protected FlightBookingAmenityMetaViewModel(Parcel in) {
        key = in.readString();
        description = in.readString();
        amenities = in.createTypedArrayList(FlightBookingAmenityViewModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(description);
        dest.writeTypedList(amenities);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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
}
