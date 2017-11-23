package com.tokopedia.flight.booking.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by alvarisi on 11/22/17.
 */

public class FlightBookingLuggageMetaViewModel implements Parcelable {
    private String key;
    private String description;
    private List<FlightBookingLuggageViewModel> luggages;

    public FlightBookingLuggageMetaViewModel() {
    }

    protected FlightBookingLuggageMetaViewModel(Parcel in) {
        key = in.readString();
        description = in.readString();
        luggages = in.createTypedArrayList(FlightBookingLuggageViewModel.CREATOR);
    }

    public static final Creator<FlightBookingLuggageMetaViewModel> CREATOR = new Creator<FlightBookingLuggageMetaViewModel>() {
        @Override
        public FlightBookingLuggageMetaViewModel createFromParcel(Parcel in) {
            return new FlightBookingLuggageMetaViewModel(in);
        }

        @Override
        public FlightBookingLuggageMetaViewModel[] newArray(int size) {
            return new FlightBookingLuggageMetaViewModel[size];
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

    public List<FlightBookingLuggageViewModel> getLuggages() {
        return luggages;
    }

    public void setLuggages(List<FlightBookingLuggageViewModel> luggages) {
        this.luggages = luggages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(description);
        dest.writeTypedList(luggages);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FlightBookingLuggageMetaViewModel && ((FlightBookingLuggageMetaViewModel) obj).getKey().equalsIgnoreCase(key);
    }
}
