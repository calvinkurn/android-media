package com.tokopedia.inbox.rescenter.createreso.domain.model.solution;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionProblemDomain {

    private int type;
    private String name;
    private int trouble;
    private SolutionProblemAmountDomain amount;
    private SolutionProblemAmountDomain maxAmount;
    private int qty;
    private String remark;

    public SolutionProblemDomain(int type,
                                 String name,
                                 int trouble,
                                 SolutionProblemAmountDomain amount,
                                 SolutionProblemAmountDomain maxAmount,
                                 int qty,
                                 String remark) {
        this.type = type;
        this.name = name;
        this.trouble = trouble;
        this.amount = amount;
        this.maxAmount = maxAmount;
        this.qty = qty;
        this.remark = remark;
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
