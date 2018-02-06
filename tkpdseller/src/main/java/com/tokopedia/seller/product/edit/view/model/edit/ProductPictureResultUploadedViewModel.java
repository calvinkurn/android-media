package com.tokopedia.seller.product.edit.view.model.edit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 2/5/18.
 */

public class ProductPictureResultUploadedViewModel {
    @SerializedName("server_id")
    @Expose
    private String serverId;
    @SerializedName("file_name")
    @Expose
    private String fileName;
    @SerializedName("file_path")
    @Expose
    private String filePath;
    @SerializedName("w")
    @Expose
    private String w;
    @SerializedName("h")
    @Expose
    private String h;
    @SerializedName("p_id")
    @Expose
    private String pId;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public String getPId() {
        return pId;
    }

    public void setPId(String pId) {
        this.pId = pId;
    }
}
