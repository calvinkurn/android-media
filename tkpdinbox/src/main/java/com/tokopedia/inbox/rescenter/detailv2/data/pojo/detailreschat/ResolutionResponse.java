package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ResolutionResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("freeReturn")
    private int freeReturn;

    @SerializedName("status")
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(int freeReturn) {
        this.freeReturn = freeReturn;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
