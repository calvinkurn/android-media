package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yfsx on 07/11/17.
 */
public class LogResponse {

    @SerializedName("id")
    private int id;
    @SerializedName("action")
    private String action;
    @SerializedName("solution")
    private SolutionResponse solution;
    @SerializedName("actionBy")
    private CreateByResponse actionBy;
    @SerializedName("createBy")
    private CreateByResponse createBy;
    @SerializedName("createTime")
    private String createTime;
    @SerializedName("createTimeStr")
    private String createTimeStr;
    @SerializedName("createTimeFullStr")
    private String createTimeFullStr;
    @SerializedName("month")
    private String month;
    @SerializedName("dateNumber")
    private String dateNumber;
    @SerializedName("times")
    private String timeNumber;

    public String getCreateTimeFullStr() {
        return createTimeFullStr;
    }

    public void setCreateTimeFullStr(String createTimeFullStr) {
        this.createTimeFullStr = createTimeFullStr;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDateNumber() {
        return dateNumber;
    }

    public void setDateNumber(String dateNumber) {
        this.dateNumber = dateNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public SolutionResponse getSolution() {
        return solution;
    }

    public void setSolution(SolutionResponse solution) {
        this.solution = solution;
    }

    public CreateByResponse getActionBy() {
        return actionBy;
    }

    public void setActionBy(CreateByResponse actionBy) {
        this.actionBy = actionBy;
    }

    public CreateByResponse getCreateBy() {
        return createBy;
    }

    public void setCreateBy(CreateByResponse createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getTimeNumber() {
        return timeNumber;
    }

    public void setTimeNumber(String timeNumber) {
        this.timeNumber = timeNumber;
    }
}
