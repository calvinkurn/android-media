package com.tokopedia.inbox.rescenter.inboxv2.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yfsx on 24/01/18.
 */
public class CreateTimeResponse {
    /**
     * time : 2017-10-24T15:34:11.693183Z
     * string : 24/10/17 15:34
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
