package com.tokopedia.seller.product.edit.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zulfikarrahman on 3/22/17.
 */

public class GenerateHostModel {

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("message_error")
    @Expose
    List<String> messageError;

    @SerializedName("data")
    @Expose
    DataGenerateHost data;

    @SerializedName("config")
    @Expose
    String config;

    @SerializedName("server_process_time")
    @Expose
    String server_process_time;

    public DataGenerateHost getData() {
        return data;
    }

    public void setData(DataGenerateHost data) {
        this.data = data;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public List<String> getMessageError() {
        return messageError;
    }

    public void setMessageError(List<String> messageError) {
        this.messageError = messageError;
    }

    public String getServer_process_time() {
        return server_process_time;
    }

    public void setServer_process_time(String server_process_time) {
        this.server_process_time = server_process_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
