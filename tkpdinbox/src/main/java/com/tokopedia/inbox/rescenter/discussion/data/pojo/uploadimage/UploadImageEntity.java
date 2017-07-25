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

    @SerializedName("pic_obj")
    @Expose
    private String picObj;
    @SerializedName("pic_src")
    @Expose
    private String picSrc;

    /**
     *
     * @return
     *     The picObj
     */
    public String getPicObj() {
        return picObj;
    }

    /**
     *
     * @param picObj
     *     The pic_obj
     */
    public void setPicObj(String picObj) {
        this.picObj = picObj;
    }

    /**
     *
     * @return
     *     The picSrc
     */
    public String getPicSrc() {
        return picSrc;
    }

    /**
     *
     * @param picSrc
     *     The pic_src
     */
    public void setPicSrc(String picSrc) {
        this.picSrc = picSrc;
    }
}
