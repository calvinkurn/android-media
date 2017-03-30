package com.tokopedia.inbox.rescenter.product.domain.model;

import java.util.List;

/**
 * Created by hangnadi on 3/28/17.
 */

public class ProductDetailData {
    private boolean success;
    private String messageError;
    private int errorCode;
    private String productName;
    private String productThumbUrl;
    private String productPrice;
    private String trouble;
    private String troubleReason;
    private List<AttachmentProductDomainData> attachment;

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

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductThumbUrl() {
        return productThumbUrl;
    }

    public void setProductThumbUrl(String productThumbUrl) {
        this.productThumbUrl = productThumbUrl;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getTrouble() {
        return trouble;
    }

    public void setTrouble(String trouble) {
        this.trouble = trouble;
    }

    public String getTroubleReason() {
        return troubleReason;
    }

    public void setTroubleReason(String troubleReason) {
        this.troubleReason = troubleReason;
    }


    public List<AttachmentProductDomainData> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<AttachmentProductDomainData> attachment) {
        this.attachment = attachment;
    }
}
