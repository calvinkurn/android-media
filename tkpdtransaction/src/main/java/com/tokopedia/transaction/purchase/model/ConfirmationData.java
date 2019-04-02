package com.tokopedia.transaction.purchase.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by herdimac on 4/8/16.
 * modified by Angga.Prasetiyo implement parcelable
 */
public class ConfirmationData implements Parcelable {

    @SerializedName("payment_detail")
    @Expose
    private PaymentDetail paymentDetail;
    @SerializedName("is_success")
    @Expose
    private Integer isSuccess;

    /**
     * @return The paymentDetail
     */
    public PaymentDetail getPaymentDetail() {
        return paymentDetail;
    }

    /**
     * @param paymentDetail The payment_detail
     */
    public void setPaymentDetail(PaymentDetail paymentDetail) {
        this.paymentDetail = paymentDetail;
    }

    /**
     * @return The getResponseCode
     */
    public Integer getIsSuccess() {
        return isSuccess;
    }

    /**
     * @param isSuccess The is_success
     */
    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }

    protected ConfirmationData(Parcel in) {
        paymentDetail = (PaymentDetail) in.readValue(PaymentDetail.class.getClassLoader());
        isSuccess = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(paymentDetail);
        if (isSuccess == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(isSuccess);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ConfirmationData> CREATOR
            = new Parcelable.Creator<ConfirmationData>() {
        @Override
        public ConfirmationData createFromParcel(Parcel in) {
            return new ConfirmationData(in);
        }

        @Override
        public ConfirmationData[] newArray(int size) {
            return new ConfirmationData[size];
        }
    };

}
