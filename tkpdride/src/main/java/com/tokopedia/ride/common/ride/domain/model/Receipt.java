package com.tokopedia.ride.common.ride.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alvarisi on 3/31/17.
 */

public class Receipt implements Parcelable {
    private String requestId;
    private String currency;
    private String distance;
    private String distanceUnit;
    private String duration;
    private String subtotal;
    private String totalCharged;
    private String totalFare;
    private String totalOwe;
    private int duratuinInMinute;
    private String uberSignupUrl;
    private String uberSignupText;
    private String uberSignupTermsUrl;
    private float discount;
    private float cashback;
    private PendingPayment pendingPayment;
    private String cashbackDisplayFormat;
    private String discountDisplayFormat;
    private TipList tipList;
    private String paymentMethod;

    public Receipt() {
    }

    protected Receipt(Parcel in) {
        requestId = in.readString();
        currency = in.readString();
        distance = in.readString();
        distanceUnit = in.readString();
        duration = in.readString();
        subtotal = in.readString();
        totalCharged = in.readString();
        totalFare = in.readString();
        totalOwe = in.readString();
        duratuinInMinute = in.readInt();
        uberSignupUrl = in.readString();
        uberSignupText = in.readString();
        uberSignupTermsUrl = in.readString();
        discount = in.readFloat();
        cashback = in.readFloat();
        pendingPayment = in.readParcelable(PendingPayment.class.getClassLoader());
        cashbackDisplayFormat = in.readString();
        discountDisplayFormat = in.readString();
        tipList = in.readParcelable(TipList.class.getClassLoader());
        paymentMethod = in.readString();
    }

    public static final Creator<Receipt> CREATOR = new Creator<Receipt>() {
        @Override
        public Receipt createFromParcel(Parcel in) {
            return new Receipt(in);
        }

        @Override
        public Receipt[] newArray(int size) {
            return new Receipt[size];
        }
    };

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTotalCharged() {
        return totalCharged;
    }

    public void setTotalCharged(String totalCharged) {
        this.totalCharged = totalCharged;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public String getTotalOwe() {
        return totalOwe;
    }

    public void setTotalOwe(String totalOwe) {
        this.totalOwe = totalOwe;
    }

    public int getDuratuinInMinute() {
        return duratuinInMinute;
    }

    public void setDuratuinInMinute(int duratuinInMinute) {
        this.duratuinInMinute = duratuinInMinute;
    }

    public String getUberSignupUrl() {
        return uberSignupUrl;
    }

    public void setUberSignupUrl(String uberSignupUrl) {
        this.uberSignupUrl = uberSignupUrl;
    }

    public String getUberSignupText() {
        return uberSignupText;
    }

    public void setUberSignupText(String uberSignupText) {
        this.uberSignupText = uberSignupText;
    }

    public String getUberSignupTermsUrl() {
        return uberSignupTermsUrl;
    }

    public void setUberSignupTermsUrl(String uberSignupTermsUrl) {
        this.uberSignupTermsUrl = uberSignupTermsUrl;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getCashback() {
        return cashback;
    }

    public void setCashback(float cashback) {
        this.cashback = cashback;
    }

    public PendingPayment getPendingPayment() {
        return pendingPayment;
    }

    public void setPendingPayment(PendingPayment pendingPayment) {
        this.pendingPayment = pendingPayment;
    }

    public String getCashbackDisplayFormat() {
        return cashbackDisplayFormat;
    }

    public void setCashbackDisplayFormat(String cashbackDisplayFormat) {
        this.cashbackDisplayFormat = cashbackDisplayFormat;
    }

    public String getDiscountDisplayFormat() {
        return discountDisplayFormat;
    }

    public void setDiscountDisplayFormat(String discountDisplayFormat) {
        this.discountDisplayFormat = discountDisplayFormat;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public TipList getTipList() {
        return tipList;
    }

    public void setTipList(TipList tipList) {
        this.tipList = tipList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(requestId);
        parcel.writeString(currency);
        parcel.writeString(distance);
        parcel.writeString(distanceUnit);
        parcel.writeString(duration);
        parcel.writeString(subtotal);
        parcel.writeString(totalCharged);
        parcel.writeString(totalFare);
        parcel.writeString(totalOwe);
        parcel.writeInt(duratuinInMinute);
        parcel.writeString(uberSignupUrl);
        parcel.writeString(uberSignupText);
        parcel.writeString(uberSignupTermsUrl);
        parcel.writeFloat(discount);
        parcel.writeFloat(cashback);
        parcel.writeParcelable(pendingPayment, i);
        parcel.writeString(cashbackDisplayFormat);
        parcel.writeString(discountDisplayFormat);
        parcel.writeParcelable(tipList, i);
        parcel.writeString(paymentMethod);
    }
}
