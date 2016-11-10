
package com.tokopedia.core.selling.model.modelConfirmShipping;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Data {

    @SerializedName("is_success")
    @Expose
    Integer isSuccess;

    /**
     * 
     * @return
     *     The isSuccess
     */
    public Integer getIsSuccess() {
        return isSuccess;
    }

    /**
     * 
     * @param isSuccess
     *     The is_success
     */
    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }

}
