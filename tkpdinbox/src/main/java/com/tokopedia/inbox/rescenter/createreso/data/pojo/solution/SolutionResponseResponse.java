package com.tokopedia.inbox.rescenter.createreso.data.pojo.solution;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yoasfs on 14/08/17.
 */

public class SolutionResponseResponse {
    @SerializedName("solution")
    @Expose
    private List<SolutionResponse> solution;
    @SerializedName("require")
    @Expose
    private RequireResponse require;

    public List<SolutionResponse> getSolution() {
        return solution;
    }

    public void setSolution(List<SolutionResponse> solution) {
        this.solution = solution;
    }

    public RequireResponse getRequire() {
        return require;
    }

    public void setRequire(RequireResponse require) {
        this.require = require;
    }

    @Override
    public String toString() {
        return "SolutionResponseResponse{" +
                "solution='" + solution.toString() + '\'' +
                "require='" + require.toString() + '\'' +
                '}';
    }
}
