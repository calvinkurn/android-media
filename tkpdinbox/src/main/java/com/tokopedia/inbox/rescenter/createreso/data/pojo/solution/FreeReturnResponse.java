package com.tokopedia.inbox.rescenter.createreso.data.pojo.solution;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 29/08/17.
 */

public class FreeReturnResponse {
    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("link")
    @Expose
    private String link;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "FreeReturnResponse{" +
                "info='" + info + '\'' +
                '}';
    }
}
