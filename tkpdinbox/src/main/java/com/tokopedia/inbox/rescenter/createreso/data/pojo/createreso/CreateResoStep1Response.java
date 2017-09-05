package com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 05/09/17.
 */

public class CreateResoStep1Response {

    @SerializedName("resolution")
    @Expose
    private String resolution;

    @SerializedName("cacheKey")
    @Expose
    private String cacheKey;

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @Override
    public String toString() {
        return "CreateResoStep1Response{" +
                "resolution='" + resolution + '\'' +
                "cacheKey='" + cacheKey + '\'' +
                '}';
    }

}
