package com.tokopedia.flight.booking.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.search.data.cloud.model.response.Route;

/**
 * Created by alvarisi on 11/7/17.
 */

public class FlightBookingLuggageRouteViewModel implements Parcelable {
    private Route route;
    private FlightBookingLuggageViewModel luggage;

    public FlightBookingLuggageRouteViewModel() {
    }

    protected FlightBookingLuggageRouteViewModel(Parcel in) {
        route = in.readParcelable(Route.class.getClassLoader());
        luggage = in.readParcelable(FlightBookingLuggageViewModel.class.getClassLoader());
    }

    public static final Creator<FlightBookingLuggageRouteViewModel> CREATOR = new Creator<FlightBookingLuggageRouteViewModel>() {
        @Override
        public FlightBookingLuggageRouteViewModel createFromParcel(Parcel in) {
            return new FlightBookingLuggageRouteViewModel(in);
        }

        @Override
        public FlightBookingLuggageRouteViewModel[] newArray(int size) {
            return new FlightBookingLuggageRouteViewModel[size];
        }
    };

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public FlightBookingLuggageViewModel getLuggage() {
        return luggage;
    }

    public void setLuggage(FlightBookingLuggageViewModel luggage) {
        this.luggage = luggage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(route, flags);
        dest.writeParcelable(luggage, flags);
    }
}
