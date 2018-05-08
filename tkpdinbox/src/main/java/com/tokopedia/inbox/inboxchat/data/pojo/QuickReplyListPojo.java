package com.tokopedia.inbox.inboxchat.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by yfsx on 08/05/18.
 */

public class QuickReplyListPojo extends BaseChatPojo {
    @SerializedName("quick_replies")
    @Expose
    private List<QuickReplyPojo> quickReplies;

    public List<QuickReplyPojo> getQuickReplies() {
        return quickReplies;
    }

    public void setQuickReplies(List<QuickReplyPojo> quickReplies) {
        this.quickReplies = quickReplies;
    }
}
