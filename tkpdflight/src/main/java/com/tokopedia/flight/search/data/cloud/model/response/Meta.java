package com.tokopedia.flight.search.data.cloud.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;

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

    private String departureAirport; // merge result
    private String arrivalAirport; // merge result
    private String time; // merge result

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

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }
}
