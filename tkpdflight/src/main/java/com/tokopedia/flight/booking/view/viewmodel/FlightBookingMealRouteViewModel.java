package com.tokopedia.flight.booking.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.search.data.cloud.model.response.Route;

import java.util.List;

/**
 * Created by alvarisi on 11/7/17.
 */

public class FlightBookingMealRouteViewModel implements Parcelable {
    private Route route;
    private List<FlightBookingMealViewModel> mealViewModels;

    public FlightBookingMealRouteViewModel() {
    }

    protected FlightBookingMealRouteViewModel(Parcel in) {
        route = in.readParcelable(Route.class.getClassLoader());
        mealViewModels = in.createTypedArrayList(FlightBookingMealViewModel.CREATOR);
    }

    public static final Creator<FlightBookingMealRouteViewModel> CREATOR = new Creator<FlightBookingMealRouteViewModel>() {
        @Override
        public FlightBookingMealRouteViewModel createFromParcel(Parcel in) {
            return new FlightBookingMealRouteViewModel(in);
        }

        @Override
        public FlightBookingMealRouteViewModel[] newArray(int size) {
            return new FlightBookingMealRouteViewModel[size];
        }
    };

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public List<FlightBookingMealViewModel> getMealViewModels() {
        return mealViewModels;
    }

    public void setMealViewModels(List<FlightBookingMealViewModel> mealViewModels) {
        this.mealViewModels = mealViewModels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(route, flags);
        dest.writeTypedList(mealViewModels);
    }
}
