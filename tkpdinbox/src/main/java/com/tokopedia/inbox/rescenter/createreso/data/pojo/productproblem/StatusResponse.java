package com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yoasfs on 14/08/17.
 */

public class StatusResponse {
    @SerializedName("delivered")
    @Expose
    private boolean delivered;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("trouble")
    @Expose
    private List<StatusTroubleResponse> trouble;
    @SerializedName("info")
    @Expose
    private StatusInfoResponse info;

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StatusTroubleResponse> getTrouble() {
        return trouble;
    }

    public void setTrouble(List<StatusTroubleResponse> trouble) {
        this.trouble = trouble;
    }

    public StatusInfoResponse getInfo() {
        return info;
    }

    public void setInfo(StatusInfoResponse info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "StatusResponse{" +
                "delivered='" + delivered + '\'' +
                ", name='" + name + '\'' +
                ", trouble='" + trouble.toString() + '\'' +
                ", info=" + info.toString() +
                '}';
    }
}
