package com.tokopedia.ride.bookingride.view.adapter.viewmodel;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.ride.bookingride.view.adapter.factory.PaymentMethodTypeFactory;

/**
 * Created by alvarisi on 5/3/17.
 */

public class PaymentMethodViewModel implements Visitable<PaymentMethodTypeFactory>, Parcelable {
    public static String MODE_CC = "cc";
    public static String MODE_WALLET = "wallet";


    private String name;
    private boolean isActive;
    private String type;
    private String imageUrl;
    private String expiryYear;
    private String expiryMonth;
    private String deleteUrl;
    private Bundle deleteBody;


    public PaymentMethodViewModel() {
    }

    protected PaymentMethodViewModel(Parcel in) {
        name = in.readString();
        isActive = in.readByte() != 0;
        type = in.readString();
        imageUrl = in.readString();
        expiryMonth = in.readString();
        expiryYear = in.readString();
        deleteUrl = in.readString();
        deleteBody = in.readBundle();
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getDeleteUrl() {
        return deleteUrl;
    }

    public void setDeleteUrl(String deleteUrl) {
        this.deleteUrl = deleteUrl;
    }

    public Bundle getDeleteBody() {
        return deleteBody;
    }

    public void setDeleteBody(Bundle deleteBody) {
        this.deleteBody = deleteBody;
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
        dest.writeString(imageUrl);
        dest.writeString(expiryYear);
        dest.writeString(expiryMonth);
        dest.writeString(deleteUrl);
        dest.writeBundle(deleteBody);
    }
}
