package com.tokopedia.inbox.rescenter.createreso.domain.model.solution;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionMessageDomain {
    private String confirm;

    public SolutionMessageDomain(String confirm) {
        this.confirm = confirm;
    }

    public String getConfirm() {

        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
}
