package com.tokopedia.seller.selling.orderReject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Toped10 on 6/9/2016.
 */
@Parcel
public class DataResponseReject {
    public static final String MODEL_DATA_REJECT_RESPONSE_KEY = "model_reject_response_key";

    @SerializedName("is_success")
    @Expose
    Integer isSuccess;

    /**
     * @return The isSuccess
     */
    public Integer getIsSuccess() {
        return isSuccess;
    }

    /**
     * @param isSuccess The is_success
     */
    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }

}
