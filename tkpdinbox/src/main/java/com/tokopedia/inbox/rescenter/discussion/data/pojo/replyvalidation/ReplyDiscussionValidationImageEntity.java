package com.tokopedia.inbox.rescenter.discussion.data.pojo.replyvalidation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 4/4/17.
 */

public class ReplyDiscussionValidationImageEntity {
    @SerializedName("post_key")
    @Expose
    private String postKey;

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }
}
