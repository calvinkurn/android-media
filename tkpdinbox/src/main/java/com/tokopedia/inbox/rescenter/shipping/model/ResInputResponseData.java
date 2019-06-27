package com.tokopedia.inbox.rescenter.shipping.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResInputResponseData {

    @SerializedName("resolution")
    @Expose
    private Resolution resolution;

    @SerializedName("cache_key")
    @Expose
    private String cacheKey;

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }
}
