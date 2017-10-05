package com.tokopedia.inbox.rescenter.discussion.data.pojo.getdiscussion;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nisie on 3/30/17.
 */

public class DiscussionEntity {

    @SerializedName("listConversation")
    private List<DiscussionItemEntity> listDiscussion;

    @SerializedName("canLoadMore")
    private int canLoadMore;

    public List<DiscussionItemEntity> getListDiscussion() {
        return listDiscussion;
    }

    public void setListDiscussion(List<DiscussionItemEntity> listDiscussion) {
        this.listDiscussion = listDiscussion;
    }

    public int canLoadMore() {
        return canLoadMore;
    }

    public void setCanLoadMore(int canLoadMore) {
        this.canLoadMore = canLoadMore;
    }
}
