package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationFlagResponse {

    @SerializedName("system")
    private int system;

    @SerializedName("solution")
    private int solution;

    public int getSystem() {
        return system;
    }

    public void setSystem(int systrem) {
        this.system = systrem;
    }

    public int getSolution() {
        return solution;
    }

    public void setSolution(int solution) {
        this.solution = solution;
    }
}
