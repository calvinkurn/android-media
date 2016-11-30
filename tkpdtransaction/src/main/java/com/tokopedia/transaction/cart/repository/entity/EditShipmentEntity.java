package com.tokopedia.transaction.cart.repository.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 11/30/16.
 */

public class EditShipmentEntity implements Parcelable {
    @SerializedName("message_status")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public EditShipmentEntity() {
    }

    protected EditShipmentEntity(Parcel in) {
        message = in.readString();
        status = in.readString();
    }

    public static final Creator<EditShipmentEntity> CREATOR = new Creator<EditShipmentEntity>() {
        @Override
        public EditShipmentEntity createFromParcel(Parcel in) {
            return new EditShipmentEntity(in);
        }

        @Override
        public EditShipmentEntity[] newArray(int size) {
            return new EditShipmentEntity[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(message);
        parcel.writeString(status);
    }
}
