package com.tokopedia.inbox.rescenter.historyawb.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hangnadi on 3/24/17.
 */

public class HistoryAwbEntity {

    @SerializedName("listHistoryAwb")
    private List<ListHistoryAwb> listHistoryAwb;

    @SerializedName("resoStatus")
    private int resolutionStatus;

    public List<ListHistoryAwb> getListHistoryAwb() {
        return listHistoryAwb;
    }

    public void setListHistoryAwb(List<ListHistoryAwb> listHistoryAwb) {
        this.listHistoryAwb = listHistoryAwb;
    }

    public int getResolutionStatus() {
        return resolutionStatus;
    }

    public void setResolutionStatus(int resolutionStatus) {
        this.resolutionStatus = resolutionStatus;
    }
}
