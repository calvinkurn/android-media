package com.tokopedia.seller.product.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 3/22/17.
 */

public class UploadImageModel {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("config")
    @Expose
    private Object config;
    @SerializedName("server_process_time")
    @Expose
    private String serverProcessTime;
    @SerializedName("result")
    @Expose
    private Result result;

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

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {

        @SerializedName("file_path")
        @Expose
        private String filePath;
        @SerializedName("pic_id")
        @Expose
        private Integer picId;
        @SerializedName("pic_obj")
        @Expose
        private String picObj;

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public Integer getPicId() {
            return picId;
        }

        public void setPicId(Integer picId) {
            this.picId = picId;
        }

        public String getPicObj() {
            return picObj;
        }

        public void setPicObj(String picObj) {
            this.picObj = picObj;
        }

    }
}
