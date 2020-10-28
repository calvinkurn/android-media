package com.tokopedia.seller.manageitem.data.cloud.model.catalog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.manageitem.data.model.Result;

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
