package com.tokopedia.transaction.cart.model.voucher;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 12/1/16.
 */

public class VoucherData implements Parcelable {
    @SerializedName("data_voucher")
    private Voucher voucher;

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.voucher, flags);
    }

    public VoucherData() {
    }

    protected VoucherData(Parcel in) {
        this.voucher = in.readParcelable(Voucher.class.getClassLoader());
    }

    public static final Parcelable.Creator<VoucherData> CREATOR = new Parcelable.Creator<VoucherData>() {
        @Override
        public VoucherData createFromParcel(Parcel source) {
            return new VoucherData(source);
        }

        @Override
        public VoucherData[] newArray(int size) {
            return new VoucherData[size];
        }
    };
}
