package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model;

/**
 * @author by nisie on 8/15/17.
 */

public class RevieweeDataDomain {

    private String revieweeName;
    private String revieweeUri;
    private String revieweeRole;
    private String revieweePicture;
    private RevieweeBadgeDomain revieweeBadge;

    public RevieweeDataDomain(String revieweeName, String revieweeUri,
                              String revieweeRole, String revieweePicture,
                              RevieweeBadgeDomain revieweeBadge) {
        this.revieweeName = revieweeName;
        this.revieweeUri = revieweeUri;
        this.revieweeRole = revieweeRole;
        this.revieweePicture = revieweePicture;
        this.revieweeBadge = revieweeBadge;
    }

    public String getRevieweeName() {
        return revieweeName;
    }

    public String getRevieweeUri() {
        return revieweeUri;
    }

    public String getRevieweeRole() {
        return revieweeRole;
    }

    public String getRevieweePicture() {
        return revieweePicture;
    }

    public RevieweeBadgeDomain getRevieweeBadge() {
        return revieweeBadge;
    }
}
