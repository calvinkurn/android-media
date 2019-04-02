package com.tokopedia.inbox.rescenter.historyaction.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hangnadi on 3/27/17.
 */

public class HistoryActionEntity {

    @SerializedName("listHistoryAction")
    private List<ListHistoryAction> listHistoryAction;

    @SerializedName("resoStatus")
    private int resolutionStatus;

    public int getResolutionStatus() {
        return resolutionStatus;
    }

    public void setResolutionStatus(int resolutionStatus) {
        this.resolutionStatus = resolutionStatus;
    }

    public List<ListHistoryAction> getListHistoryAction() {
        return listHistoryAction;
    }

    public void setListHistoryAction(List<ListHistoryAction> listHistoryAction) {
        this.listHistoryAction = listHistoryAction;
    }

}
