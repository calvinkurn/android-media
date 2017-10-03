package com.tokopedia.ride.bookingride.view.adapter.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.ride.bookingride.view.adapter.factory.PaymentMethodTypeFactory;

/**
 * Created by alvarisi on 5/3/17.
 */

public class PaymentMethodViewModel implements Visitable<PaymentMethodTypeFactory>,Parcelable {
    String name;
    boolean isActive;
    String type;

    public PaymentMethodViewModel(String name, boolean isActive, String type) {
        this.name = name;
        this.isActive = isActive;
        this.type = type;
    }

    protected PaymentMethodViewModel(Parcel in) {
        name = in.readString();
        isActive = in.readByte() != 0;
        type = in.readString();
    }

    public static final Creator<PaymentMethodViewModel> CREATOR = new Creator<PaymentMethodViewModel>() {
        @Override
        public PaymentMethodViewModel createFromParcel(Parcel in) {
            return new PaymentMethodViewModel(in);
        }

        @Override
        public PaymentMethodViewModel[] newArray(int size) {
            return new PaymentMethodViewModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int type(PaymentMethodTypeFactory paymentMethodTypeFactory) {
        return paymentMethodTypeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeString(type);
    }
}
