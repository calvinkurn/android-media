package com.tokopedia.inbox.rescenter.inboxv2.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yfsx on 24/01/18.
 */
public class StatusResponse {
    /**
     * int : 500
     * string : Kendala Selesai
     */

    @SerializedName("int")
    private int intX;
    @SerializedName("string")
    private String string;

    public int getIntX() {
        return intX;
    }

    public void setIntX(int intX) {
        this.intX = intX;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
