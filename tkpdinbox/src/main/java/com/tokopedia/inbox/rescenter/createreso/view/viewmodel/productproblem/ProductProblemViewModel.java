package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ProductProblemViewModel {
    private ProblemViewModel problem;
    private OrderViewModel order;
    private List<StatusViewModel> statusList = new ArrayList<>();

    public ProductProblemViewModel(ProblemViewModel problem, OrderViewModel order, List<StatusViewModel> statusList) {
        this.problem = problem;
        this.order = order;
        this.statusList = statusList;
    }

    public ProblemViewModel getProblem() {
        return problem;
    }

    public void setProblem(ProblemViewModel product) {
        this.problem = problem;
    }

    public OrderViewModel getOrder() {
        return order;
    }

    public void setOrder(OrderViewModel order) {
        this.order = order;
    }

    public List<StatusViewModel> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<StatusViewModel> statusList) {
        this.statusList = statusList;
    }
}
