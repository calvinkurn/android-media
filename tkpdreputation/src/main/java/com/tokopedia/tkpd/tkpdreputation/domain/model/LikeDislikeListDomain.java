package com.tokopedia.tkpd.tkpdreputation.domain.model;

/**
 * @author by nisie on 9/29/17.
 */

public class LikeDislikeListDomain {

    private int reviewId;
    private int totalLike;
    private int totalDislike;
    private int likeStatus;

    public LikeDislikeListDomain(int reviewId, int totalLike, int totalDislike, int likeStatus) {
        this.reviewId = reviewId;
        this.totalLike = totalLike;
        this.totalDislike = totalDislike;
        this.likeStatus = likeStatus;
    }

    public int getReviewId() {
        return reviewId;
    }

    public int getTotalLike() {
        return totalLike;
    }

    public int getTotalDislike() {
        return totalDislike;
    }

    public int getLikeStatus() {
        return likeStatus;
    }
}
