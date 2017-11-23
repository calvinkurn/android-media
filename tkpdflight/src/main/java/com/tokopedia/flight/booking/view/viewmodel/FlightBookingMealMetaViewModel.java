package com.tokopedia.flight.booking.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author by alvarisi on 11/22/17.
 */

public class FlightBookingMealMetaViewModel implements Parcelable {
    private String key;
    private String description;
    private List<FlightBookingMealViewModel> mealViewModels;

    public FlightBookingMealMetaViewModel() {
    }

    protected FlightBookingMealMetaViewModel(Parcel in) {
        key = in.readString();
        description = in.readString();
        mealViewModels = in.createTypedArrayList(FlightBookingMealViewModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(description);
        dest.writeTypedList(mealViewModels);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlightBookingMealMetaViewModel> CREATOR = new Creator<FlightBookingMealMetaViewModel>() {
        @Override
        public FlightBookingMealMetaViewModel createFromParcel(Parcel in) {
            return new FlightBookingMealMetaViewModel(in);
        }

        @Override
        public FlightBookingMealMetaViewModel[] newArray(int size) {
            return new FlightBookingMealMetaViewModel[size];
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

    public List<FlightBookingMealViewModel> getMealViewModels() {
        return mealViewModels;
    }

    public void setMealViewModels(List<FlightBookingMealViewModel> mealViewModels) {
        this.mealViewModels = mealViewModels;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FlightBookingMealMetaViewModel &&
                ((FlightBookingMealMetaViewModel) obj).getKey().equalsIgnoreCase(key);
    }
}
