package com.tokopedia.seller.selling.model;

import java.util.List;

/**
 * Created by Toped10 on 8/2/2016.
 */
public class ResponseConfirmShipping {

    /**
     * config : null
     * status : OK
     * data : {"is_success":0}
     * serverProcessTime : 0.032727
     * messageError : ["Nomor Resi harus diisi."]
     */

    private String config;
    private String status;
    /**
     * is_success : 0
     */

    private DataBean data;
    private String serverProcessTime;
    private List<String> messageError;
    private List<String> messageStatus  ;

    public List<String> getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(List<String> messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getServerProcessTime() {
        return serverProcessTime;
    }

    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    public List<String> getMessageError() {
        return messageError;
    }

    public void setMessageError(List<String> messageError) {
        this.messageError = messageError;
    }

    public static class DataBean {
        private int is_success;

        public int getIs_success() {
            return is_success;
        }

        public void setIs_success(int is_success) {
            this.is_success = is_success;
        }
    }
}
