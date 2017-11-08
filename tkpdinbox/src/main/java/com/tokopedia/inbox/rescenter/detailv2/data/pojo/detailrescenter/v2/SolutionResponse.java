package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yfsx on 08/11/17.
 */
public class SolutionResponse {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("nameCustom")
    private String nameCustom;
    @SerializedName("actionBy")
    private int actionBy;
    @SerializedName("amount")
    private AmountResponse amount;
    @SerializedName("createTime")
    private String createTime;
    @SerializedName("createTimeStr")
    private String createTimeStr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameCustom() {
        return nameCustom;
    }

    public void setNameCustom(String nameCustom) {
        this.nameCustom = nameCustom;
    }

    public int getActionBy() {
        return actionBy;
    }

    public void setActionBy(int actionBy) {
        this.actionBy = actionBy;
    }

    public AmountResponse getAmount() {
        return amount;
    }

    public void setAmount(AmountResponse amount) {
        this.amount = amount;
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

}
