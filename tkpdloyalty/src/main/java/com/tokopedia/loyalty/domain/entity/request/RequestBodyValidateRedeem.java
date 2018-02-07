package com.tokopedia.loyalty.domain.entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class RequestBodyValidateRedeem {

    @SerializedName("catalog_id")
    @Expose
    private int catalogId;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("is_gift")
    @Expose
    private int isGift;
    @SerializedName("gift_user_id")
    @Expose
    private int giftUserId;
    @SerializedName("gift_email")
    @Expose
    private String giftEmail;

    private RequestBodyValidateRedeem(Builder builder) {
        setCatalogId(builder.catalogId);
        setUserId(builder.userId);
        setIsGift(builder.isGift);
        setGiftUserId(builder.giftUserId);
        setGiftEmail(builder.giftEmail);
    }

    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setIsGift(int isGift) {
        this.isGift = isGift;
    }

    public void setGiftUserId(int giftUserId) {
        this.giftUserId = giftUserId;
    }

    public void setGiftEmail(String giftEmail) {
        this.giftEmail = giftEmail;
    }


    public static final class Builder {
        private int catalogId;
        private int userId;
        private int isGift;
        private int giftUserId;
        private String giftEmail;

        public Builder() {
        }

        public static Builder buildRequest() {
            return new Builder();
        }

        public Builder catalogId(int val) {
            catalogId = val;
            return this;
        }

        public Builder userId(int val) {
            userId = val;
            return this;
        }

        public Builder isGift(int val) {
            isGift = val;
            return this;
        }

        public Builder giftUserId(int val) {
            giftUserId = val;
            return this;
        }

        public Builder giftEmail(String val) {
            giftEmail = val;
            return this;
        }

        public RequestBodyValidateRedeem build() {
            return new RequestBodyValidateRedeem(this);
        }
    }
}
