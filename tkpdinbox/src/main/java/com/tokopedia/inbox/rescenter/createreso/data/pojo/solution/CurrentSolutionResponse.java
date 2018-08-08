package com.tokopedia.inbox.rescenter.createreso.data.pojo.solution;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 08/08/18.
 */
public class CurrentSolutionResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("identifier")
    private String identifier;

    @SerializedName("amount")
    private SolutionProblemAmountResponse amount;

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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public SolutionProblemAmountResponse getAmount() {
        return amount;
    }

    public void setAmount(SolutionProblemAmountResponse amount) {
        this.amount = amount;
    }
}
