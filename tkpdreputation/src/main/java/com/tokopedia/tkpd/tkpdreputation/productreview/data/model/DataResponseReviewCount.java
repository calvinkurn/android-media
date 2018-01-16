package com.tokopedia.tkpd.tkpdreputation.productreview.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class DataResponseReviewCount {
    @SerializedName("review_count")
    @Expose
    private int reviewCount;

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }
}
