package com.tokopedia.seller.shop.open.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.Transient;

/**
 * Created by ZulfikarRahman on 1/5/2018.
 */

public class ResponseOpenShopPicture {

    public ResponseOpenShopPicture(){}

    @SerializedName("message_error")
    @Expose
    String[] message_error;

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("data")
    @Expose
    Data data;

    @SerializedName("config")
    @Expose
    String config;

    @SerializedName("server_process_time")
    @Expose
    String server_process_time;

    @Parcel
    public static class Data{

        /**
         * This is for parcelable
         */
        public Data(){}

        @Transient
        public static final int SUCCESS = 1;

        @SerializedName("is_success")
        @Expose
        String is_success;

        @SerializedName("file_uploaded")
        @Expose
        String file_uploaded;

        public String getIs_success() {
            return is_success;
        }

        public void setIs_success(String is_success) {
            this.is_success = is_success;
        }

        public String getFile_uploaded() {
            return file_uploaded;
        }

        public void setFile_uploaded(String file_uploaded) {
            this.file_uploaded = file_uploaded;
        }
    }

    public String[] getMessage_error() {
        return message_error;
    }

    public void setMessage_error(String[] message_error) {
        this.message_error = message_error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
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
}
