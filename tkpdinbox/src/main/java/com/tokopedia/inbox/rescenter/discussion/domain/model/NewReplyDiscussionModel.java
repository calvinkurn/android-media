package com.tokopedia.inbox.rescenter.discussion.domain.model;

import com.tokopedia.inbox.rescenter.discussion.domain.model.replyvalidation.ReplyDiscussionValidationModel;

/**
 * Created by hangnadi on 6/16/17.
 */

public class NewReplyDiscussionModel extends ReplyDiscussionValidationModel {

    private String cacheKey;

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getCacheKey() {
        return cacheKey;
    }
}
