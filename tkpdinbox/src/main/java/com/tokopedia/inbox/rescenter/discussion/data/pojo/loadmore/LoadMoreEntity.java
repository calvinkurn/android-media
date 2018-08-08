package com.tokopedia.inbox.rescenter.discussion.data.pojo.loadmore;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nisie on 3/31/17.
 */

public class LoadMoreEntity {

    @SerializedName("listConversation")
    private List<LoadMoreItemEntity> listDiscussion;

    @SerializedName("canLoadMore")
    private int canLoadMore;

    public List<LoadMoreItemEntity> getListDiscussion() {
        return listDiscussion;
    }

    public void setListDiscussion(List<LoadMoreItemEntity> listDiscussion) {
        this.listDiscussion = listDiscussion;
    }

    public int canLoadMore() {
        return canLoadMore;
    }

    public void setCanLoadMore(int canLoadMore) {
        this.canLoadMore = canLoadMore;
    }
}
