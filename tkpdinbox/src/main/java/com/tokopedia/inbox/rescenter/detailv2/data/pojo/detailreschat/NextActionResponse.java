package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 10/10/17.
 */

public class NextActionResponse {

    @SerializedName("last")
    private String last;

    @SerializedName("detail")
    private NextActionDetailResponse detail;

    @SerializedName("problem")
    private String problem;

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public NextActionDetailResponse getDetail() {
        return detail;
    }

    public void setDetail(NextActionDetailResponse detail) {
        this.detail = detail;
    }
}
