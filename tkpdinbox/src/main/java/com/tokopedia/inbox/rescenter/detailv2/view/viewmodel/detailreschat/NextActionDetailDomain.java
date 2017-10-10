package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import java.util.List;

/**
 * Created by yoasfs on 10/10/17.
 */

public class NextActionDetailDomain {

    private String solution;
    private List<NextActionDetailStepDomain> step;

    public NextActionDetailDomain(String solution, List<NextActionDetailStepDomain> step) {
        this.solution = solution;
        this.step = step;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public List<NextActionDetailStepDomain> getStep() {
        return step;
    }

    public void setStep(List<NextActionDetailStepDomain> step) {
        this.step = step;
    }
}
