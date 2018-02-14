
package com.tokopedia.seller.product.edit.view.model.edit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductPreorderViewModel {

    @SerializedName("preorder_process_time")
    @Expose
    private long preorderProcessTime;
    @SerializedName("preorder_time_unit")
    @Expose
    private long preorderTimeUnit; //1:day, 2:week
    @SerializedName("preorder_status")
    @Expose
    private long preorderStatus; // 1:active; -1: not active

    public long getPreorderProcessTime() {
        return preorderProcessTime;
    }

    public void setPreorderProcessTime(long preorderProcessTime) {
        this.preorderProcessTime = preorderProcessTime;
    }

    public long getPreorderTimeUnit() {
        return preorderTimeUnit;
    }

    public void setPreorderTimeUnit(long preorderTimeUnit) {
        this.preorderTimeUnit = preorderTimeUnit;
    }

    public long getPreorderStatus() {
        return preorderStatus;
    }

    public void setPreorderStatus(long preorderStatus) {
        this.preorderStatus = preorderStatus;
    }

}
