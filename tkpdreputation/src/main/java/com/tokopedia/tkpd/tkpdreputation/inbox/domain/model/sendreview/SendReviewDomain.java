package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview;

/**
 * @author by nisie on 8/31/17.
 */

public class SendReviewDomain {
    boolean isSuccess;

    public SendReviewDomain(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
