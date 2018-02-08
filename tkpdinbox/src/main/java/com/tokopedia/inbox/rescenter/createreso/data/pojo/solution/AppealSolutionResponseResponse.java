package com.tokopedia.inbox.rescenter.createreso.data.pojo.solution;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yoasfs on 14/08/17.
 */

public class AppealSolutionResponseResponse {
    @SerializedName("solutionLists")
    @Expose
    private List<AppealSolutionResponse> solution;

    @SerializedName("freeReturn")
    @Expose
    private AppealFreeReturnResponse freeReturn;

    public List<AppealSolutionResponse> getSolution() {
        return solution;
    }

    public AppealFreeReturnResponse getFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(AppealFreeReturnResponse freeReturn) {
        this.freeReturn = freeReturn;
    }

    public void setSolution(List<AppealSolutionResponse> solution) {
        this.solution = solution;
    }

    @Override
    public String toString() {
        return "AppealSolutionResponseResponse{" +
                "solutionLists='" + solution.toString() + '\'' +
                ", freeReturn='" + freeReturn.toString() + '\'' +
                '}';
    }
}
