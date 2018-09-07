package com.tokopedia.transaction.bcaoneklik.model.creditcard;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by kris on 8/23/17. Tokopedia
 */

public class CreditCardModelItem implements Parcelable {

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tokenId);
        dest.writeString(this.maskedNumber);
        dest.writeString(this.expiryMonth);
        dest.writeString(this.expiryYear);
        dest.writeString(this.cardType);
        dest.writeString(this.bank);
        dest.writeString(this.image);
        dest.writeString(this.cardTypeImage);
    }

    public CreditCardModelItem() {
    }

    protected CreditCardModelItem(Parcel in) {
        this.tokenId = in.readString();
        this.maskedNumber = in.readString();
        this.expiryMonth = in.readString();
        this.expiryYear = in.readString();
        this.cardType = in.readString();
        this.bank = in.readString();
        this.image = in.readString();
        this.cardTypeImage = in.readString();
    }

    public static final Creator<CreditCardModelItem> CREATOR = new Creator<CreditCardModelItem>() {
        @Override
        public CreditCardModelItem createFromParcel(Parcel source) {
            return new CreditCardModelItem(source);
        }

        @Override
        public CreditCardModelItem[] newArray(int size) {
            return new CreditCardModelItem[size];
        }
    };
}
