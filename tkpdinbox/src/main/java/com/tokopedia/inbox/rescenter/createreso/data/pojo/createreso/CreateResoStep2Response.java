package com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 05/09/17.
 */

public class CreateResoStep2Response {
    @SerializedName("resolution")
    @Expose
    private String resolution;

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @Override
    public String toString() {
        return "CreateResoStep2Response{" +
                "resolution='" + resolution + '\'' +
                '}';
    }
}
