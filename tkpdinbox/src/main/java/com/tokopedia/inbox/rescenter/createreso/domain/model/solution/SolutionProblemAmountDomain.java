package com.tokopedia.inbox.rescenter.createreso.domain.model.solution;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionProblemAmountDomain {

    private String idr;
    private int integer;

    public SolutionProblemAmountDomain(String idr, int integer) {
        this.idr = idr;
        this.integer = integer;
    }

    public String getIdr() {
        return idr;
    }

    public void setIdr(String idr) {
        this.idr = idr;
    }

    public int getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }
}
