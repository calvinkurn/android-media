
package com.tokopedia.inbox.inboxchat.domain.pojo.quickreply;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.inbox.inboxchat.domain.model.reply.FallbackAttachment;

public class QuickReplyAttachment {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("attributes")
    @Expose
    private QuickReplyAttachmentAttributes attributes;
    @SerializedName("fallback_attachment")
    @Expose
    private FallbackAttachment fallbackAttachment;

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

    public QuickReplyAttachmentAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(QuickReplyAttachmentAttributes attributes) {
        this.attributes = attributes;
    }

    public FallbackAttachment getFallbackAttachment() {
        return fallbackAttachment;
    }

    public void setFallbackAttachment(FallbackAttachment fallbackAttachment) {
        this.fallbackAttachment = fallbackAttachment;
    }
}
