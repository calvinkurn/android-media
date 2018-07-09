
package com.tokopedia.inbox.inboxchat.domain.model.reply;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.inbox.inboxchat.domain.pojo.quickreply.QuickReplyListPojo;

public class Attachment {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("attributes")
    @Expose
    private AttachmentAttributes attributes;
    @SerializedName("fallback_attachment")
    @Expose
    private FallbackAttachment fallbackAttachment;
    @SerializedName("quick_replies")
    @Expose
    private QuickReplyListPojo quickReplies;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AttachmentAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(AttachmentAttributes attributes) {
        this.attributes = attributes;
    }

    public FallbackAttachment getFallbackAttachment() {
        return fallbackAttachment;
    }

    public void setFallbackAttachment(FallbackAttachment fallbackAttachment) {
        this.fallbackAttachment = fallbackAttachment;
    }

    public QuickReplyListPojo getQuickReplies() {
        return quickReplies;
    }

    public void setQuickReplies(QuickReplyListPojo quickReplies) {
        this.quickReplies = quickReplies;
    }
}
