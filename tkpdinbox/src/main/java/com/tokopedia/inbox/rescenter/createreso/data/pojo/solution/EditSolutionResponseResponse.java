package com.tokopedia.inbox.rescenter.createreso.data.pojo.solution;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yoasfs on 14/08/17.
 */

public class EditSolutionResponseResponse {
    @SerializedName("solutionLists")
    @Expose
    private List<EditSolutionResponse> solution;

    @SerializedName("freeReturn")
    @Expose
    private EditFreeReturnResponse freeReturn;

    public List<EditSolutionResponse> getSolution() {
        return solution;
    }

    public EditFreeReturnResponse getFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(EditFreeReturnResponse freeReturn) {
        this.freeReturn = freeReturn;
    }

    public void setSolution(List<EditSolutionResponse> solution) {
        this.solution = solution;
    }

    @Override
    public String toString() {
        return "EditSolutionResponseResponse{" +
                "solutionLists='" + solution.toString() + '\'' +
                "freeReturn='" + freeReturn.toString() + '\'' +
                '}';
    }
}
