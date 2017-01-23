package com.tokopedia.transaction.cart.model.thankstoppaydata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 1/23/17.
 */

public class ThanksTopPayData implements Parcelable {

    @SerializedName("parameter")
    @Expose
    private Parameter parameter;
    @SerializedName("is_success")
    @Expose
    private int isSuccess;

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.parameter, flags);
        dest.writeInt(this.isSuccess);
    }

    public ThanksTopPayData() {
    }

    protected ThanksTopPayData(Parcel in) {
        this.parameter = in.readParcelable(Parameter.class.getClassLoader());
        this.isSuccess = in.readInt();
    }

    public static final Parcelable.Creator<ThanksTopPayData> CREATOR
            = new Parcelable.Creator<ThanksTopPayData>() {
        @Override
        public ThanksTopPayData createFromParcel(Parcel source) {
            return new ThanksTopPayData(source);
        }

        @Override
        public ThanksTopPayData[] newArray(int size) {
            return new ThanksTopPayData[size];
        }
    };
}
