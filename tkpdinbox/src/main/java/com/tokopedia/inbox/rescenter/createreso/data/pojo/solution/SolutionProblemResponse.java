package com.tokopedia.inbox.rescenter.createreso.data.pojo.solution;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionProblemResponse {

    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("trouble")
    @Expose
    private int trouble;
    @SerializedName("amount")
    @Expose
    private SolutionProblemAmountResponse amount;
    @SerializedName("maxAmount")
    @Expose
    private SolutionProblemAmountResponse maxAmount;
    @SerializedName("qty")
    @Expose
    private int qty;
    @SerializedName("remark")
    @Expose
    private String remark;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SolutionProblemAmountResponse getAmount() {
        return amount;
    }

    public void setAmount(SolutionProblemAmountResponse amount) {
        this.amount = amount;
    }

    public SolutionProblemAmountResponse getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(SolutionProblemAmountResponse maxAmount) {
        this.maxAmount = maxAmount;
    }

    public int getTrouble() {
        return trouble;
    }

    public void setTrouble(int trouble) {
        this.trouble = trouble;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
