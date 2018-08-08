package com.tokopedia.transaction.cart.model.toppaydata;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 11/23/16.
 */

public class TopPayParameterData implements Parcelable {
    @SerializedName("query_string")
    @Expose
    private String queryString;
    @SerializedName("redirect_url")
    @Expose
    private String redirectUrl;
    @SerializedName("parameter")
    @Expose
    private Parameter parameter;
    @SerializedName("callback_url")
    @Expose
    private String callbackUrl;

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

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getCallbackUrlPath() {
        try {
            Uri uri = Uri.parse(callbackUrl);
            return uri.getPath();
        } catch (Exception e) {
            return "wrong";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.queryString);
        dest.writeString(this.redirectUrl);
        dest.writeParcelable(this.parameter, flags);
        dest.writeString(this.callbackUrl);
    }

    public TopPayParameterData() {
    }

    protected TopPayParameterData(Parcel in) {
        this.queryString = in.readString();
        this.redirectUrl = in.readString();
        this.parameter = in.readParcelable(Parameter.class.getClassLoader());
        this.callbackUrl = in.readString();
    }

    public static final Parcelable.Creator<TopPayParameterData> CREATOR = new Parcelable.Creator<TopPayParameterData>() {
        @Override
        public TopPayParameterData createFromParcel(Parcel source) {
            return new TopPayParameterData(source);
        }

        @Override
        public TopPayParameterData[] newArray(int size) {
            return new TopPayParameterData[size];
        }
    };
}
