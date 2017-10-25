package com.tokopedia.inbox.rescenter.createreso.domain.model.solution;

import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.AmountDomain;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 24/08/17.
 */

public class AppealSolutionDomain {
    @Nullable
    private int id;

    @Nullable
    private String name;

    @Nullable
    private String solutionName;

    @Nullable
    private AmountDomain refundAmount;

    public AppealSolutionDomain(int id, String name, String solutionName, AmountDomain refundAmount) {
        this.id = id;
        this.name = name;
        this.solutionName = solutionName;
        this.refundAmount = refundAmount;
    }

    @Nullable
    public int getId() {
        return id;
    }

    public void setId(@Nullable int id) {
        this.id = id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public String getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(@Nullable String solutionName) {
        this.solutionName = solutionName;
    }

    @Nullable
    public AmountDomain getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(@Nullable AmountDomain refundAmount) {
        this.refundAmount = refundAmount;
    }
}
