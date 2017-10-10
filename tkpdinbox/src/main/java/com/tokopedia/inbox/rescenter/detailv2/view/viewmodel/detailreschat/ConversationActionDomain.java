package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationActionDomain {

    private String type;
    private int by;

    public ConversationActionDomain(String type, int by) {
        this.type = type;
        this.by = by;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getBy() {
        return by;
    }

    public void setBy(int by) {
        this.by = by;
    }
}
