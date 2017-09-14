package com.tokopedia.inbox.rescenter.createreso.data.pojo.solution;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 29/08/17.
 */

public class EditFreeReturnResponse {
    @SerializedName("info")
    @Expose
    private String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "EditFreeReturnResponse{" +
                "info='" + info + '\'' +
                '}';
    }
}
