package com.tokopedia.seller.product.edit.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultUploadImage {

    @SerializedName("file_path")
    @Expose
    private String filePath;
    @SerializedName("pic_id")
    @Expose
    private String picId;
    @SerializedName("pic_obj")
    @Expose
    private String picObj;
    @SerializedName("file_th")
    @Expose
    private String fileTh;

    public String getFileTh() {
        return fileTh;
    }

    public void setFileTh(String fileTh) {
        this.fileTh = fileTh;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

    public String getPicObj() {
        return picObj;
    }

    public void setPicObj(String picObj) {
        this.picObj = picObj;
    }
}