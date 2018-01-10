package com.tokopedia.transaction.bcaoneklik.model.creditcard;

import java.io.Serializable;

/**
 * Created by kris on 8/23/17. Tokopedia
 */

public class CreditCardModelItem implements Serializable {

    private String tokenId;

    private String maskedNumber;

    private String expiryMonth;

    private String expiryYear;

    private String cardType;

    private String bank;

    private String image;

    private String cardTypeImage;

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
}
