package com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ProductProblemResponse {
    @SerializedName("problem")
    @Expose
    private ProblemResponse problem;
    @SerializedName("order")
    @Expose
    private OrderResponse order;
    @SerializedName("status")
    @Expose
    private List<StatusResponse> statusList = new ArrayList<>();

    public ProblemResponse getProblem() {
        return problem;
    }

    public void setProblem(ProblemResponse problem) {
        this.problem = problem;
    }

    public OrderResponse getOrder() {
        return order;
    }

    public void setOrder(OrderResponse order) {
        this.order = order;
    }

    public List<StatusResponse> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<StatusResponse> statusList) {
        this.statusList = statusList;
    }


    @Override
    public String toString() {
        return "ProductProblemResponse{" +
                "problem='" + problem.toString() + '\'' +
                ", order='" + order.toString() + '\'' +
                ", status='" + statusList.toString() + '\'' +
                '}';
    }
}
