package com.tokopedia.flight.search.data.cloud.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 11/14/2017.
 */

public class Meta {
    @SerializedName("need_refresh")
    @Expose
    private boolean needRefresh;
    @SerializedName("refresh_time")
    @Expose
    private int refreshTime;
    @SerializedName("max_retry")
    @Expose
    private int maxRetry;
    @SerializedName("adult")
    @Expose
    private int adult;
    @SerializedName("child")
    @Expose
    private int child;
    @SerializedName("infant")
    @Expose
    private int infant;

    public boolean isNeedRefresh() {
        return needRefresh;
    }

    public int getRefreshTime() {
        return refreshTime;
    }

    public int getMaxRetry() {
        return maxRetry;
    }

    public int getAdult() {
        return adult;
    }

    public int getChild() {
        return child;
    }

    public int getInfant() {
        return infant;
    }
}
