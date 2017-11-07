package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.SolutionResponse;

/**
 * Created by yfsx on 07/11/17.
 */
public class LogResponse {
    /**
     * id : 1801762
     * action : Pembeli membuat komplain penukaran barang dan pengembalian dana sebesar Rp. 10.000
     * solution : {"id":3,"name":"retur barang dan kembalikan dana","nameCustom":"","amount":{"idr":"Rp. 10.000","integer":10000}}
     * actionBy : {"id":1,"name":"Pembeli"}
     * createBy : {"id":5512337,"name":"Jovina Maulida Annisa Lubis","picture":{"fullUrl":"https://imagerouter-staging.tokopedia.com/image/v1/u/5512337/user_profile/desktop","thumbnail":"https://imagerouter-staging.tokopedia.com/image/v1/u/5512337/user_thumbnail/desktop"}}
     * createTime : 2017-08-10T14:29:30.900425Z
     * createTimeStr : 2017-08-10 14:29:30
     */

    @SerializedName("id")
    private int id;
    @SerializedName("action")
    private String action;
    @SerializedName("solution")
    private SolutionResponse solution;
    @SerializedName("actionBy")
    private CreateByResponse actionBy;
    @SerializedName("createBy")
    private CreateByResponse createBy;
    @SerializedName("createTime")
    private String createTime;
    @SerializedName("createTimeStr")
    private String createTimeStr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public SolutionResponse getSolution() {
        return solution;
    }

    public void setSolution(SolutionResponse solution) {
        this.solution = solution;
    }

    public CreateByResponse getActionBy() {
        return actionBy;
    }

    public void setActionBy(CreateByResponse actionBy) {
        this.actionBy = actionBy;
    }

    public CreateByResponse getCreateBy() {
        return createBy;
    }

    public void setCreateBy(CreateByResponse createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }


}
