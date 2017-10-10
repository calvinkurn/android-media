package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationCreateTimeDomain {

    private String timestamp;
    private String string;

    public ConversationCreateTimeDomain(String timestamp, String string) {
        this.timestamp = timestamp;
        this.string = string;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
