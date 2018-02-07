
package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.most_helpful_review;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response implements Parcelable{

    @SerializedName("response_time_fmt")
    @Expose
    private String responseTimeFmt;
    @SerializedName("response_msg")
    @Expose
    private String responseMsg;

    protected Response(Parcel in) {
        responseTimeFmt = in.readString();
        responseMsg = in.readString();
    }

    public static final Creator<Response> CREATOR = new Creator<Response>() {
        @Override
        public Response createFromParcel(Parcel in) {
            return new Response(in);
        }

        @Override
        public Response[] newArray(int size) {
            return new Response[size];
        }
    };

    /**
     * 
     * @return
     *     The responseTimeFmt
     */
    public String getResponseTimeFmt() {
        return responseTimeFmt;
    }

    /**
     * 
     * @param responseTimeFmt
     *     The response_time_fmt
     */
    public void setResponseTimeFmt(String responseTimeFmt) {
        this.responseTimeFmt = responseTimeFmt;
    }

    /**
     * 
     * @return
     *     The responseMsg
     */
    public String getResponseMsg() {
        return responseMsg;
    }

    /**
     * 
     * @param responseMsg
     *     The response_msg
     */
    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(responseTimeFmt);
        dest.writeString(responseMsg);
    }
}
