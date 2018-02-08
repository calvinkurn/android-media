
package com.tokopedia.ride.common.ride.domain.model;

import android.os.Bundle;

public class PaymentMethod {

    private String label;
    private String mode;
    private String saveUrl;
    private String deleteUrl;
    private String method;
    private String tokenId;
    private Boolean active;
    private Bundle saveBody;
    private Bundle removeBody;
    private String expiryMonth;
    private String expiryYear;
    private String cardType;
    private String bank;
    private String image;
    private String cardTypeImage;
    private String bankImage;
    private String maskedNumber;
    private boolean saveWebView;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getSaveUrl() {
        return saveUrl;
    }

    public void setSaveUrl(String saveUrl) {
        this.saveUrl = saveUrl;
    }

    public String getDeleteUrl() {
        return deleteUrl;
    }

    public void setDeleteUrl(String deleteUrl) {
        this.deleteUrl = deleteUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Bundle getSaveBody() {
        return saveBody;
    }

    public void setSaveBody(Bundle saveBody) {
        this.saveBody = saveBody;
    }

    public Bundle getRemoveBody() {
        return removeBody;
    }

    public void setRemoveBody(Bundle removeBody) {
        this.removeBody = removeBody;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCardTypeImage() {
        return cardTypeImage;
    }

    public void setCardTypeImage(String cardTypeImage) {
        this.cardTypeImage = cardTypeImage;
    }

    public String getBankImage() {
        return bankImage;
    }

    public void setBankImage(String bankImage) {
        this.bankImage = bankImage;
    }

    public String getMaskedNumber() {
        return maskedNumber;
    }

    public void setMaskedNumber(String maskedNumber) {
        this.maskedNumber = maskedNumber;
    }

    public boolean isSaveWebView() {
        return saveWebView;
    }

    public void setSaveWebView(boolean saveWebView) {
        this.saveWebView = saveWebView;
    }
}
