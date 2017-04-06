
package com.tokopedia.seller.product.data.source.cloud.model.myetalase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyEtalaseListServiceModel {

    @SerializedName("config")
    @Expose
    private Object config;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("server_process_time")
    @Expose
    private String serverProcessTime;
    @SerializedName("data")
    @Expose
    private Data data;

    public Object getConfig() {
        return config;
    }

    public void setConfig(Object config) {
        this.config = config;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServerProcessTime() {
        return serverProcessTime;
    }

    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
