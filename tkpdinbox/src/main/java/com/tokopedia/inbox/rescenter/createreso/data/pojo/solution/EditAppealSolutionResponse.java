package com.tokopedia.inbox.rescenter.createreso.data.pojo.solution;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 29/08/17.
 */

public class EditAppealSolutionResponse {
    @SerializedName("successMessage")
    @Expose
    private String successMessage;

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    @Override
    public String toString() {
        return "EditAppealSolutionResponse{" +
                "successMessage='" + successMessage + '\'' +
                '}';
    }
}
