
package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentMethodEntity {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("save_url")
    @Expose
    private String saveUrl;
    @SerializedName("delete_url")
    @Expose
    private String deleteUrl;
    @SerializedName("method")
    @Expose
    private String method;
    @SerializedName("token_id")
    @Expose
    private String tokenId;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("save_body")
    @Expose
    private JsonObject saveBody;
    @SerializedName("remove_body")
    @Expose
    private JsonObject removeBody;
    @SerializedName("masked_num")
    @Expose
    private String maskedNum;
    @SerializedName("expiry_month")
    @Expose
    private String expiryMonth;
    @SerializedName("expiry_year")
    @Expose
    private String expiryYear;
    @SerializedName("card_type")
    @Expose
    private String cardType;
    @SerializedName("bank")
    @Expose
    private String bank;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("card_type_image")
    @Expose
    private String cardTypeImage;
    @SerializedName("bank_image")
    @Expose
    private String bankImage;
    @SerializedName("save_webview")
    @Expose
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

    public JsonObject getSaveBody() {
        return saveBody;
    }

    public void setSaveBody(JsonObject saveBody) {
        this.saveBody = saveBody;
    }

    public JsonObject getRemoveBody() {
        return removeBody;
    }

    public void setRemoveBody(JsonObject removeBody) {
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

    public String getMaskedNum() {
        return maskedNum;
    }

    public void setMaskedNum(String maskedNum) {
        this.maskedNum = maskedNum;
    }

    public boolean isSaveWebView() {
        return saveWebView;
    }
}
