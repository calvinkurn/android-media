package com.tokopedia.inbox.rescenter.createreso.data.pojo.solution;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem.AmountResponse;

/**
 * Created by yoasfs on 14/08/17.
 */

public class EditSolutionResponse {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("nameCustom")
    @Expose
    private String solutionName;
    @SerializedName("amount")
    @Expose
    private AmountResponse amount;

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

    public AmountResponse getAmount() {
        return amount;
    }

    public void setAmount(AmountResponse amount) {
        this.amount = amount;
    }

    public String getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }

    @Override
    public String toString() {
        return "EditSolutionResponse{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", amount='" + amount.toString() + '\'' +
                '}';
    }
}
