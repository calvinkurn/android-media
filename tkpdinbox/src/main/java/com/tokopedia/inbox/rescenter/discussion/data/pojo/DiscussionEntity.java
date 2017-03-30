package com.tokopedia.inbox.rescenter.discussion.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nisie on 3/30/17.
 */

public class DiscussionEntity {

    @SerializedName("listdiscussion")
    private List<DiscussionItemEntity> listDiscussion;

    public List<DiscussionItemEntity> getListDiscussion() {
        return listDiscussion;
    }

    public void setListDiscussion(List<DiscussionItemEntity> listDiscussion) {
        this.listDiscussion = listDiscussion;
    }
}
