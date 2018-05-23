package com.tokopedia.inbox.inboxchat.domain.model.websocket;

/**
 * @author by yfsx on 08/05/18.
 */

public class MessageViewModel {

    public MessageViewModel() {
    }

    private String censoredReply;
    private String originalReply;
    private String timestamp;
    private String timestampFmt;
    private String timeStampUnixNano;
    private String timeStampUnix;

    public String getCensoredReply() {
        return censoredReply;
    }

    public void setCensoredReply(String censoredReply) {
        this.censoredReply = censoredReply;
    }

    public String getOriginalReply() {
        return originalReply;
    }

    public void setOriginalReply(String originalReply) {
        this.originalReply = originalReply;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestampFmt() {
        return timestampFmt;
    }

    public void setTimestampFmt(String timestampFmt) {
        this.timestampFmt = timestampFmt;
    }

    public String getTimeStampUnix() {
        return timeStampUnix;
    }

    public void setTimeStampUnix(String timeStampUnix) {
        this.timeStampUnix = timeStampUnix;
    }

    public String getTimeStampUnixNano() {
        return timeStampUnixNano;
    }

    public void setTimeStampUnixNano(String timeStampUnixNano) {
        this.timeStampUnixNano = timeStampUnixNano;
    }
}
