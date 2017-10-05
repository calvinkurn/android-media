package com.tokopedia.inbox.rescenter.product.domain.model;

import java.util.List;

/**
 * Created by hangnadi on 3/27/17.
 */

public class ListProductDomainData {
    private boolean success;
    private String messageError;
    private List<ListProductItemDomainData> listHistoryAddress;
    private int errorCode;

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

    public List<ListProductItemDomainData> getListHistoryAddress() {
        return listHistoryAddress;
    }

    public void setListHistoryAddress(List<ListProductItemDomainData> listHistoryAddress) {
        this.listHistoryAddress = listHistoryAddress;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
