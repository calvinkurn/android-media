package com.tokopedia.transaction.checkout.view.holderitemdata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Aghny A. Putra on 28/02/18
 */

public class CartPromo implements Parcelable {

    public static final int TYPE_PROMO_NOT_ACTIVE = 0;
    public static final int TYPE_PROMO_VOUCHER = 1;
    public static final int TYPE_PROMO_COUPON = 2;

    private int typePromo;

    private String voucherCode;
    private String voucherMessage;
    private long voucherDiscountAmount;

    private String couponTitle;
    private String couponMessage;
    private String couponCode;
    private long couponDiscountAmount;

    public CartPromo() {
        setPromoNotActive();
    }

    public void setPromoVoucherType(String voucherCode,
                                    String voucherMessage,
                                    long voucherDiscountAmount) {
        this.typePromo = TYPE_PROMO_VOUCHER;
        this.voucherMessage = voucherMessage;
        this.voucherCode = voucherCode;
        this.voucherDiscountAmount = voucherDiscountAmount;
    }

    public void setPromoCouponType(String couponTitle,
                                   String couponCode,
                                   String couponMessage,
                                   long couponDiscountAmount) {
        this.typePromo = TYPE_PROMO_COUPON;
        this.couponMessage = couponMessage;
        this.couponCode = couponCode;
        this.couponDiscountAmount = couponDiscountAmount;
        this.couponTitle = couponTitle;
    }

    public void setPromoNotActive() {
        this.typePromo = TYPE_PROMO_NOT_ACTIVE;
        this.voucherMessage = "";
        this.voucherCode = "";
        this.voucherDiscountAmount = 0;
        this.couponMessage = "";
        this.couponCode = "";
        this.couponDiscountAmount = 0;
        this.couponTitle = "";
    }

    public static int getTypePromoNotActive() {
        return TYPE_PROMO_NOT_ACTIVE;
    }

    public static int getTypePromoVoucher() {
        return TYPE_PROMO_VOUCHER;
    }

    public static int getTypePromoCoupon() {
        return TYPE_PROMO_COUPON;
    }

    public int getTypePromo() {
        return typePromo;
    }

    public void setTypePromo(int typePromo) {
        this.typePromo = typePromo;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getVoucherMessage() {
        return voucherMessage;
    }

    public void setVoucherMessage(String voucherMessage) {
        this.voucherMessage = voucherMessage;
    }

    public long getVoucherDiscountAmount() {
        return voucherDiscountAmount;
    }

    public void setVoucherDiscountAmount(long voucherDiscountAmount) {
        this.voucherDiscountAmount = voucherDiscountAmount;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }

    public String getCouponMessage() {
        return couponMessage;
    }

    public void setCouponMessage(String couponMessage) {
        this.couponMessage = couponMessage;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public long getCouponDiscountAmount() {
        return couponDiscountAmount;
    }

    public void setCouponDiscountAmount(long couponDiscountAmount) {
        this.couponDiscountAmount = couponDiscountAmount;
    }

    public static Creator<CartPromo> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.typePromo);
        dest.writeString(this.voucherCode);
        dest.writeString(this.voucherMessage);
        dest.writeLong(this.voucherDiscountAmount);
        dest.writeString(this.couponTitle);
        dest.writeString(this.couponMessage);
        dest.writeString(this.couponCode);
        dest.writeLong(this.couponDiscountAmount);
    }

    protected CartPromo(Parcel in) {
        this.typePromo = in.readInt();
        this.voucherCode = in.readString();
        this.voucherMessage = in.readString();
        this.voucherDiscountAmount = in.readLong();
        this.couponTitle = in.readString();
        this.couponMessage = in.readString();
        this.couponCode = in.readString();
        this.couponDiscountAmount = in.readLong();
    }

    public static final Creator<CartPromo> CREATOR = new Creator<CartPromo>() {
        @Override
        public CartPromo createFromParcel(Parcel source) {
            return new CartPromo(source);
        }

        @Override
        public CartPromo[] newArray(int size) {
            return new CartPromo[size];
        }
    };
}
