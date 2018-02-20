
package com.tokopedia.reputation.common.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReputationSpeed {

    @SerializedName("recent_1_month")
    @Expose
    private ReputationRecentMonth recent1Month;
    @SerializedName("recent_3_month")
    @Expose
    private ReputationRecentMonth recent3Month;
    @SerializedName("recent_12_month")
    @Expose
    private ReputationRecentMonth recent12Month;

    public ReputationRecentMonth getRecent1Month() {
        return recent1Month;
    }

    public void setRecent1Month(ReputationRecentMonth recent1Month) {
        this.recent1Month = recent1Month;
    }

    public ReputationRecentMonth getRecent3Month() {
        return recent3Month;
    }

    public void setRecent3Month(ReputationRecentMonth recent3Month) {
        this.recent3Month = recent3Month;
    }

    public ReputationRecentMonth getRecent12Month() {
        return recent12Month;
    }

    public void setRecent12Month(ReputationRecentMonth recent12Month) {
        this.recent12Month = recent12Month;
    }

}
