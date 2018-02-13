package com.tokopedia.reputation.speed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Speed {

    @SerializedName("recent_1_month")
    @Expose
    private Recent1Month recent1Month;
    @SerializedName("recent_3_month")
    @Expose
    private Recent3Month recent3Month;
    @SerializedName("recent_12_month")
    @Expose
    private Recent12Month recent12Month;

    public Recent1Month getRecent1Month() {
        return recent1Month;
    }

    public void setRecent1Month(Recent1Month recent1Month) {
        this.recent1Month = recent1Month;
    }

    public Recent3Month getRecent3Month() {
        return recent3Month;
    }

    public void setRecent3Month(Recent3Month recent3Month) {
        this.recent3Month = recent3Month;
    }

    public Recent12Month getRecent12Month() {
        return recent12Month;
    }

    public void setRecent12Month(Recent12Month recent12Month) {
        this.recent12Month = recent12Month;
    }

}
