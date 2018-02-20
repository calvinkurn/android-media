package com.tokopedia.transaction.checkout.view.holderitemdata;

/**
 * @author anggaprasetiyo on 20/02/18.
 */

public class CartItemPromoHolderData {

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

    public int getTypePromo() {
        return typePromo;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public String getVoucherMessage() {
        return voucherMessage;
    }


    public String getCouponTitle() {
        return couponTitle;
    }

    public String getCouponMessage() {
        return couponMessage;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public long getVoucherDiscountAmount() {
        return voucherDiscountAmount;
    }

    public long getCouponDiscountAmount() {
        return couponDiscountAmount;
    }

    public void setPromoVoucherType(
            String voucherCode, String voucherMessage, long voucherDiscountAmount
    ) {
        this.typePromo = TYPE_PROMO_VOUCHER;
        this.voucherMessage = voucherMessage;
        this.voucherCode = voucherCode;
        this.voucherDiscountAmount = voucherDiscountAmount;
    }

    public void setPromoCouponType(
            String couponTitle, String couponCode, String couponMessage, long couponDiscountAmount
    ) {
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
}
