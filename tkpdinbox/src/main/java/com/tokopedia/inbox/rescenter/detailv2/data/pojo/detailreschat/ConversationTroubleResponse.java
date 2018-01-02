package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationTroubleResponse {

    @SerializedName("string")
    private String string;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
