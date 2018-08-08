package com.tokopedia.inbox.rescenter.inboxv2.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yfsx on 29/01/18.
 */

public class QuickFilterResponse {


    @SerializedName("unread")
    private FilterResponse unread;
    @SerializedName("unanswered")
    private FilterResponse unanswered;
    @SerializedName("finished")
    private FilterResponse finished;
    @SerializedName("autoExecution")
    private FilterResponse autoExecution;
    @SerializedName("unfinished")
    private FilterResponse unfinished;

    public FilterResponse getUnread() {
        return unread;
    }

    public void setUnread(FilterResponse unread) {
        this.unread = unread;
    }

    public FilterResponse getUnanswered() {
        return unanswered;
    }

    public void setUnanswered(FilterResponse unanswered) {
        this.unanswered = unanswered;
    }

    public FilterResponse getFinished() {
        return finished;
    }

    public void setFinished(FilterResponse finished) {
        this.finished = finished;
    }

    public FilterResponse getAutoExecution() {
        return autoExecution;
    }

    public void setAutoExecution(FilterResponse autoExecution) {
        this.autoExecution = autoExecution;
    }

    public FilterResponse getUnfinished() {
        return unfinished;
    }

    public void setUnfinished(FilterResponse unfinished) {
        this.unfinished = unfinished;
    }
}