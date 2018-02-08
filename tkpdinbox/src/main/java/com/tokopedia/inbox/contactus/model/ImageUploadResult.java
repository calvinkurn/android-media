
package com.tokopedia.inbox.contactus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageUploadResult {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("config")
    @Expose
    private Object config;
    @SerializedName("server_process_time")
    @Expose
    private String serverProcessTime;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("message_error")
    @Expose
    private String[] messageError;

    /**
     *
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     *     The config
     */
    public Object getConfig() {
        return config;
    }

    /**
     *
     * @param config
     *     The config
     */
    public void setConfig(Object config) {
        this.config = config;
    }

    /**
     *
     * @return
     *     The serverProcessTime
     */
    public String getServerProcessTime() {
        return serverProcessTime;
    }

    /**
     *
     * @param serverProcessTime
     *     The server_process_time
     */
    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    /**
     *
     * @return
     *     The data
     */
    public Data getData() {
        return data;
    }

    /**
     *
     * @param data
     *     The data
     */
    public void setData(Data data) {
        this.data = data;
    }


    /**
     *
     * @return
     *     The messageError
     */
    public String getMessageError() {
        String error = "";
        for (int i = 0 ; i < messageError.length ; i++){
            error += messageError[i];
        }
        return error;
    }

    /**
     *
     * @param messageError
     *     The message_error
     */
    public void setMessageError(String[] messageError) {
        this.messageError = messageError;
    }
}
