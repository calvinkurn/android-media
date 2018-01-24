package com.tokopedia.inbox.rescenter.inboxv2.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yfsx on 24/01/18.
 */
public class AutoExecuteTimeResponse {
    /**
     * time : 0001-01-01T00:00:00Z
     * string :
     */

    @SerializedName("time")
    private String time;
    @SerializedName("string")
    private String string;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
