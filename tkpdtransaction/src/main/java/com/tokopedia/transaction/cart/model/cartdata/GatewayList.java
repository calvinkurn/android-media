package com.tokopedia.transaction.cart.model.cartdata;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.R;

public class GatewayList implements Parcelable {

    @SerializedName("toppay_flag")
    @Expose
    private String toppayFlag;
    @SerializedName("gateway")
    @Expose
    private Integer gateway;
    @SerializedName("gateway_image")
    @Expose
    private String gatewayImage;
    @SerializedName("gateway_name")
    @Expose
    private String gatewayName;
    @SerializedName("gateway_desc")
    @Expose
    private String gatewayDesc;

    public String getToppayFlag() {
        return toppayFlag;
    }

    public void setToppayFlag(String toppayFlag) {
        this.toppayFlag = toppayFlag;
    }

    public Integer getGateway() {
        return gateway;
    }

    public void setGateway(Integer gateway) {
        this.gateway = gateway;
    }

    public String getGatewayImage() {
        return gatewayImage;
    }

    public void setGatewayImage(String gatewayImage) {
        this.gatewayImage = gatewayImage;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }

    public String getFeeInformation(Context context) {
        if (this.gatewayDesc != null) return gatewayDesc;
        switch (gateway) {
            case 10:
                return context.getString(R.string.msg_tokopedia_indomaret_fee);
            case 12:
                return context.getString(R.string.installment_payment_label);
            case 8:
                return context.getString(R.string.msg_tokopedia_cc_fee);
            default:
                return context.getString(R.string.msg_tokopedia_free);
        }
    }

    public String getGatewayDesc() {
        return gatewayDesc;
    }

    public void setGatewayDesc(String gatewayDesc) {
        this.gatewayDesc = gatewayDesc;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.toppayFlag);
        dest.writeValue(this.gateway);
        dest.writeString(this.gatewayImage);
        dest.writeString(this.gatewayName);
        dest.writeString(this.gatewayDesc);
    }

    public GatewayList() {
    }

    protected GatewayList(Parcel in) {
        this.toppayFlag = in.readString();
        this.gateway = (Integer) in.readValue(Integer.class.getClassLoader());
        this.gatewayImage = in.readString();
        this.gatewayName = in.readString();
        this.gatewayDesc = in.readString();
    }

    public static final Creator<GatewayList> CREATOR = new Creator<GatewayList>() {
        @Override
        public GatewayList createFromParcel(Parcel source) {
            return new GatewayList(source);
        }

        @Override
        public GatewayList[] newArray(int size) {
            return new GatewayList[size];
        }
    };
}
