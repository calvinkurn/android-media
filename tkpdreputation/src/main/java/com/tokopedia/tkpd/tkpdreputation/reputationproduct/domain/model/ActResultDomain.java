package com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 20/07/17.
 */
public class ActResultDomain implements Parcelable {

    private boolean success;
    private String errMessage;
    private int errCode;
    private int isOwner;
    private ReviewResponseDomain reviewResponse;
    private String reputationReviewCounter;
    private int isSuccess;
    private int showBookmark;
    private String reviewId;
    private ProductOwnerDomain productOwner;
    private int feedbackId;
    private String postKey;
    private String messageError;
    private String attachmentId;

    public ActResultDomain() {
    }
    protected ActResultDomain(Parcel in) {
        isOwner = in.readInt();
        reviewResponse = in.readParcelable(ReviewResponseDomain.class.getClassLoader());
        reputationReviewCounter = in.readString();
        isSuccess = in.readInt();
        showBookmark = in.readInt();
        reviewId = in.readString();
        productOwner = in.readParcelable(ProductOwnerDomain.class.getClassLoader());
        feedbackId = in.readInt();
        postKey = in.readString();
        messageError = in.readString();
        attachmentId = in.readString();
    }

    public static final Creator<ActResultDomain> CREATOR = new Creator<ActResultDomain>() {
        @Override
        public ActResultDomain createFromParcel(Parcel in) {
            return new ActResultDomain(in);
        }

        @Override
        public ActResultDomain[] newArray(int size) {
            return new ActResultDomain[size];
        }
    };

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public int getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(int isOwner) {
        this.isOwner = isOwner;
    }

    public ReviewResponseDomain getReviewResponse() {
        return reviewResponse;
    }

    public void setReviewResponse(ReviewResponseDomain reviewResponse) {
        this.reviewResponse = reviewResponse;
    }

    public String getReputationReviewCounter() {
        return reputationReviewCounter;
    }

    public void setReputationReviewCounter(String reputationReviewCounter) {
        this.reputationReviewCounter = reputationReviewCounter;
    }

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public int getShowBookmark() {
        return showBookmark;
    }

    public void setShowBookmark(int showBookmark) {
        this.showBookmark = showBookmark;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public ProductOwnerDomain getProductOwner() {
        return productOwner;
    }

    public void setProductOwner(ProductOwnerDomain productOwner) {
        this.productOwner = productOwner;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(isOwner);
        dest.writeParcelable(reviewResponse, flags);
        dest.writeString(reputationReviewCounter);
        dest.writeInt(isSuccess);
        dest.writeInt(showBookmark);
        dest.writeString(reviewId);
        dest.writeParcelable(productOwner, flags);
        dest.writeInt(feedbackId);
        dest.writeString(postKey);
        dest.writeString(messageError);
        dest.writeString(attachmentId);
    }
}