
package com.tokopedia.transaction.bcaoneklik.model.creditcard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreditCardResponseItem {

    @SerializedName("token_id")
    @Expose
    private String tokenId;
    @SerializedName("masked_number")
    @Expose
    private String maskedNumber;
    @SerializedName("expiry_month")
    @Expose
    private String expiryMonth;
    @SerializedName("expiry_year")
    @Expose
    private String expiryYear;
    @SerializedName("card_type")
    @Expose
    private String cardType;
    @SerializedName("card_type_name")
    private String cardTypeName;
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

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getMaskedNumber() {
        return maskedNumber;
    }

    public void setMaskedNumber(String maskedNumber) {
        this.maskedNumber = maskedNumber;
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

    public String getCardTypeName() {
        return cardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
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

}
