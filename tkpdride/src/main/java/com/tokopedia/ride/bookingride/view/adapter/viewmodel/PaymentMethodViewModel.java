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
    private String name;
    private String cardType;
    private boolean isActive;
    private String type;
    private String imageUrl;
    private String expiryYear;
    private String expiryMonth;
    private String deleteUrl;
    private Bundle deleteBody;
    private String saveurl;
    private Bundle saveBody;
    private String tokoCashBalance;
    private boolean saveWebView;
    private String bankImage;


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
        saveurl = in.readString();
        saveBody = in.readBundle();
        cardType = in.readString();
        tokoCashBalance = in.readString();
        saveWebView = in.readByte() != 0;
        bankImage = in.readString();
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

    public String getSaveurl() {
        return saveurl;
    }

    public void setSaveurl(String saveurl) {
        this.saveurl = saveurl;
    }

    public Bundle getSaveBody() {
        return saveBody;
    }

    public void setSaveBody(Bundle saveBody) {
        this.saveBody = saveBody;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getTokoCashBalance() {
        return tokoCashBalance;
    }

    public void setTokoCashBalance(String tokoCashBalance) {
        this.tokoCashBalance = tokoCashBalance;
    }

    @Override
    public int type(PaymentMethodTypeFactory paymentMethodTypeFactory) {
        return paymentMethodTypeFactory.type(this);
    }

    public boolean isSaveWebView() {
        return saveWebView;
    }

    public void setSaveWebView(boolean saveWebView) {
        this.saveWebView = saveWebView;
    }

    public String getBankImage() {
        return bankImage;
    }

    public void setBankImage(String bankImage) {
        this.bankImage = bankImage;
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
        dest.writeString(saveurl);
        dest.writeBundle(saveBody);
        dest.writeString(cardType);
        dest.writeString(tokoCashBalance);
        dest.writeByte((byte) (saveWebView ? 1 : 0));
        dest.writeString(bankImage);
    }
}
