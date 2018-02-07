package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yfsx on 09/11/17.
 */

public class ConversationListResponse {

    @SerializedName("canLoadMore")
    private int canLoadMore;

    @SerializedName("conversations")
    private List<ConversationResponse> conversation;

    public int getCanLoadMore() {
        return canLoadMore;
    }

    public void setCanLoadMore(int canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    public List<ConversationResponse> getConversation() {
        return conversation;
    }

    public void setConversation(List<ConversationResponse> conversation) {
        this.conversation = conversation;
    }
}
