package com.tokopedia.tokocash.historytokocash.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 8/23/17.
 */

public class ParamsActionHistory implements Parcelable {

    private long amount;

    private String amountFormatted;

    private String refundId;

    private String refundType;

    public ParamsActionHistory() {
    }

    protected ParamsActionHistory(Parcel in) {
        amount = in.readLong();
        amountFormatted = in.readString();
        refundId = in.readString();
        refundType = in.readString();
    }

    public static final Creator<ParamsActionHistory> CREATOR = new Creator<ParamsActionHistory>() {
        @Override
        public ParamsActionHistory createFromParcel(Parcel in) {
            return new ParamsActionHistory(in);
        }

        @Override
        public ParamsActionHistory[] newArray(int size) {
            return new ParamsActionHistory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(amount);
        parcel.writeString(amountFormatted);
        parcel.writeString(refundId);
        parcel.writeString(refundType);
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getAmountFormatted() {
        return amountFormatted;
    }

    public void setAmountFormatted(String amountFormatted) {
        this.amountFormatted = amountFormatted;
    }
}
