package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 8/28/17.
 */

public class ReputationDataViewModel implements Parcelable{

    private int revieweeScore;
    private int revieweeScoreStatus;
    private boolean showRevieweeScore;
    private int reviewerScore;
    private int reviewerScoreStatus;
    private boolean isEditable;
    private boolean isInserted;
    private boolean isLocked;
    private boolean isAutoScored;
    private boolean isCompleted;
    private boolean showLockingDeadline;
    private int lockingDeadlineDays;
    private boolean showBookmark;
    private String actionMessage;

    public ReputationDataViewModel(int revieweeScore, int revieweeScoreStatus,
                                   boolean showRevieweeScore, int reviewerScore,
                                   int reviewerScoreStatus, boolean isEditable,
                                   boolean isInserted, boolean isLocked, boolean isAutoScored,
                                   boolean isCompleted, boolean showLockingDeadline,
                                   int lockingDeadlineDays, boolean showBookmark,
                                   String actionMessage) {
        this.revieweeScore = revieweeScore;
        this.revieweeScoreStatus = revieweeScoreStatus;
        this.showRevieweeScore = showRevieweeScore;
        this.reviewerScore = reviewerScore;
        this.reviewerScoreStatus = reviewerScoreStatus;
        this.isEditable = isEditable;
        this.isInserted = isInserted;
        this.isLocked = isLocked;
        this.isAutoScored = isAutoScored;
        this.isCompleted = isCompleted;
        this.showLockingDeadline = showLockingDeadline;
        this.lockingDeadlineDays = lockingDeadlineDays;
        this.showBookmark = showBookmark;
        this.actionMessage = actionMessage;
    }

    protected ReputationDataViewModel(Parcel in) {
        revieweeScore = in.readInt();
        revieweeScoreStatus = in.readInt();
        showRevieweeScore = in.readByte() != 0;
        reviewerScore = in.readInt();
        reviewerScoreStatus = in.readInt();
        isEditable = in.readByte() != 0;
        isInserted = in.readByte() != 0;
        isLocked = in.readByte() != 0;
        isAutoScored = in.readByte() != 0;
        isCompleted = in.readByte() != 0;
        showLockingDeadline = in.readByte() != 0;
        lockingDeadlineDays = in.readInt();
        showBookmark = in.readByte() != 0;
        actionMessage = in.readString();
    }

    public static final Creator<ReputationDataViewModel> CREATOR = new Creator<ReputationDataViewModel>() {
        @Override
        public ReputationDataViewModel createFromParcel(Parcel in) {
            return new ReputationDataViewModel(in);
        }

        @Override
        public ReputationDataViewModel[] newArray(int size) {
            return new ReputationDataViewModel[size];
        }
    };

    public int getRevieweeScore() {
        return revieweeScore;
    }

    public int getRevieweeScoreStatus() {
        return revieweeScoreStatus;
    }

    public boolean isShowRevieweeScore() {
        return showRevieweeScore;
    }

    public int getReviewerScore() {
        return reviewerScore;
    }

    public int getReviewerScoreStatus() {
        return reviewerScoreStatus;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public boolean isInserted() {
        return isInserted;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public boolean isAutoScored() {
        return isAutoScored;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean isShowLockingDeadline() {
        return showLockingDeadline;
    }

    public int getLockingDeadlineDays() {
        return lockingDeadlineDays;
    }

    public boolean isShowBookmark() {
        return showBookmark;
    }

    public String getActionMessage() {
        return actionMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(revieweeScore);
        dest.writeInt(revieweeScoreStatus);
        dest.writeByte((byte) (showRevieweeScore ? 1 : 0));
        dest.writeInt(reviewerScore);
        dest.writeInt(reviewerScoreStatus);
        dest.writeByte((byte) (isEditable ? 1 : 0));
        dest.writeByte((byte) (isInserted ? 1 : 0));
        dest.writeByte((byte) (isLocked ? 1 : 0));
        dest.writeByte((byte) (isAutoScored ? 1 : 0));
        dest.writeByte((byte) (isCompleted ? 1 : 0));
        dest.writeByte((byte) (showLockingDeadline ? 1 : 0));
        dest.writeInt(lockingDeadlineDays);
        dest.writeByte((byte) (showBookmark ? 1 : 0));
        dest.writeString(actionMessage);
    }

    public void setReviewerScore(int reviewerScore) {
        this.reviewerScore = reviewerScore;
    }

    public void setInserted(boolean inserted) {
        isInserted = inserted;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }
}
