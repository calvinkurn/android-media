package com.tokopedia.inbox.rescenter.createreso.data.pojo.solution;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionMessageResponse {

    @SerializedName("confirm")
    private String confirm;

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
}
