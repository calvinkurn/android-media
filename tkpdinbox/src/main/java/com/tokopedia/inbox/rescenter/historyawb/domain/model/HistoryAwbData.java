package com.tokopedia.inbox.rescenter.historyawb.domain.model;

import java.util.List;

/**
 * Created by hangnadi on 3/23/17.
 */

public class HistoryAwbData {
    private boolean success;
    private int errorCode;
    private String messageError;
    private int resolutionStatus;
    private List<HistoryAwbItemDomainData> listHistoryAwb;

    public int getResolutionStatus() {
        return resolutionStatus;
    }

    public void setResolutionStatus(int resolutionStatus) {
        this.resolutionStatus = resolutionStatus;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setListHistoryAwb(List<HistoryAwbItemDomainData> listHistoryAwb) {
        this.listHistoryAwb = listHistoryAwb;
    }

    public List<HistoryAwbItemDomainData> getListHistoryAwb() {
        return listHistoryAwb;
    }
}
