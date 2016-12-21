package com.tokopedia.seller.topads.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 12/15/16.
 */

public class AdResponseAction {

    @SerializedName("ad_id")
    @Expose
    private String adId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("status_desc")
    @Expose
    private String statusDesc;
    @SerializedName("ad_budget")
    @Expose
    private String adBudget;
    @SerializedName("ad_schedule")
    @Expose
    private String adSchedule;
    @SerializedName("ad_start_date")
    @Expose
    private String adStartDate;
    @SerializedName("ad_start_time")
    @Expose
    private String adStartTime;
    @SerializedName("ad_end_date")
    @Expose
    private String adEndDate;

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getAdBudget() {
        return adBudget;
    }

    public void setAdBudget(String adBudget) {
        this.adBudget = adBudget;
    }

    public String getAdSchedule() {
        return adSchedule;
    }

    public void setAdSchedule(String adSchedule) {
        this.adSchedule = adSchedule;
    }

    public String getAdStartDate() {
        return adStartDate;
    }

    public void setAdStartDate(String adStartDate) {
        this.adStartDate = adStartDate;
    }

    public String getAdStartTime() {
        return adStartTime;
    }

    public void setAdStartTime(String adStartTime) {
        this.adStartTime = adStartTime;
    }

    public String getAdEndDate() {
        return adEndDate;
    }

    public void setAdEndDate(String adEndDate) {
        this.adEndDate = adEndDate;
    }
}
