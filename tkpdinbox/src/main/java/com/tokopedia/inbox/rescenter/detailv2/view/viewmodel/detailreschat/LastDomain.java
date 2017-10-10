package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

/**
 * Created by yoasfs on 10/10/17.
 */

public class LastDomain {
    private LastSolutionDomain solution;
    private String problem;

    public LastDomain(LastSolutionDomain solution, String problem) {
        this.solution = solution;
        this.problem = problem;
    }

    public LastSolutionDomain getSolution() {
        return solution;
    }

    public void setSolution(LastSolutionDomain solution) {
        this.solution = solution;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }
}
