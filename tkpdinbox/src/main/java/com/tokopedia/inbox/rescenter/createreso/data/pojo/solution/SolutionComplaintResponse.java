package com.tokopedia.inbox.rescenter.createreso.data.pojo.solution;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionComplaintResponse {

    @SerializedName("problem")
    @Expose
    private SolutionProblemResponse problem;
    @SerializedName("shipping")
    @Expose
    private SolutionShippingResponse shipping;
    @SerializedName("product")
    @Expose
    private SolutionProductResponse product;
    @SerializedName("order")
    @Expose
    private SolutionOrderResponse order;

    public SolutionProblemResponse getProblem() {
        return problem;
    }

    public void setProblem(SolutionProblemResponse problem) {
        this.problem = problem;
    }

    public SolutionShippingResponse getShipping() {
        return shipping;
    }

    public void setShipping(SolutionShippingResponse shipping) {
        this.shipping = shipping;
    }

    public SolutionProductResponse getProduct() {
        return product;
    }

    public void setProduct(SolutionProductResponse product) {
        this.product = product;
    }

    public SolutionOrderResponse getOrder() {
        return order;
    }

    public void setOrder(SolutionOrderResponse order) {
        this.order = order;
    }

}
