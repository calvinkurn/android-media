package com.tokopedia.inbox.rescenter.historyaddress.domain.model;

import java.util.List;

/**
 * Created by hangnadi on 3/27/17.
 */

public class HistoryAddressData {
    private boolean success;
    private String messageError;
    private List<HistoryAddressItemDomainData> listHistoryAddress;
    private int errorCode;
    private int resolutionStatus;

    public int getResolutionStatus() {
        return resolutionStatus;
    }

    public void setResolutionStatus(int resolutionStatus) {
        this.resolutionStatus = resolutionStatus;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public List<HistoryAddressItemDomainData> getListHistoryAddress() {
        return listHistoryAddress;
    }

    public void setListHistoryAddress(List<HistoryAddressItemDomainData> listHistoryAddress) {
        this.listHistoryAddress = listHistoryAddress;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
