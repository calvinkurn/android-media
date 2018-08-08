package com.tokopedia.transaction.cart.interactor.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author  by alvarisi on 11/30/16.
 */

public class EditShipmentEntity{
    @SerializedName("message_status")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public EditShipmentEntity() {
    }

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
}
