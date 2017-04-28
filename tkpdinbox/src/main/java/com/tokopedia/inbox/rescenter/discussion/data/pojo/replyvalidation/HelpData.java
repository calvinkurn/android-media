
package com.tokopedia.inbox.rescenter.discussion.data.pojo.replyvalidation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HelpData {

    @SerializedName("by")
    @Expose
    private HelpDataBy by;
    @SerializedName("dispute")
    @Expose
    private Dispute dispute;

    public HelpDataBy getBy() {
        return by;
    }

    public void setBy(HelpDataBy by) {
        this.by = by;
    }

    public Dispute getDispute() {
        return dispute;
    }

    public void setDispute(Dispute dispute) {
        this.dispute = dispute;
    }

}
