package com.tokopedia.flight.dashboard.view.fragment.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.booking.view.adapter.FlightBookingListPassengerAdapterTypeFactory;

/**
 * Created by alvarisi on 10/25/17.
 */

public class FlightPassengerViewModel implements Parcelable, Cloneable {
    private int adult;
    private int children;
    private int infant;

    public FlightPassengerViewModel() {
    }


    public FlightPassengerViewModel(int adult, int children, int infant) {
        this.adult = adult;
        this.children = children;
        this.infant = infant;
    }

    protected FlightPassengerViewModel(Parcel in) {
        adult = in.readInt();
        children = in.readInt();
        infant = in.readInt();
    }

    public static final Creator<FlightPassengerViewModel> CREATOR = new Creator<FlightPassengerViewModel>() {
        @Override
        public FlightPassengerViewModel createFromParcel(Parcel in) {
            return new FlightPassengerViewModel(in);
        }

        @Override
        public FlightPassengerViewModel[] newArray(int size) {
            return new FlightPassengerViewModel[size];
        }
    };

    public int getAdult() {
        return adult;
    }

    public void setAdult(int adult) {
        this.adult = adult;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public int getInfant() {
        return infant;
    }

    public void setInfant(int infant) {
        this.infant = infant;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(adult);
        dest.writeInt(children);
        dest.writeInt(infant);
    }

    public static class Builder {
        private int adult;
        private int children;
        private int infant;

        public Builder() {
            adult = 0;
            children = 0;
            infant = 0;
        }

        public FlightPassengerViewModel.Builder setAdult(int adult) {
            this.adult = adult;
            return this;
        }

        public FlightPassengerViewModel.Builder setChildren(int children) {
            this.children = children;
            return this;
        }

        public FlightPassengerViewModel.Builder setInfant(int infant) {
            this.infant = infant;
            return this;
        }

        public FlightPassengerViewModel build() {
            return new FlightPassengerViewModel(adult, children, infant);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
