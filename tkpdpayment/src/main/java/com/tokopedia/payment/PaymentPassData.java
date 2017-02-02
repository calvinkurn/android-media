package com.tokopedia.payment;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 2/2/17.
 */

public class PaymentPassData<T extends Parcelable> implements Parcelable {

    private String paymentId;
    private String queryString;
    private String redirectUrl;
    private String callbackUrl;
    private Parcelable detailData;

    @SuppressWarnings("unchecked")
    public T getDetailData() {
        return (T) detailData;
    }

    public void setDetailData(T detailData) {
        this.detailData = detailData;
    }

    public PaymentPassData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.paymentId);
        dest.writeString(this.queryString);
        dest.writeString(this.redirectUrl);
        dest.writeString(this.callbackUrl);
        dest.writeParcelable(this.detailData, flags);
    }

    protected PaymentPassData(Parcel in) {
        this.paymentId = in.readString();
        this.queryString = in.readString();
        this.redirectUrl = in.readString();
        this.callbackUrl = in.readString();
        this.detailData = in.readParcelable(Parcelable.class.getClassLoader());
    }

    public static final Creator<PaymentPassData> CREATOR = new Creator<PaymentPassData>() {
        @Override
        public PaymentPassData createFromParcel(Parcel source) {
            return new PaymentPassData(source);
        }

        @Override
        public PaymentPassData[] newArray(int size) {
            return new PaymentPassData[size];
        }
    };
}
