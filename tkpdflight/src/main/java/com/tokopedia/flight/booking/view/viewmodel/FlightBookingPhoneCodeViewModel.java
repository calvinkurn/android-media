package com.tokopedia.flight.booking.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.BaseListTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingPhoneCodeViewModel implements ItemType, Parcelable, Visitable<BaseListTypeFactory<FlightBookingPhoneCodeViewModel>> {

    private String countryId;
    private String countryName;
    private String countryPhoneCode;

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryPhoneCode() {
        return countryPhoneCode;
    }

    public void setCountryPhoneCode(String countryPhoneCode) {
        this.countryPhoneCode = countryPhoneCode;
    }

    public static final int TYPE = 932;

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.countryId);
        dest.writeString(this.countryName);
        dest.writeString(this.countryPhoneCode);
    }

    public FlightBookingPhoneCodeViewModel() {
    }

    protected FlightBookingPhoneCodeViewModel(Parcel in) {
        this.countryId = in.readString();
        this.countryName = in.readString();
        this.countryPhoneCode = in.readString();
    }

    public static final Parcelable.Creator<FlightBookingPhoneCodeViewModel> CREATOR = new Parcelable.Creator<FlightBookingPhoneCodeViewModel>() {
        @Override
        public FlightBookingPhoneCodeViewModel createFromParcel(Parcel source) {
            return new FlightBookingPhoneCodeViewModel(source);
        }

        @Override
        public FlightBookingPhoneCodeViewModel[] newArray(int size) {
            return new FlightBookingPhoneCodeViewModel[size];
        }
    };

    @Override
    public int type(BaseListTypeFactory<FlightBookingPhoneCodeViewModel> typeFactory) {
        return typeFactory.type(this);
    }
}
