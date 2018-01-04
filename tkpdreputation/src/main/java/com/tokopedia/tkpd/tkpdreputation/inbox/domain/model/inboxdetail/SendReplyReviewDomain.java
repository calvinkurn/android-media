package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail;

/**
 * @author by nisie on 9/28/17.
 */

public class SendReplyReviewDomain {
    boolean isSuccess;

    public SendReplyReviewDomain(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
