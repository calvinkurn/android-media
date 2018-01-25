package com.tokopedia.flight.review.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.booking.constant.FlightBookingPassenger;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.review.view.adapter.FlightBookingReviewPassengerAdapterTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/23/17.
 */

public class FlightDetailPassenger implements Parcelable, Visitable<FlightBookingReviewPassengerAdapterTypeFactory> {

    List<SimpleViewModel> infoPassengerList;
    String passengerName;
    @FlightBookingPassenger
    int passengerType;

    protected FlightDetailPassenger(Parcel in) {
        infoPassengerList = in.createTypedArrayList(SimpleViewModel.CREATOR);
        passengerName = in.readString();
        passengerType = in.readInt();
    }

    public static final Creator<FlightDetailPassenger> CREATOR = new Creator<FlightDetailPassenger>() {
        @Override
        public FlightDetailPassenger createFromParcel(Parcel in) {
            return new FlightDetailPassenger(in);
        }

        @Override
        public FlightDetailPassenger[] newArray(int size) {
            return new FlightDetailPassenger[size];
        }
    };

    public List<SimpleViewModel> getInfoPassengerList() {
        return infoPassengerList;
    }

    public void setInfoPassengerList(List<SimpleViewModel> infoPassengerList) {
        this.infoPassengerList = infoPassengerList;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public FlightDetailPassenger() {
    }

    public @FlightBookingPassenger
    int getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(int passengerType) {
        this.passengerType = passengerType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeTypedList(infoPassengerList);
        dest.writeString(passengerName);
        dest.writeInt(passengerType);
    }

    @Override
    public int type(FlightBookingReviewPassengerAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
