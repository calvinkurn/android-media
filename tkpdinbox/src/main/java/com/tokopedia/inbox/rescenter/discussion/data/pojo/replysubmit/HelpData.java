
package com.tokopedia.inbox.rescenter.discussion.data.pojo.replysubmit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HelpData {

    @SerializedName("dispute")
    @Expose
    private Dispute dispute;
    @SerializedName("by")
    @Expose
    private By by;

    public Dispute getDispute() {
        return dispute;
    }

    public void setDispute(Dispute dispute) {
        this.dispute = dispute;
    }

    public By getBy() {
        return by;
    }

    public void setBy(By by) {
        this.by = by;
    }

}
