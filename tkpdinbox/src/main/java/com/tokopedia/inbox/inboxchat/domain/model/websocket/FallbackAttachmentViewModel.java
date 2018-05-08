
package com.tokopedia.inbox.inboxchat.domain.model.websocket;

/**
 * @author by yfsx on 08/05/18.
 */

public class FallbackAttachmentViewModel {

    public FallbackAttachmentViewModel() {
    }

    private String message;
    private String url;
    private String span;
    private String html;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

}
