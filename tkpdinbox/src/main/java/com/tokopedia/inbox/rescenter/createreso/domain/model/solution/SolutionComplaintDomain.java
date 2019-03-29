package com.tokopedia.inbox.rescenter.createreso.domain.model.solution;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionComplaintDomain {

    private SolutionProblemDomain problem;
    private SolutionShippingDomain shipping;
    private SolutionProductDomain product;
    private SolutionOrderDomain order;

    public SolutionComplaintDomain(SolutionProblemDomain problem, SolutionShippingDomain shipping, SolutionProductDomain product, SolutionOrderDomain order) {
        this.problem = problem;
        this.shipping = shipping;
        this.product = product;
        this.order = order;
    }

    public SolutionProblemDomain getProblem() {
        return problem;
    }

    public void setProblem(SolutionProblemDomain problem) {
        this.problem = problem;
    }

    public SolutionShippingDomain getShipping() {
        return shipping;
    }

    public void setShipping(SolutionShippingDomain shipping) {
        this.shipping = shipping;
    }

    public SolutionProductDomain getProduct() {
        return product;
    }

    public void setProduct(SolutionProductDomain product) {
        this.product = product;
    }

    public SolutionOrderDomain getOrder() {
        return order;
    }

    public void setOrder(SolutionOrderDomain order) {
        this.order = order;
    }

}
