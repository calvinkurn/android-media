package com.tokopedia.inbox.rescenter.createreso.domain.model.solution;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionOrderDomain {

    private SolutionOrderDetailDomain detail;

    public SolutionOrderDomain(SolutionOrderDetailDomain detail) {
        this.detail = detail;
    }

    public SolutionOrderDetailDomain getDetail() {
        return detail;
    }

    public void setDetail(SolutionOrderDetailDomain detail) {
        this.detail = detail;
    }

}
