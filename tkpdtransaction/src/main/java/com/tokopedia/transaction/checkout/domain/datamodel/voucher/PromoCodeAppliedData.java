package com.tokopedia.transaction.checkout.domain.datamodel.voucher;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 06/03/18.
 */

public class PromoCodeAppliedData implements Parcelable {
    public static final int TYPE_VOUCHER = 1;
    public static final int TYPE_COUPON = 2;

    private int typeVoucher;
    private String promoCode;
    private String description;
    private String couponTitle;
    private int amount;

    private PromoCodeAppliedData(Builder builder) {
        setTypeVoucher(builder.typeVoucher);
        setPromoCode(builder.promoCode);
        setDescription(builder.description);
        setCouponTitle(builder.couponTitle);
        setAmount(builder.amount);
    }

    public int getTypeVoucher() {
        return typeVoucher;
    }

    public void setTypeVoucher(int typeVoucher) {
        this.typeVoucher = typeVoucher;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.typeVoucher);
        dest.writeString(this.promoCode);
        dest.writeString(this.description);
        dest.writeString(this.couponTitle);
        dest.writeInt(this.amount);
    }

    public PromoCodeAppliedData() {
    }

    protected PromoCodeAppliedData(Parcel in) {
        this.typeVoucher = in.readInt();
        this.promoCode = in.readString();
        this.description = in.readString();
        this.couponTitle = in.readString();
        this.amount = in.readInt();
    }

    public static final Creator<PromoCodeAppliedData> CREATOR =
            new Creator<PromoCodeAppliedData>() {
                @Override
                public PromoCodeAppliedData createFromParcel(Parcel source) {
                    return new PromoCodeAppliedData(source);
                }

                @Override
                public PromoCodeAppliedData[] newArray(int size) {
                    return new PromoCodeAppliedData[size];
                }
            };


    public static final class Builder {
        private int typeVoucher;
        private String promoCode;
        private String description;
        private String couponTitle;
        private int amount;

        public Builder() {
        }

        public Builder typeVoucher(int val) {
            typeVoucher = val;
            return this;
        }

        public Builder promoCode(String val) {
            promoCode = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder couponTitle(String val) {
            couponTitle = val;
            return this;
        }

        public Builder amount(int val) {
            amount = val;
            return this;
        }

        public PromoCodeAppliedData build() {
            return new PromoCodeAppliedData(this);
        }
    }
}
