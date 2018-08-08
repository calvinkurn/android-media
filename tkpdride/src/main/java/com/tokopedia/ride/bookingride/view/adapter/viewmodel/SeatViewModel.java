package com.tokopedia.ride.bookingride.view.adapter.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alvarisi on 3/23/17.
 */

public class SeatViewModel implements Parcelable{
    private int seat;
    private String seatText;

    public SeatViewModel() {
    }

    public SeatViewModel(int seat, String seatText) {
        this.seat = seat;
        this.seatText = seatText;
    }

    protected SeatViewModel(Parcel in) {
        seat = in.readInt();
        seatText = in.readString();
    }

    public static final Creator<SeatViewModel> CREATOR = new Creator<SeatViewModel>() {
        @Override
        public SeatViewModel createFromParcel(Parcel in) {
            return new SeatViewModel(in);
        }

        @Override
        public SeatViewModel[] newArray(int size) {
            return new SeatViewModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(seat);
        parcel.writeString(seatText);
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public String getSeatType() {
        return seatText;
    }

    public void setSeatType(String seatType) {
        this.seatText = seatType;
    }
}
