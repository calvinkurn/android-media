package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 08/11/17.
 */
public class AmountData implements Parcelable {
    public static final Parcelable.Creator<AmountData> CREATOR = new Parcelable.Creator<AmountData>() {
        @Override
        public AmountData createFromParcel(Parcel source) {
            return new AmountData(source);
        }

        @Override
        public AmountData[] newArray(int size) {
            return new AmountData[size];
        }
    };
    private String idr;
    private int integer;


    public AmountData(String idr, int integer) {
        this.idr = idr;
        this.integer = integer;
    }

    protected AmountData(Parcel in) {
        this.idr = in.readString();
        this.integer = in.readInt();
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
}
