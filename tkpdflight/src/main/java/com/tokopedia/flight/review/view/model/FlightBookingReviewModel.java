package com.tokopedia.flight.review.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/23/17.
 */

public class FlightBookingReviewModel implements Parcelable {

    FlightDetailViewModel detailViewModelListDeparture;
    FlightDetailViewModel detailViewModelListReturn;

    List<FlightDetailPassenger> detailPassengers;
    List<SimpleViewModel> flightReviewFares;

    String totalPrice;

    public FlightDetailViewModel getDetailViewModelListDeparture() {
        return detailViewModelListDeparture;
    }

    public void setDetailViewModelListDeparture(FlightDetailViewModel detailViewModelListDeparture) {
        this.detailViewModelListDeparture = detailViewModelListDeparture;
    }

    public FlightDetailViewModel getDetailViewModelListReturn() {
        return detailViewModelListReturn;
    }

    public void setDetailViewModelListReturn(FlightDetailViewModel detailViewModelListReturn) {
        this.detailViewModelListReturn = detailViewModelListReturn;
    }

    public List<FlightDetailPassenger> getDetailPassengers() {
        return detailPassengers;
    }

    public void setDetailPassengers(List<FlightDetailPassenger> detailPassengers) {
        this.detailPassengers = detailPassengers;
    }

    public List<SimpleViewModel> getFlightReviewFares() {
        return flightReviewFares;
    }

    public void setFlightReviewFares(List<SimpleViewModel> flightReviewFares) {
        this.flightReviewFares = flightReviewFares;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public FlightBookingReviewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.detailViewModelListDeparture, flags);
        dest.writeParcelable(this.detailViewModelListReturn, flags);
        dest.writeTypedList(this.detailPassengers);
        dest.writeList(this.flightReviewFares);
        dest.writeString(this.totalPrice);
    }

    protected FlightBookingReviewModel(Parcel in) {
        this.detailViewModelListDeparture = in.readParcelable(FlightDetailViewModel.class.getClassLoader());
        this.detailViewModelListReturn = in.readParcelable(FlightDetailViewModel.class.getClassLoader());
        this.detailPassengers = in.createTypedArrayList(FlightDetailPassenger.CREATOR);
        this.flightReviewFares = new ArrayList<SimpleViewModel>();
        in.readList(this.flightReviewFares, SimpleViewModel.class.getClassLoader());
        this.totalPrice = in.readString();
    }

    public static final Creator<FlightBookingReviewModel> CREATOR = new Creator<FlightBookingReviewModel>() {
        @Override
        public FlightBookingReviewModel createFromParcel(Parcel source) {
            return new FlightBookingReviewModel(source);
        }

        @Override
        public FlightBookingReviewModel[] newArray(int size) {
            return new FlightBookingReviewModel[size];
        }
    };
}
