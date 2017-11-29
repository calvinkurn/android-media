package com.tokopedia.flight.review.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingCartData;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingParamViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/23/17.
 */

public class FlightBookingReviewModel implements Parcelable {

    private String id;
    private FlightDetailViewModel detailViewModelListDeparture;
    private FlightDetailViewModel detailViewModelListReturn;

    private List<FlightDetailPassenger> detailPassengers;
    private List<SimpleViewModel> flightReviewFares;
    private String dateFinishTime;

    private String totalPrice;
    private int totalPriceNumeric;

    public int getTotalPriceNumeric() {
        return totalPriceNumeric;
    }

    public void setTotalPriceNumeric(int totalPriceNumeric) {
        this.totalPriceNumeric = totalPriceNumeric;
    }

    public Date getDateFinishTime() {
        return FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_TIMESTAMP_FORMAT,dateFinishTime);
    }

    public void setDateFinishTime(Date dateFinishTime) {
        this.dateFinishTime = FlightDateUtil.dateToString(dateFinishTime, FlightDateUtil.DEFAULT_TIMESTAMP_FORMAT);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public FlightBookingReviewModel(FlightBookingParamViewModel flightBookingParamViewModel, FlightBookingCartData flightBookingCartData) {
        setId(flightBookingParamViewModel.getId());
        setDetailViewModelListDeparture(new FlightDetailViewModel().build(flightBookingCartData.getDepartureTrip()));
        setDetailViewModelListReturn(new FlightDetailViewModel().build(flightBookingCartData.getReturnTrip()));
        setDetailPassengers(generateFlightDetailPassenger(flightBookingParamViewModel.getPassengerViewModels()));
        setFlightReviewFares(flightBookingParamViewModel.getPriceListDetails());
        setTotalPrice(flightBookingParamViewModel.getTotalPriceFmt());
        setTotalPriceNumeric(flightBookingParamViewModel.getTotalPriceNumeric());
        setDateFinishTime(FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_TIMESTAMP_FORMAT,flightBookingParamViewModel.getOrderDueTimestamp()));
    }

    private List<FlightDetailPassenger> generateFlightDetailPassenger(List<FlightBookingPassengerViewModel> passengerViewModels) {
        List<FlightDetailPassenger> flightDetailPassengers = new ArrayList<>();
        for (FlightBookingPassengerViewModel flightBookingPassengerViewModel : passengerViewModels){
            FlightDetailPassenger flightDetailPassenger = new FlightDetailPassenger();
            flightDetailPassenger.setPassengerName(flightBookingPassengerViewModel.getPassengerName());
            flightDetailPassenger.setPassengerType(flightBookingPassengerViewModel.getType());
            flightDetailPassenger.setInfoPassengerList(generateDetailViewModelPassenger(flightBookingPassengerViewModel.getFlightBookingLuggageMetaViewModels(),
                    flightBookingPassengerViewModel.getFlightBookingAmenityMetaViewModels()));
            flightDetailPassengers.add(flightDetailPassenger);
        }
        return flightDetailPassengers;
    }

    private List<SimpleViewModel> generateDetailViewModelPassenger(List<FlightBookingAmenityMetaViewModel> flightBookingLuggageMetaViewModels,
                                                                   List<FlightBookingAmenityMetaViewModel> flightBookingAmenityMetaViewModels) {
        List<SimpleViewModel> simpleViewModels = new ArrayList<>();
        for (FlightBookingAmenityMetaViewModel flightBookingLuggageMetaViewModel : flightBookingLuggageMetaViewModels) {
            SimpleViewModel simpleViewModel = new SimpleViewModel();
            simpleViewModel.setDescription(flightBookingLuggageMetaViewModel.getDescription());
            simpleViewModel.setLabel(generateLabelLuggage(flightBookingLuggageMetaViewModel.getAmenities()));
            simpleViewModels.add(simpleViewModel);
        }

        for (FlightBookingAmenityMetaViewModel flightBookingAmenityMetaViewModel : flightBookingAmenityMetaViewModels) {
            SimpleViewModel simpleViewModel = new SimpleViewModel();
            simpleViewModel.setDescription(flightBookingAmenityMetaViewModel.getDescription());
            simpleViewModel.setLabel(generateLabelMeal(flightBookingAmenityMetaViewModel.getAmenities()));
            simpleViewModels.add(simpleViewModel);
        }
        return simpleViewModels;
    }

    private String generateLabelMeal(List<FlightBookingAmenityViewModel> mealViewModels) {
        String labelMeal = "";
        for(FlightBookingMealViewModel flightBookingMealViewModel : mealViewModels){
            labelMeal = labelMeal + flightBookingMealViewModel.getTitle() + "\n";
        }
        return labelMeal;
    }

    private String generateLabelLuggage(List<FlightBookingAmenityViewModel> luggages) {
        String labelLuggage = "";
        for(FlightBookingLuggageViewModel flightBookingLuggageViewModel : luggages){
            labelLuggage = labelLuggage + flightBookingLuggageViewModel.getWeightFmt() + "\n";
        }
        return labelLuggage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.detailViewModelListDeparture, flags);
        dest.writeParcelable(this.detailViewModelListReturn, flags);
        dest.writeTypedList(this.detailPassengers);
        dest.writeTypedList(this.flightReviewFares);
        dest.writeString(this.dateFinishTime);
        dest.writeString(this.totalPrice);
        dest.writeInt(this.totalPriceNumeric);
    }

    protected FlightBookingReviewModel(Parcel in) {
        this.id = in.readString();
        this.detailViewModelListDeparture = in.readParcelable(FlightDetailViewModel.class.getClassLoader());
        this.detailViewModelListReturn = in.readParcelable(FlightDetailViewModel.class.getClassLoader());
        this.detailPassengers = in.createTypedArrayList(FlightDetailPassenger.CREATOR);
        this.flightReviewFares = in.createTypedArrayList(SimpleViewModel.CREATOR);
        this.dateFinishTime = in.readString();
        this.totalPrice = in.readString();
        this.totalPriceNumeric = in.readInt();
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
