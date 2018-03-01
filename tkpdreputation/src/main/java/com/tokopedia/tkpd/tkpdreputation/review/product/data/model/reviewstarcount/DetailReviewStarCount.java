package com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewstarcount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class DetailReviewStarCount {

    @SerializedName("rate")
    @Expose
    private int rate;
    @SerializedName("total_review")
    @Expose
    private int totalReview;
    @SerializedName("percentage")
    @Expose
    private String percentage;

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getTotalReview() {
        return totalReview;
    }

    public void setTotalReview(int totalReview) {
        this.totalReview = totalReview;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
