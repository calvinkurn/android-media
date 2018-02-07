package com.tokopedia.flight.dashboard.view.fragment.viewmodel;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;

/**
 * Created by alvarisi on 10/30/17.
 */

public class FlightDashboardViewModel implements Parcelable, Cloneable {
    private String departureDate;
    private String departureDateFmt;
    private String returnDate;
    private String returnDateFmt;
    private boolean isOneWay;
    private FlightPassengerViewModel flightPassengerViewModel;
    private String flightPassengerFmt;
    private FlightAirportDB departureAirport;
    private String departureAirportFmt;
    private FlightAirportDB arrivalAirport;
    private String arrivalAirportFmt;
    private FlightClassViewModel flightClass;

    public FlightDashboardViewModel() {
    }

    public FlightDashboardViewModel(String departureDate,
                                    String departureDateFmt,
                                    String returnDate,
                                    String returnDateFmt,
                                    boolean isOneWay,
                                    FlightPassengerViewModel flightPassengerViewModel,
                                    String flightPassengerFmt,
                                    FlightAirportDB departureAirport,
                                    String departureAirportFmt,
                                    FlightAirportDB arrivalAirport,
                                    String arrivalAirportFmt,
                                    FlightClassViewModel flightClass) {
        this.departureDate = departureDate;
        this.departureDateFmt = departureDateFmt;
        this.returnDate = returnDate;
        this.returnDateFmt = returnDateFmt;
        this.isOneWay = isOneWay;
        this.flightPassengerViewModel = flightPassengerViewModel;
        this.flightPassengerFmt = flightPassengerFmt;
        this.departureAirport = departureAirport;
        this.departureAirportFmt = departureAirportFmt;
        this.arrivalAirport = arrivalAirport;
        this.arrivalAirportFmt = arrivalAirportFmt;
        this.flightClass = flightClass;
    }

    protected FlightDashboardViewModel(Parcel in) {
        departureDate = in.readString();
        departureDateFmt = in.readString();
        returnDate = in.readString();
        returnDateFmt = in.readString();
        isOneWay = in.readByte() != 0;
        flightPassengerViewModel = in.readParcelable(FlightPassengerViewModel.class.getClassLoader());
        flightPassengerFmt = in.readString();
        departureAirport = in.readParcelable(FlightAirportDB.class.getClassLoader());
        departureAirportFmt = in.readString();
        arrivalAirport = in.readParcelable(FlightAirportDB.class.getClassLoader());
        arrivalAirportFmt = in.readString();
        flightClass = in.readParcelable(FlightClassViewModel.class.getClassLoader());
    }

    public static final Creator<FlightDashboardViewModel> CREATOR = new Creator<FlightDashboardViewModel>() {
        @Override
        public FlightDashboardViewModel createFromParcel(Parcel in) {
            return new FlightDashboardViewModel(in);
        }

        @Override
        public FlightDashboardViewModel[] newArray(int size) {
            return new FlightDashboardViewModel[size];
        }
    };

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isOneWay() {
        return isOneWay;
    }

    public void setOneWay(boolean oneWay) {
        isOneWay = oneWay;
    }

    public FlightPassengerViewModel getFlightPassengerViewModel() {
        return flightPassengerViewModel;
    }

    public void setFlightPassengerViewModel(FlightPassengerViewModel flightPassengerViewModel) {
        this.flightPassengerViewModel = flightPassengerViewModel;
    }

    public FlightAirportDB getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(FlightAirportDB departureAirport) {
        this.departureAirport = departureAirport;
    }

    public FlightAirportDB getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(FlightAirportDB arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public CharSequence getDepartureDateFmt() {
        return departureDateFmt;
    }

    public CharSequence getReturnDateFmt() {
        return returnDateFmt;
    }

    public CharSequence getPassengerFmt() {
        return flightPassengerFmt;
    }

    public void setDepartureDateFmt(String departureDateFmt) {
        this.departureDateFmt = departureDateFmt;
    }

    public void setReturnDateFmt(String returnDateFmt) {
        this.returnDateFmt = returnDateFmt;
    }

    public String getFlightPassengerFmt() {
        return flightPassengerFmt;
    }

    public void setFlightPassengerFmt(String flightPassengerFmt) {
        this.flightPassengerFmt = flightPassengerFmt;
    }

    public String getDepartureAirportFmt() {
        return departureAirportFmt;
    }

    public CharSequence getAirportTextForView(Context context, boolean isDeparture){
        FlightAirportDB flightAirportDB = isDeparture? departureAirport: arrivalAirport;

        SpannableStringBuilder text = new SpannableStringBuilder();
        String depAirportID = flightAirportDB.getAirportId();
        if (TextUtils.isEmpty(depAirportID)) {
            // id is more than one
            String cityCode = flightAirportDB.getCityCode();
            if (TextUtils.isEmpty(cityCode)) {
                text.append(flightAirportDB.getCityName());
                return makeBold(context, text);
            } else {
                text.append(cityCode);
            }
        } else {
            text.append(depAirportID);
        }
        makeBold(context, text);
        String cityName = flightAirportDB.getCityName();
        if (!TextUtils.isEmpty(cityName)) {
            SpannableStringBuilder cityNameText = new SpannableStringBuilder(cityName);
            makeSmall(cityNameText);
            text.append("\n");
            text.append(cityNameText);
        }
        return text;
    }

    private SpannableStringBuilder makeBold(Context context, SpannableStringBuilder text) {
        if (TextUtils.isEmpty(text)){
            return text;
        }
        text.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new RelativeSizeSpan(1.25f),
                0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(context, android.R.color.black)),
                0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

    private SpannableStringBuilder makeSmall(SpannableStringBuilder text) {
        if (TextUtils.isEmpty(text)){
            return text;
        }
        text.setSpan(new RelativeSizeSpan(0.75f),
                0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

    public void setDepartureAirportFmt(String departureAirportFmt) {
        this.departureAirportFmt = departureAirportFmt;
    }

    public String getArrivalAirportFmt() {
        return arrivalAirportFmt;
    }

    public void setArrivalAirportFmt(String arrivalAirportFmt) {
        this.arrivalAirportFmt = arrivalAirportFmt;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public FlightClassViewModel getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(FlightClassViewModel flightClass) {
        this.flightClass = flightClass;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(departureDate);
        parcel.writeString(departureDateFmt);
        parcel.writeString(returnDate);
        parcel.writeString(returnDateFmt);
        parcel.writeByte((byte) (isOneWay ? 1 : 0));
        parcel.writeParcelable(flightPassengerViewModel, i);
        parcel.writeString(flightPassengerFmt);
        parcel.writeParcelable(departureAirport, i);
        parcel.writeString(departureAirportFmt);
        parcel.writeParcelable(arrivalAirport, i);
        parcel.writeString(arrivalAirportFmt);
        parcel.writeParcelable(flightClass, i);
    }

    public static class Builder {
        private String departureDate;
        private String departureDateFmt;
        private String returnDate;
        private String returnDateFmt;
        private boolean isOneWay;
        private FlightPassengerViewModel flightPassengerViewModel;
        private String flightPassengerFmt;
        private FlightAirportDB departureAirport;
        private String departureAirportFmt;
        private FlightAirportDB arrivalAirport;
        private String arrivalAirportFmt;
        private FlightClassViewModel flightClass;

        public Builder() {
        }

        public Builder setDepartureDate(String departureDate) {
            this.departureDate = departureDate;
            return this;
        }

        public Builder setDepartureDateFmt(String departureDate) {
            this.departureDateFmt = departureDate;
            return this;
        }

        public Builder setReturnDate(String returnDate) {
            this.returnDate = returnDate;
            return this;
        }

        public Builder setReturnDateFmt(String returnDate) {
            this.returnDateFmt = returnDate;
            return this;
        }

        public Builder setIsOneWay(boolean isOneWay) {
            this.isOneWay = isOneWay;
            return this;
        }

        public Builder setFlightPassengerViewModel(FlightPassengerViewModel flightPassengerViewModel) {
            this.flightPassengerViewModel = flightPassengerViewModel;
            return this;
        }

        public Builder setFlightPassengerFmt(String passengerFmt) {
            this.flightPassengerFmt = passengerFmt;
            return this;
        }

        public Builder setDepartureAirport(FlightAirportDB departureAirport) {
            this.departureAirport = departureAirport;
            return this;
        }

        public Builder setDepartureAirportFmt(String departureAirportFmt) {
            this.departureAirportFmt = departureAirportFmt;
            return this;
        }

        public Builder setArrivalAirport(FlightAirportDB arrivalAirport) {
            this.arrivalAirport = arrivalAirport;
            return this;
        }

        public Builder setArrivalAirportFmt(String arrivalAirportFmt) {
            this.arrivalAirportFmt = arrivalAirportFmt;
            return this;
        }

        public Builder setFlightClass(FlightClassViewModel flightClass) {
            this.flightClass = flightClass;
            return this;
        }


        public FlightDashboardViewModel build() {
            return new FlightDashboardViewModel(departureDate,
                    departureDateFmt,
                    returnDate,
                    returnDateFmt,
                    isOneWay,
                    flightPassengerViewModel,
                    flightPassengerFmt,
                    departureAirport,
                    departureAirportFmt,
                    arrivalAirport,
                    arrivalAirportFmt,
                    flightClass);
        }
    }
}
