package com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class ProductProblemDomain {
    @Nullable
    private ProblemDomain problemDomain;
    @Nullable
    private OrderDomain orderDomain;
    @Nullable
    private List<StatusDomain> statusDomainList = new ArrayList<>();

    public ProductProblemDomain(@Nullable ProblemDomain problemDomain,
                                @Nullable OrderDomain orderDomain,
                                @Nullable List<StatusDomain> statusDomainList) {
        this.problemDomain = problemDomain;
        this.orderDomain = orderDomain;
        this.statusDomainList = statusDomainList;
    }

    @Nullable
    public ProblemDomain getProblemDomain() {
        return problemDomain;
    }

    public void setProblemDomain(@Nullable ProblemDomain problemDomain) {
        this.problemDomain = problemDomain;
    }

    @Nullable
    public OrderDomain getOrderDomain() {
        return orderDomain;
    }

    public void setOrderDomain(@Nullable OrderDomain orderDomain) {
        this.orderDomain = orderDomain;
    }

    @Nullable
    public List<StatusDomain> getStatusDomainList() {
        return statusDomainList;
    }

    public void setStatusDomainList(@Nullable List<StatusDomain> statusDomainList) {
        this.statusDomainList = statusDomainList;
    }
}
