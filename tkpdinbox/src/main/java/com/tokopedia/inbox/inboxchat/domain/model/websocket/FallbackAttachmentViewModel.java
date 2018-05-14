
package com.tokopedia.inbox.inboxchat.domain.model.websocket;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;

/**
 * @author by yfsx on 08/05/18.
 */

public class FallbackAttachmentViewModel extends BaseChatViewModel implements
        Visitable<ChatRoomTypeFactory> {

    public FallbackAttachmentViewModel(String msgId,
                                       String fromUid,
                                       String from,
                                       String fromRole,
                                       String attachmentId,
                                       String attachmentType,
                                       String replyTime,
                                       String fallbackMessage,
                                       String url,
                                       String span,
                                       String html) {
        super(msgId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime);
        this.message = fallbackMessage;
        this.url = url;
        this.span = span;
        this.html = html;
    }

    private String message;
    private String url;
    private String span;
    private String html;

    public String getMessage() {
        return message;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSpan() {
        return span;
    }

    public void setSpan(String span) {
        this.span = span;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    @Override
    public int type(ChatRoomTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
