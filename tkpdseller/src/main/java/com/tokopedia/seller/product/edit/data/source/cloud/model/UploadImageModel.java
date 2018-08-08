package com.tokopedia.seller.product.edit.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zulfikarrahman on 3/22/17.
 */

public class UploadImageModel {

    @SerializedName("message_error")
    @Expose
    private List<String> messageError = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("config")
    @Expose
    private Object config;
    @SerializedName("server_process_time")
    @Expose
    private String serverProcessTime;
    @SerializedName(value = "result", alternate = "data")
    @Expose
    private ResultUploadImage result;

    public List<String> getMessageError() {
        return messageError;
    }

    public void setMessageError(List<String> messageError) {
        this.messageError = messageError;
    }

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

    public ResultUploadImage getResult() {
        return result;
    }

    public void setResult(ResultUploadImage result) {
        this.result = result;
    }

}
