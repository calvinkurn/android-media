package com.tokopedia.seller.product.edit.data.source.cloud.model.catalogdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry 4/5/2017
 */
public class CatalogDataModel {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("result")
    @Expose
    private Result result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

}
