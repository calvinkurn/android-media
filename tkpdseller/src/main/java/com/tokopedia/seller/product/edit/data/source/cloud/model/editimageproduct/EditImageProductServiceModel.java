
package com.tokopedia.seller.product.edit.data.source.cloud.model.editimageproduct;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditImageProductServiceModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("config")
    @Expose
    private Object config;
    @SerializedName("data")
    @Expose
    private EditIageProductDataServiceModule data;
    @SerializedName("server_process_time")
    @Expose
    private String serverProcessTime;

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

    public EditIageProductDataServiceModule getData() {
        return data;
    }

    public void setData(EditIageProductDataServiceModule data) {
        this.data = data;
    }

    public String getServerProcessTime() {
        return serverProcessTime;
    }

    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

}
