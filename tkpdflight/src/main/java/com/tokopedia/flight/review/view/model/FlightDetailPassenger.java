package com.tokopedia.flight.review.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;
import com.tokopedia.flight.booking.constant.FlightBookingPassenger;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.review.view.adapter.FlightBookingReviewPassengerAdapterTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/23/17.
 */

public class FlightDetailPassenger implements Parcelable, ItemType, Visitable<FlightBookingReviewPassengerAdapterTypeFactory> {

    public static final int TYPE = 234;
    List<SimpleViewModel> infoPassengerList;
    String passengerName;
    @FlightBookingPassenger
    int passengerType;

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

    public @FlightBookingPassenger int getPassengerType() {
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
        dest.writeList(this.infoPassengerList);
        dest.writeString(this.passengerName);
        dest.writeInt(this.passengerType);
    }

    protected FlightDetailPassenger(Parcel in) {
        this.infoPassengerList = new ArrayList<SimpleViewModel>();
        in.readList(this.infoPassengerList, SimpleViewModel.class.getClassLoader());
        this.passengerName = in.readString();
        this.passengerType = in.readInt();
    }

    public static final Creator<FlightDetailPassenger> CREATOR = new Creator<FlightDetailPassenger>() {
        @Override
        public FlightDetailPassenger createFromParcel(Parcel source) {
            return new FlightDetailPassenger(source);
        }

        @Override
        public FlightDetailPassenger[] newArray(int size) {
            return new FlightDetailPassenger[size];
        }
    };

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public int type(FlightBookingReviewPassengerAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
