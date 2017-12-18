package com.tokopedia.loyalty.domain.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 12/5/17. Tokopedia
 */

public class VoucherResponse implements Parcelable{

    @SerializedName("data_voucher")
    private Voucher voucher;

    protected VoucherResponse(Parcel in) {
        voucher = in.readParcelable(Voucher.class.getClassLoader());
    }

    public static final Creator<VoucherResponse> CREATOR = new Creator<VoucherResponse>() {
        @Override
        public VoucherResponse createFromParcel(Parcel in) {
            return new VoucherResponse(in);
        }

        @Override
        public VoucherResponse[] newArray(int size) {
            return new VoucherResponse[size];
        }
    };

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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(voucher, i);
    }
}
