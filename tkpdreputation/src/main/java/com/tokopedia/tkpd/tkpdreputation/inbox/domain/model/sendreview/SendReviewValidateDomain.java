package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview;

/**
 * @author by nisie on 8/31/17.
 */

public class SendReviewValidateDomain {

    private String postKey;
    private int reviewId;
    private int isSuccess;

    public SendReviewValidateDomain(String postKey, int reviewId, int isSuccess) {
        this.postKey = postKey;
        this.reviewId = reviewId;
        this.isSuccess = isSuccess;
    }

    public String getPostKey() {
        return postKey;
    }

    public int getReviewId() {
        return reviewId;
    }

    public int getIsSuccess() {
        return isSuccess;
    }
}
