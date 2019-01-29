package com.tokopedia.transaction.common.data.cartcheckout;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 26/02/18.
 */

public class CheckoutData implements Parcelable {

    private boolean isError;
    private String errorMessage;

    private String paymentId;
    private String queryString;
    private String redirectUrl;
    private String callbackSuccessUrl;
    private String callbackFailedUrl;
    private String transactionId;

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getCallbackSuccessUrl() {
        return callbackSuccessUrl;
    }

    public void setCallbackSuccessUrl(String callbackSuccessUrl) {
        this.callbackSuccessUrl = callbackSuccessUrl;
    }

    public String getCallbackFailedUrl() {
        return callbackFailedUrl;
    }

    public void setCallbackFailedUrl(String callbackFailedUrl) {
        this.callbackFailedUrl = callbackFailedUrl;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }


    public CheckoutData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isError ? (byte) 1 : (byte) 0);
        dest.writeString(this.errorMessage);
        dest.writeString(this.paymentId);
        dest.writeString(this.queryString);
        dest.writeString(this.redirectUrl);
        dest.writeString(this.callbackSuccessUrl);
        dest.writeString(this.callbackFailedUrl);
        dest.writeString(this.transactionId);
    }

    protected CheckoutData(Parcel in) {
        this.isError = in.readByte() != 0;
        this.errorMessage = in.readString();
        this.paymentId = in.readString();
        this.queryString = in.readString();
        this.redirectUrl = in.readString();
        this.callbackSuccessUrl = in.readString();
        this.callbackFailedUrl = in.readString();
        this.transactionId = in.readString();
    }

    public static final Creator<CheckoutData> CREATOR = new Creator<CheckoutData>() {
        @Override
        public CheckoutData createFromParcel(Parcel source) {
            return new CheckoutData(source);
        }

        @Override
        public CheckoutData[] newArray(int size) {
            return new CheckoutData[size];
        }
    };
}
