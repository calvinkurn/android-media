
package com.tokopedia.seller.product.edit.data.source.cloud.model.addproductsubmit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddProductSubmitServiceModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("config")
    @Expose
    private Object config;
    @SerializedName("server_process_time")
    @Expose
    private String serverProcessTime;
    @SerializedName("data")
    @Expose
    private AddProductSubmitResult addProductSubmitResult;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getConfig() {
        return config;
    }

    public void setConfig(Object config) {
        this.config = config;
    }

    public String getServerProcessTime() {
        return serverProcessTime;
    }

    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    public AddProductSubmitResult getAddProductSubmitResult() {
        return addProductSubmitResult;
    }

    public void setAddProductSubmitResult(AddProductSubmitResult addProductSubmitResult) {
        this.addProductSubmitResult = addProductSubmitResult;
    }

}
