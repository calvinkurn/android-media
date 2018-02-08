package com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 05/09/17.
 */

public class UploadResponse {

    @SerializedName("pic_obj")
    @Expose
    private String picObj;

    @SerializedName("pic_src")
    @Expose
    private String picSrc;

    public String getPicObj() {
        return picObj;
    }

    public void setPicObj(String picObj) {
        this.picObj = picObj;
    }

    public String getPicSrc() {
        return picSrc;
    }

    public void setPicSrc(String picSrc) {
        this.picSrc = picSrc;
    }

    @Override
    public String toString() {
        return "UploadResponse{" +
                "picObj='" + picObj + '\'' +
                ", picSrc='" + picSrc + '\'' +
                '}';
    }

}
