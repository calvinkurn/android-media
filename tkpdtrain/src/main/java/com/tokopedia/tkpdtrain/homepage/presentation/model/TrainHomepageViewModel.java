package com.tokopedia.tkpdtrain.homepage.presentation.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainStationViewModel;

/**
 * Created by Rizky on 21/02/18.
 */

public class TrainHomepageViewModel implements Parcelable, Cloneable {

    private boolean isOneWay;
    private TrainStationViewModel originStation;
    private TrainStationViewModel destinationStation;
    private String departureDate;
    private String departureDateFmt;
    private String returnDate;
    private String returnDateFmt;
    private TrainPassengerViewModel kaiPassengerViewModel;
    private String passengerFmt;

    public TrainHomepageViewModel() {
    }

    public TrainHomepageViewModel(boolean isOneWay,
                                  TrainStationViewModel originStation,
                                  TrainStationViewModel destinationStation,
                                  String departureDate,
                                  String departureDateFmt,
                                  String returnDate,
                                  String returnDateFmt,
                                  TrainPassengerViewModel kaiPassengerViewModel,
                                  String passengerFmt
    ) {
        this.isOneWay = isOneWay;
        this.originStation = originStation;
        this.destinationStation = destinationStation;
        this.departureDate = departureDate;
        this.departureDateFmt = departureDateFmt;
        this.returnDate = returnDate;
        this.returnDateFmt = returnDateFmt;
        this.kaiPassengerViewModel = kaiPassengerViewModel;
        this.passengerFmt = passengerFmt;
    }

    protected TrainHomepageViewModel(Parcel in) {
        isOneWay = in.readByte() != 0;
        originStation = in.readParcelable(TrainStationViewModel.class.getClassLoader());
        destinationStation = in.readParcelable(TrainStationViewModel.class.getClassLoader());
        departureDate = in.readString();
        departureDateFmt = in.readString();
        returnDate = in.readString();
        returnDateFmt = in.readString();
        kaiPassengerViewModel = in.readParcelable(TrainPassengerViewModel.class.getClassLoader());
        passengerFmt = in.readString();
    }

    public static final Creator<TrainHomepageViewModel> CREATOR = new Creator<TrainHomepageViewModel>() {
        @Override
        public TrainHomepageViewModel createFromParcel(Parcel in) {
            return new TrainHomepageViewModel(in);
        }

        @Override
        public TrainHomepageViewModel[] newArray(int size) {
            return new TrainHomepageViewModel[size];
        }
    };

    public boolean isOneWay() {
        return isOneWay;
    }

    public void setOneWay(boolean oneWay) {
        isOneWay = oneWay;
    }

    public TrainStationViewModel getOriginStation() {
        return originStation;
    }

    public void setOriginStation(TrainStationViewModel originStation) {
        this.originStation = originStation;
    }

    public TrainStationViewModel getDestinationStation() {
        return destinationStation;
    }

    public void setDestinationStation(TrainStationViewModel destinationStation) {
        this.destinationStation = destinationStation;
    }

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

    public CharSequence getDepartureDateFmt() {
        return departureDateFmt;
    }

    public void setDepartureDateFmt(String departureDateFmt) {
        this.departureDateFmt = departureDateFmt;
    }

    public CharSequence getReturnDateFmt() {
        return returnDateFmt;
    }

    public void setReturnDateFmt(String returnDateFmt) {
        this.returnDateFmt = returnDateFmt;
    }

    public TrainPassengerViewModel getKaiPassengerViewModel() {
        return kaiPassengerViewModel;
    }

    public void setKaiPassengerViewModel(TrainPassengerViewModel kaiPassengerViewModel) {
        this.kaiPassengerViewModel = kaiPassengerViewModel;
    }

    public String getPassengerFmt() {
        return passengerFmt;
    }

    public void setPassengerFmt(String passengerFmt) {
        this.passengerFmt = passengerFmt;
    }

    public CharSequence getStationTextForView(Context context, boolean isDeparture){
        TrainStationViewModel trainStationViewModel = isDeparture? originStation: destinationStation;

        SpannableStringBuilder text = new SpannableStringBuilder();
        String stationId = String.valueOf(trainStationViewModel.getStationId());
        if (TextUtils.isEmpty(stationId)) {
            // id is more than one
            String cityCode = trainStationViewModel.getCityCode();
            if (TextUtils.isEmpty(cityCode)) {
                text.append(trainStationViewModel.getCityName());
                return makeBold(context, text);
            } else {
                text.append(cityCode);
            }
        } else {
            text.append(stationId);
        }
        makeBold(context, text);
        String cityName = trainStationViewModel.getCityName();
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
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
        parcel.writeParcelable(kaiPassengerViewModel, i);
        parcel.writeParcelable(originStation, i);
        parcel.writeParcelable(destinationStation, i);
    }

    public static class Builder {
        private String departureDate;
        private String departureDateFmt;
        private String returnDate;
        private String returnDateFmt;
        private boolean isOneWay;
        private TrainPassengerViewModel kaiPassengerViewModel;
        private TrainStationViewModel originStation;
        private TrainStationViewModel destinationStation;
        private String passengerFmt;

        public Builder() {
        }

        public Builder setIsOneWay(boolean isOneWay) {
            this.isOneWay = isOneWay;
            return this;
        }

        public Builder setOriginStation(TrainStationViewModel originStation) {
            this.originStation = originStation;
            return this;
        }

        public Builder setDestinationStation(TrainStationViewModel destinationStation) {
            this.destinationStation = destinationStation;
            return this;
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

        public Builder setKAIPassengerViewModel(TrainPassengerViewModel kaiPassengerViewModel) {
            this.kaiPassengerViewModel = kaiPassengerViewModel;
            return this;
        }

        public Builder setPassengerFmt(String passengerFmt) {
            this.passengerFmt = passengerFmt;
            return this;
        }

        public TrainHomepageViewModel build() {
            return new TrainHomepageViewModel(
                    isOneWay,
                    originStation,
                    destinationStation,
                    departureDate,
                    departureDateFmt,
                    returnDate,
                    returnDateFmt,
                    kaiPassengerViewModel,
                    passengerFmt
            );
        }

    }
}
