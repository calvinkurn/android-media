package com.tokopedia.inbox.rescenter.discussion.data.pojo.replysubmit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hangnadi on 7/5/17.
 */

public class NewReplyDiscussionSubmitEntity {

    @SerializedName("resolution")
    private Resolution resolution;
    @SerializedName("conversations")
    private List<Conversations> conversations;

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public List<Conversations> getConversations() {
        return conversations;
    }

    public void setConversations(List<Conversations> conversations) {
        this.conversations = conversations;
    }


}
