package com.tokopedia.inbox.rescenter.createreso.domain.model.solution;

import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.AmountDomain;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 24/08/17.
 */

public class EditSolutionDomain {
    @Nullable
    private int id;

    @Nullable
    private String name;

    @Nullable
    private AmountDomain refundAmount;

    public EditSolutionDomain(@Nullable int id,
                              @Nullable String name,
                              @Nullable AmountDomain refundAmount) {
        this.id = id;
        this.name = name;
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
    public AmountDomain getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(@Nullable AmountDomain refundAmount) {
        this.refundAmount = refundAmount;
    }
}
