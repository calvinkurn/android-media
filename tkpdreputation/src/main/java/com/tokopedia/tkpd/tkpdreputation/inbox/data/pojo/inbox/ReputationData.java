
package com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inbox;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReputationData {

    @SerializedName("reviewee_score")
    @Expose
    private int revieweeScore;
    @SerializedName("reviewee_score_status")
    @Expose
    private int revieweeScoreStatus;
    @SerializedName("show_reviewee_score")
    @Expose
    private boolean showRevieweeScore;
    @SerializedName("reviewer_score")
    @Expose
    private int reviewerScore;
    @SerializedName("reviewer_score_status")
    @Expose
    private int reviewerScoreStatus;
    @SerializedName("is_editable")
    @Expose
    private boolean isEditable;
    @SerializedName("is_inserted")
    @Expose
    private boolean isInserted;
    @SerializedName("is_locked")
    @Expose
    private boolean isLocked;
    @SerializedName("is_auto_scored")
    @Expose
    private boolean isAutoScored;
    @SerializedName("is_completed")
    @Expose
    private boolean isCompleted;
    @SerializedName("show_locking_deadline")
    @Expose
    private boolean showLockingDeadline;
    @SerializedName("locking_deadline_days")
    @Expose
    private int lockingDeadlineDays;
    @SerializedName("show_bookmark")
    @Expose
    private boolean showBookmark;
    @SerializedName("action_message")
    @Expose
    private String actionMessage;

    public int getRevieweeScore() {
        return revieweeScore;
    }

    public void setRevieweeScore(int revieweeScore) {
        this.revieweeScore = revieweeScore;
    }

    public int getRevieweeScoreStatus() {
        return revieweeScoreStatus;
    }

    public void setRevieweeScoreStatus(int revieweeScoreStatus) {
        this.revieweeScoreStatus = revieweeScoreStatus;
    }

    public boolean isShowRevieweeScore() {
        return showRevieweeScore;
    }

    public void setShowRevieweeScore(boolean showRevieweeScore) {
        this.showRevieweeScore = showRevieweeScore;
    }

    public int getReviewerScore() {
        return reviewerScore;
    }

    public void setReviewerScore(int reviewerScore) {
        this.reviewerScore = reviewerScore;
    }

    public int getReviewerScoreStatus() {
        return reviewerScoreStatus;
    }

    public void setReviewerScoreStatus(int reviewerScoreStatus) {
        this.reviewerScoreStatus = reviewerScoreStatus;
    }

    public boolean isIsEditable() {
        return isEditable;
    }

    public void setIsEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public boolean isIsInserted() {
        return isInserted;
    }

    public void setIsInserted(boolean isInserted) {
        this.isInserted = isInserted;
    }

    public boolean isIsLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean isIsAutoScored() {
        return isAutoScored;
    }

    public void setIsAutoScored(boolean isAutoScored) {
        this.isAutoScored = isAutoScored;
    }

    public boolean isIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public boolean isShowLockingDeadline() {
        return showLockingDeadline;
    }

    public void setShowLockingDeadline(boolean showLockingDeadline) {
        this.showLockingDeadline = showLockingDeadline;
    }

    public int getLockingDeadlineDays() {
        return lockingDeadlineDays;
    }

    public void setLockingDeadlineDays(int lockingDeadlineDays) {
        this.lockingDeadlineDays = lockingDeadlineDays;
    }

    public boolean isShowBookmark() {
        return showBookmark;
    }

    public void setShowBookmark(boolean showBookmark) {
        this.showBookmark = showBookmark;
    }

    public String getActionMessage() {
        return actionMessage;
    }

    public void setActionMessage(String actionMessage) {
        this.actionMessage = actionMessage;
    }

}
