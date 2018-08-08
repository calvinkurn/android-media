package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yoasfs on 10/10/17.
 */

public class NextActionDetailResponse {

    @SerializedName("solution")
    private String solution;

    @SerializedName("last")
    private LastResponse last;

    @SerializedName("step")
    private List<NextActionDetailStepResponse> step;

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public List<NextActionDetailStepResponse> getStep() {
        return step;
    }

    public void setStep(List<NextActionDetailStepResponse> step) {
        this.step = step;
    }

    public LastResponse getLast() {
        return last;
    }

    public void setLast(LastResponse last) {
        this.last = last;
    }
}
