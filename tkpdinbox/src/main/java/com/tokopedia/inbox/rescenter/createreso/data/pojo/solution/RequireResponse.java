package com.tokopedia.inbox.rescenter.createreso.data.pojo.solution;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 14/08/17.
 */

public class RequireResponse {
    @SerializedName("attachment")
    @Expose
    private boolean attachment;

    public boolean isAttachment() {
        return attachment;
    }

    public void setAttachment(boolean attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "RequireResponse{" +
                "attachment='" + attachment + '\'' +
                '}';
    }
}
