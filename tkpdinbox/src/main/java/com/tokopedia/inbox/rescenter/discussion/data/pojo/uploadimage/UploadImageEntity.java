package com.tokopedia.inbox.rescenter.discussion.data.pojo.uploadimage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 4/3/17.
 */

public class UploadImageEntity {
    @SerializedName("file_path")
    @Expose
    private String filePath;
    @SerializedName("file_th")
    @Expose
    private String fileTh;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileTh() {
        return fileTh;
    }

    public void setFileTh(String fileTh) {
        this.fileTh = fileTh;
    }
}
