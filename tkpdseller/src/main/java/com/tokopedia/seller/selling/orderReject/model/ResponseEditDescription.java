package com.tokopedia.core.selling.orderReject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Toped10 on 6/9/2016.
 */
@Parcel
public class ResponseEditDescription {

    /**
     * status : OK
     * config : null
     * server_process_time : 0.652828
     * data : {"is_success":1}
     */

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("config")
    @Expose
    String config;

    @SerializedName("server_process_time")
    @Expose
    String server_process_time;

    @SerializedName("message_error")
    @Expose
    List<String> message_error;
    /**
     * is_success : 1
     */

    @SerializedName("data")
    @Expose
    DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getServer_process_time() {
        return server_process_time;
    }

    public void setServer_process_time(String server_process_time) {
        this.server_process_time = server_process_time;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @Parcel
    public static class DataBean {
        @SerializedName("is_success")
        @Expose
        int is_success;

        public int getIs_success() {
            return is_success;
        }

        public void setIs_success(int is_success) {
            this.is_success = is_success;
        }
    }

    public List<String> getMessage_error() {
        return message_error;
    }

    public void setMessage_error(List<String> message_error) {
        this.message_error = message_error;
    }
}
