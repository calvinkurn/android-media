package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.LastSolutionDomain;

/**
 * Created by yoasfs on 10/10/17.
 */

public class LastResponse {

    @SerializedName("solution")
    private LastSolutionResponse solution;

    @SerializedName("problem")
    private String problem;

    public LastSolutionResponse getSolution() {
        return solution;
    }

    public void setSolution(LastSolutionResponse solution) {
        this.solution = solution;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }
}
