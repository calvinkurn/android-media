package com.tokopedia.flight.booking.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

/**
 * @author by alvarisi on 11/8/17.
 */

public class FlightBookingTripViewModel implements Parcelable {
    private FlightSearchViewModel departureTrip;
    private FlightSearchViewModel returnTrip;
    private FlightSearchPassDataViewModel searchParam;

    public FlightBookingTripViewModel() {
    }

    protected FlightBookingTripViewModel(Parcel in) {
        departureTrip = in.readParcelable(FlightSearchViewModel.class.getClassLoader());
        returnTrip = in.readParcelable(FlightSearchViewModel.class.getClassLoader());
        searchParam = in.readParcelable(FlightSearchPassDataViewModel.class.getClassLoader());
    }

    public static final Creator<FlightBookingTripViewModel> CREATOR = new Creator<FlightBookingTripViewModel>() {
        @Override
        public FlightBookingTripViewModel createFromParcel(Parcel in) {
            return new FlightBookingTripViewModel(in);
        }

        @Override
        public FlightBookingTripViewModel[] newArray(int size) {
            return new FlightBookingTripViewModel[size];
        }
    };

    public FlightSearchViewModel getDepartureTrip() {
        return departureTrip;
    }

    public void setDepartureTrip(FlightSearchViewModel departureTrip) {
        this.departureTrip = departureTrip;
    }

    public FlightSearchViewModel getReturnTrip() {
        return returnTrip;
    }

    public void setReturnTrip(FlightSearchViewModel returnTrip) {
        this.returnTrip = returnTrip;
    }

    public FlightSearchPassDataViewModel getSearchParam() {
        return searchParam;
    }

    public void setSearchParam(FlightSearchPassDataViewModel searchParam) {
        this.searchParam = searchParam;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(departureTrip, flags);
        dest.writeParcelable(returnTrip, flags);
        dest.writeParcelable(searchParam, flags);
    }
}
