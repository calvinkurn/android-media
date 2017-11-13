package com.tokopedia.inbox.rescenter.historyaction.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yfsx on 13/11/17.
 */

public class CreateTimeResponse {

    @SerializedName("timestampz")
    private String timestampz;
    @SerializedName("createTimeStr")
    private String createTimeStr;

    public String getTimestampz() {
        return timestampz;
    }

    public void setTimestampz(String timestampz) {
        this.timestampz = timestampz;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }
}
