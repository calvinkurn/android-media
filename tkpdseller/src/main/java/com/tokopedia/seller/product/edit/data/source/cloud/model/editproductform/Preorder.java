
package com.tokopedia.seller.product.edit.data.source.cloud.model.editproductform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Preorder {

    @SerializedName("preorder_status")
    @Expose
    private int preorderStatus;
    @SerializedName("preorder_process_time_type")
    @Expose
    private int preorderProcessTimeType;
    @SerializedName("preorder_process_time_type_string")
    @Expose
    private String preorderProcessTimeTypeString;
    @SerializedName("preorder_process_time")
    @Expose
    private int preorderProcessTime;

    public int getPreorderStatus() {
        return preorderStatus;
    }

    public void setPreorderStatus(int preorderStatus) {
        this.preorderStatus = preorderStatus;
    }

    public int getPreorderProcessTimeType() {
        return preorderProcessTimeType;
    }

    public void setPreorderProcessTimeType(int preorderProcessTimeType) {
        this.preorderProcessTimeType = preorderProcessTimeType;
    }

    public String getPreorderProcessTimeTypeString() {
        return preorderProcessTimeTypeString;
    }

    public void setPreorderProcessTimeTypeString(String preorderProcessTimeTypeString) {
        this.preorderProcessTimeTypeString = preorderProcessTimeTypeString;
    }

    public int getPreorderProcessTime() {
        return preorderProcessTime;
    }

    public void setPreorderProcessTime(int preorderProcessTime) {
        this.preorderProcessTime = preorderProcessTime;
    }

}
