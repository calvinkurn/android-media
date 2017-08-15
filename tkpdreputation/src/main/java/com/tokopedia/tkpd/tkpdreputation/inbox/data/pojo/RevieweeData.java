
package com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RevieweeData {

    @SerializedName("reviewee_name")
    @Expose
    private String revieweeName;
    @SerializedName("reviewee_uri")
    @Expose
    private String revieweeUri;
    @SerializedName("reviewee_role")
    @Expose
    private String revieweeRole;
    @SerializedName("reviewee_picture")
    @Expose
    private String revieweePicture;
    @SerializedName("reviewee_badge")
    @Expose
    private RevieweeBadge revieweeBadge;

    public String getRevieweeName() {
        return revieweeName;
    }

    public void setRevieweeName(String revieweeName) {
        this.revieweeName = revieweeName;
    }

    public String getRevieweeUri() {
        return revieweeUri;
    }

    public void setRevieweeUri(String revieweeUri) {
        this.revieweeUri = revieweeUri;
    }

    public String getRevieweeRole() {
        return revieweeRole;
    }

    public void setRevieweeRole(String revieweeRole) {
        this.revieweeRole = revieweeRole;
    }

    public String getRevieweePicture() {
        return revieweePicture;
    }

    public void setRevieweePicture(String revieweePicture) {
        this.revieweePicture = revieweePicture;
    }

    public RevieweeBadge getRevieweeBadge() {
        return revieweeBadge;
    }

    public void setRevieweeBadge(RevieweeBadge revieweeBadge) {
        this.revieweeBadge = revieweeBadge;
    }

}
