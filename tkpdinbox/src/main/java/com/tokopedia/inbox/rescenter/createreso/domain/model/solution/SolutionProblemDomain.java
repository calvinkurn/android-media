package com.tokopedia.inbox.rescenter.createreso.domain.model.solution;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionProblemDomain {

    private int type;
    private String name;
    private SolutionProblemAmountDomain amount;
    private SolutionProblemAmountDomain maxAmount;

    public SolutionProblemDomain(int type, String name, SolutionProblemAmountDomain amount, SolutionProblemAmountDomain maxAmount) {
        this.type = type;
        this.name = name;
        this.amount = amount;
        this.maxAmount = maxAmount;
    }

    public SolutionProblemAmountDomain getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(SolutionProblemAmountDomain maxAmount) {
        this.maxAmount = maxAmount;
    }

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

    public SolutionProblemAmountDomain getAmount() {
        return amount;
    }

    public void setAmount(SolutionProblemAmountDomain amount) {
        this.amount = amount;
    }

}
