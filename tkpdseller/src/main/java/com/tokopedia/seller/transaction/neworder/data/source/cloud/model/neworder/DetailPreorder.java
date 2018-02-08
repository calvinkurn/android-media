
package com.tokopedia.seller.transaction.neworder.data.source.cloud.model.neworder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailPreorder {

    @SerializedName("preorder_status")
    @Expose
    private int preorderStatus;
    @SerializedName("preorder_process_time_type")
    @Expose
    private String preorderProcessTimeType;
    @SerializedName("preorder_process_time_type_string")
    @Expose
    private String preorderProcessTimeTypeString;
    @SerializedName("preorder_process_time")
    @Expose
    private String preorderProcessTime;

    public int getPreorderStatus() {
        return preorderStatus;
    }

    public void setPreorderStatus(int preorderStatus) {
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
