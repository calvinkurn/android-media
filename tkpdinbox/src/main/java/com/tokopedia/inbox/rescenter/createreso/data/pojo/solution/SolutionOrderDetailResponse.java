package com.tokopedia.inbox.rescenter.createreso.data.pojo.solution;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionOrderDetailResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("freeReturn")
    private boolean freeReturn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(boolean freeReturn) {
        this.freeReturn = freeReturn;
    }
}
