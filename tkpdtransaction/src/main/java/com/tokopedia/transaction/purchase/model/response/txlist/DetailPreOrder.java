package com.tokopedia.transaction.purchase.model.response.txlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 21/04/2016.
 */
public class DetailPreOrder implements Parcelable {
    private static final String TAG = DetailPreOrder.class.getSimpleName();

    @SerializedName("preorder_status")
    @Expose
    private Integer preorderStatus;
    @SerializedName("preorder_process_time_type")
    @Expose
    private String preorderProcessTimeType;
    @SerializedName("preorder_process_time_type_string")
    @Expose
    private String preorderProcessTimeTypeString;
    @SerializedName("preorder_process_time")
    @Expose
    private String preorderProcessTime;

    public Integer getPreorderStatus() {
        return preorderStatus;
    }

    public void setPreorderStatus(Integer preorderStatus) {
        this.preorderStatus = preorderStatus;
    }

    public String getPreorderProcessTimeType() {
        return preorderProcessTimeType;
    }

    public void setPreorderProcessTimeType(String preorderProcessTimeType) {
        this.preorderProcessTimeType = preorderProcessTimeType;
    }

    public String getPreorderProcessTimeTypeString() {
        return preorderProcessTimeTypeString;
    }

    public void setPreorderProcessTimeTypeString(String preorderProcessTimeTypeString) {
        this.preorderProcessTimeTypeString = preorderProcessTimeTypeString;
    }

    public String getPreorderProcessTime() {
        return preorderProcessTime;
    }

    public void setPreorderProcessTime(String preorderProcessTime) {
        this.preorderProcessTime = preorderProcessTime;
    }

    protected DetailPreOrder(Parcel in) {
        preorderStatus = in.readByte() == 0x00 ? null : in.readInt();
        preorderProcessTimeType = in.readString();
        preorderProcessTimeTypeString = in.readString();
        preorderProcessTime = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (preorderStatus == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(preorderStatus);
        }
        dest.writeString(preorderProcessTimeType);
        dest.writeString(preorderProcessTimeTypeString);
        dest.writeString(preorderProcessTime);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DetailPreOrder> CREATOR = new Parcelable.Creator<DetailPreOrder>() {
        @Override
        public DetailPreOrder createFromParcel(Parcel in) {
            return new DetailPreOrder(in);
        }

        @Override
        public DetailPreOrder[] newArray(int size) {
            return new DetailPreOrder[size];
        }
    };
}
