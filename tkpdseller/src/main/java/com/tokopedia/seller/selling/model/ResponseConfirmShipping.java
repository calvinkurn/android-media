package com.tokopedia.core.selling.model;

import java.util.List;

/**
 * Created by Toped10 on 8/2/2016.
 */
public class ResponseConfirmShipping {

    /**
     * config : null
     * status : OK
     * data : {"is_success":0}
     * server_process_time : 0.032727
     * message_error : ["Nomor Resi harus diisi."]
     */

    private String config;
    private String status;
    /**
     * is_success : 0
     */

    private DataBean data;
    private String server_process_time;
    private List<String> message_error;
    private List<String> message_status;

    public List<String> getMessage_status() {
        return message_status;
    }

    public void setMessage_status(List<String> message_status) {
        this.message_status = message_status;
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

    public String getServer_process_time() {
        return server_process_time;
    }

    public void setServer_process_time(String server_process_time) {
        this.server_process_time = server_process_time;
    }

    public List<String> getMessage_error() {
        return message_error;
    }

    public void setMessage_error(List<String> message_error) {
        this.message_error = message_error;
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
