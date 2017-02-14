package com.tokopedia.seller.myproduct.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by m.normansyah on 18/01/2016.
 */
public class EditProductPictureModel {
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

    @SerializedName("message_error")
    @Expose
    ArrayList<String> messageError;

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

    public ArrayList<String> getMessageError() {
        return messageError;
    }

    public void setMessageError(ArrayList<String> messageError) {
        this.messageError = messageError;
    }

    public class Data {
        @SerializedName("is_success")
        @Expose
        String is_success;

        @SerializedName("pic_id")
        @Expose
        String picId;

        public String getIs_success() {
            return is_success;
        }

        public void setIs_success(String is_success) {
            this.is_success = is_success;
        }

        public String getPicId() {
            return picId;
        }

        public void setPicId(String picId) {
            this.picId = picId;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "is_success='" + is_success + '\'' +
                    ", picId='" + picId + '\'' +
                    '}';
        }
    }

}
