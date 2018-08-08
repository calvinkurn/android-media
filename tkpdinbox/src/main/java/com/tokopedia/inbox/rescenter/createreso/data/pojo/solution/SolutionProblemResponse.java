package com.tokopedia.inbox.rescenter.createreso.data.pojo.solution;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionProblemResponse {

    @SerializedName("type")
    private int type;
    @SerializedName("name")
    private String name;
    @SerializedName("amount")
    private SolutionProblemAmountResponse amount;
    @SerializedName("maxAmount")
    private SolutionProblemAmountResponse maxAmount;

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
}
