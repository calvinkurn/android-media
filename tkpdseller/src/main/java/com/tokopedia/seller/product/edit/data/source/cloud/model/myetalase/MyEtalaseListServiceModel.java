
package com.tokopedia.seller.product.edit.data.source.cloud.model.myetalase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyEtalaseListServiceModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("config")
    @Expose
    private Object config;
    @SerializedName("data")
    @Expose
    private MyEtalaseData data;
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

    public MyEtalaseData getData() {
        return data;
    }

    public void setData(MyEtalaseData data) {
        this.data = data;
    }

    public String getServerProcessTime() {
        return serverProcessTime;
    }

    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

}
