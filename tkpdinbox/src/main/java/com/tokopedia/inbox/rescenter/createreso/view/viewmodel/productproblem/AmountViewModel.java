package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 14/08/17.
 */

public class AmountViewModel implements Parcelable {
    private String idr;
    private int integer;

    public AmountViewModel(String idr, int integer) {
        this.idr = idr;
        this.integer = integer;
    }

    public String getIdr() {
        return idr;
    }

    public void setIdr(String idr) {
        this.idr = idr;
    }

    public int getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.idr);
        dest.writeInt(this.integer);
    }

    protected AmountViewModel(Parcel in) {
        this.idr = in.readString();
        this.integer = in.readInt();
    }

    public static final Creator<AmountViewModel> CREATOR = new Creator<AmountViewModel>() {
        @Override
        public AmountViewModel createFromParcel(Parcel source) {
            return new AmountViewModel(source);
        }

        @Override
        public AmountViewModel[] newArray(int size) {
            return new AmountViewModel[size];
        }
    };
}
