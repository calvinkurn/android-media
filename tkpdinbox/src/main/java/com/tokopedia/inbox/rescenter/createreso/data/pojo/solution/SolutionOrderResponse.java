package com.tokopedia.inbox.rescenter.createreso.data.pojo.solution;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionOrderResponse {
    @SerializedName("detail")
    private SolutionOrderDetailResponse detail;

    public SolutionOrderDetailResponse getDetail() {
        return detail;
    }

    public void setDetail(SolutionOrderDetailResponse detail) {
        this.detail = detail;
    }

}
