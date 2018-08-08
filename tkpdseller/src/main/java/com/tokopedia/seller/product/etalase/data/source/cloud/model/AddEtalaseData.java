
package com.tokopedia.seller.product.etalase.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddEtalaseData {

    @SerializedName("etalase_id")
    @Expose
    private String etalaseId;
    @SerializedName("is_success")
    @Expose
    private Integer isSuccess;

    public String getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(String etalaseId) {
        this.etalaseId = etalaseId;
    }

    public Integer getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }

}
