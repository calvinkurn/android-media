package com.tokopedia.inbox.rescenter.createreso.domain.model.solution;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionOrderDetailDomain {

    private int id;
    private boolean freeReturn;

    public SolutionOrderDetailDomain(int id, boolean freeReturn) {
        this.id = id;
        this.freeReturn = freeReturn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(boolean freeReturn) {
        this.freeReturn = freeReturn;
    }
}
