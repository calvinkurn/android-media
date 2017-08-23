
package com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewData {

    @SerializedName("review_id")
    @Expose
    private int reviewId;
    @SerializedName("reputation_id")
    @Expose
    private int reputationId;
    @SerializedName("review_title")
    @Expose
    private String reviewTitle;
    @SerializedName("review_message")
    @Expose
    private String reviewMessage;
    @SerializedName("review_rating")
    @Expose
    private int reviewRating;
    @SerializedName("review_image_url")
    @Expose
    private List<Object> reviewImageUrl = null;
    @SerializedName("review_create_time")
    @Expose
    private String reviewCreateTime;
    @SerializedName("review_update_time")
    @Expose
    private String reviewUpdateTime;
    @SerializedName("review_anonymity")
    @Expose
    private boolean reviewAnonymity;
    @SerializedName("review_response")
    @Expose
    private ReviewResponse reviewResponse;

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getReputationId() {
        return reputationId;
    }

    public void setReputationId(int reputationId) {
        this.reputationId = reputationId;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public String getReviewMessage() {
        return reviewMessage;
    }

    public void setReviewMessage(String reviewMessage) {
        this.reviewMessage = reviewMessage;
    }

    public int getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(int reviewRating) {
        this.reviewRating = reviewRating;
    }

    public List<Object> getReviewImageUrl() {
        return reviewImageUrl;
    }

    public void setReviewImageUrl(List<Object> reviewImageUrl) {
        this.reviewImageUrl = reviewImageUrl;
    }

    public String getReviewCreateTime() {
        return reviewCreateTime;
    }

    public void setReviewCreateTime(String reviewCreateTime) {
        this.reviewCreateTime = reviewCreateTime;
    }

    public String getReviewUpdateTime() {
        return reviewUpdateTime;
    }

    public void setReviewUpdateTime(String reviewUpdateTime) {
        this.reviewUpdateTime = reviewUpdateTime;
    }

    public boolean isReviewAnonymity() {
        return reviewAnonymity;
    }

    public void setReviewAnonymity(boolean reviewAnonymity) {
        this.reviewAnonymity = reviewAnonymity;
    }

    public ReviewResponse getReviewResponse() {
        return reviewResponse;
    }

    public void setReviewResponse(ReviewResponse reviewResponse) {
        this.reviewResponse = reviewResponse;
    }

}
