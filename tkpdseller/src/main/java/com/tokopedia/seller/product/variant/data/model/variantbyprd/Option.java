package com.tokopedia.seller.product.variant.data.model.variantbyprd;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 8/15/2017.
 * "option":[{"pvo_id":2184932,"v_id":1,"vu_id":0,"vuv_id":0,"value":"custom merah","status":1,"hex":"","picture":null},{"pvo_id":2184931,"v_id":1,"vu_id":0,"vuv_id":0,"value":"custom biru","status":1,"hex":"","picture":null}]
 */

public class Option implements Parcelable {

    @SerializedName("pvo_id")
    @Expose
    private int pvoId;
    @SerializedName("v_id")
    @Expose
    private int vId;
    @SerializedName("vu_id")
    @Expose
    private int vuId;
    @SerializedName("vuv_id")
    @Expose
    private int vuvId;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("hex")
    @Expose
    private String hex;
    @SerializedName("picture")
    @Expose
    private String picture;

    public int getPvoId() {
        return pvoId;
    }

    public void setPvoId(int pvoId) {
        this.pvoId = pvoId;
    }

    public Integer getVId() {
        return vId;
    }

    public void setVId(int vId) {
        this.vId = vId;
    }

    public int getVuId() {
        return vuId;
    }

    public void setVuId(int vuId) {
        this.vuId = vuId;
    }

    public int getVuvId() {
        return vuvId;
    }

    public void setVuvId(int vuvId) {
        this.vuvId = vuvId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.pvoId);
        dest.writeInt(this.vId);
        dest.writeInt(this.vuId);
        dest.writeInt(this.vuvId);
        dest.writeString(this.value);
        dest.writeInt(this.status);
        dest.writeString(this.hex);
        dest.writeString(this.picture);
    }

    public Option() {
    }

    protected Option(Parcel in) {
        this.pvoId = in.readInt();
        this.vId = in.readInt();
        this.vuId = in.readInt();
        this.vuvId = in.readInt();
        this.value = in.readString();
        this.status = in.readInt();
        this.hex = in.readString();
        this.picture = in.readString();
    }

    public static final Parcelable.Creator<Option> CREATOR = new Parcelable.Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel source) {
            return new Option(source);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[size];
        }
    };
}