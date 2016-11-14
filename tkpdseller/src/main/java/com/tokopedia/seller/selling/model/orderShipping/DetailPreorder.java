
package com.tokopedia.seller.selling.model.orderShipping;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel(parcelsIndex = false)
public class DetailPreorder {

    @SerializedName("preorder_status")
    @Expose
    Integer preorderStatus;
    @SerializedName("preorder_process_time_type")
    @Expose
    String preorderProcessTimeType;
    @SerializedName("preorder_process_time_type_string")
    @Expose
    String preorderProcessTimeTypeString;
    @SerializedName("preorder_process_time")
    @Expose
    String preorderProcessTime;

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
}
