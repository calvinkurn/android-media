
package com.tokopedia.inbox.inboxchat.domain.pojo.quickreply;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.inbox.inboxchat.domain.model.reply.AttachmentInvoice;
import com.tokopedia.inbox.inboxchat.domain.model.reply.AttachmentInvoiceList;
import com.tokopedia.inbox.inboxchat.domain.model.reply.AttachmentProductProfile;

import java.util.List;

public class QuickReplyAttachmentAttributes {

    @SerializedName("quick_replies")
    @Expose
    private List<QuickReplyPojo> quickReplies;

    public List<QuickReplyPojo> getQuickReplies() {
        return quickReplies;
    }

}
