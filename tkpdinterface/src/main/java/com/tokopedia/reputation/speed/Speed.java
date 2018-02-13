package com.tokopedia.reputation.speed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Speed {

    @SerializedName("recent_1_month")
    @Expose
    private RecentMonth recent1Month;
    @SerializedName("recent_3_month")
    @Expose
    private RecentMonth recent3Month;
    @SerializedName("recent_12_month")
    @Expose
    private RecentMonth recent12Month;

    public RecentMonth getRecent1Month() {
        return recent1Month;
    }

    public void setRecent1Month(RecentMonth recent1Month) {
        this.recent1Month = recent1Month;
    }

    public RecentMonth getRecent3Month() {
        return recent3Month;
    }

    public void setRecent3Month(RecentMonth recent3Month) {
        this.recent3Month = recent3Month;
    }

    public RecentMonth getRecent12Month() {
        return recent12Month;
    }

    public void setRecent12Month(RecentMonth recent12Month) {
        this.recent12Month = recent12Month;
    }

}
