
package com.tokopedia.inbox.contactus.model.solution;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SolutionResult {

    @SerializedName("is_success")
    @Expose
    private int isSuccess;
    @SerializedName("solutions")
    @Expose
    private Solution solutions;

    /**
     *
     * @return
     *     The isSuccess
     */
    public int getIsSuccess() {
        return isSuccess;
    }

    /**
     *
     * @param isSuccess
     *     The is_success
     */
    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    /**
     *
     * @return
     *     The solutions
     */
    public Solution getSolutions() {
        return solutions;
    }

    /**
     *
     * @param solutions
     *     The solutions
     */
    public void setSolutions(Solution solutions) {
        this.solutions = solutions;
    }

}