package com.tokopedia.flight.booking.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alvarisi on 11/7/17.
 */

public class FlightBookingMealViewModel implements Parcelable{
    private int id;
    private String title;
    private String price;

    public FlightBookingMealViewModel() {
    }

    protected FlightBookingMealViewModel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        price = in.readString();
    }

    public static final Creator<FlightBookingMealViewModel> CREATOR = new Creator<FlightBookingMealViewModel>() {
        @Override
        public FlightBookingMealViewModel createFromParcel(Parcel in) {
            return new FlightBookingMealViewModel(in);
        }

        @Override
        public FlightBookingMealViewModel[] newArray(int size) {
            return new FlightBookingMealViewModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(price);
    }
}
