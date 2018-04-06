package com.tokopedia.transaction.cart.model.cartdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.transaction.utils.ValueConverter;

import java.util.ArrayList;
import java.util.List;

public class CartData implements Parcelable {

    @SerializedName("checkout_notif_button")
    @Expose
    private Integer checkoutNotifButton;
    @SerializedName("cashback_idr")
    @Expose
    private String cashbackIdr;
    @SerializedName("lp_amount_idr")
    @Expose
    private String lpAmountIdr;
    @SerializedName("gateway")
    @Expose
    private String gateway;
    @SerializedName("gateway_list")
    @Expose
    private List<GatewayList> gatewayList = new ArrayList<GatewayList>();
    @SerializedName("deposit")
    @Expose
    private Integer deposit;
    @SerializedName("deposit_idr")
    @Expose
    private String depositIdr;
    @SerializedName("list")
    @Expose
    private List<CartItem> cartItemList = new ArrayList<CartItem>();
    @SerializedName("checkout_notif_error")
    @Expose
    private String checkoutNotifError;
    @SerializedName("ecash_flag")
    @Expose
    private String ecashFlag;
    @SerializedName("not_empty")
    @Expose
    private Integer notEmpty;
    @SerializedName("voucher_code")
    @Expose
    private String voucherCode;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("token_kero")
    @Expose
    private String tokenKero;
    @SerializedName("ut")
    @Expose
    private String ut;
    @SerializedName("credit_card")
    @Expose
    private CreditCard creditCard;
    @SerializedName("grand_total_idr")
    @Expose
    private String grandTotalIdr;
    @SerializedName("cashback")
    @Expose
    private int cashback;
    @SerializedName("grand_total")
    @Expose
    private Integer grandTotal;
    @SerializedName("lp_amount")
    @Expose
    private int lpAmount;
    @SerializedName("grand_total_without_lp")
    @Expose
    private String grandTotalWithoutLP;
    @SerializedName("grand_total_without_lp_idr")
    @Expose
    private String grandTotalWithoutLPIDR;
    @SerializedName("cart_shipping_rate")
    @Expose
    private long cartShippingRate;
    @SerializedName("donation")
    @Expose
    private CartDonation donation;
    @SerializedName("promo_suggestion")
    private CartPromo cartPromo;
    @SerializedName("is_coupon_active")
    private int isCouponActive = 0;
    @SerializedName("enable_cancel_partial")
    @Expose
    private boolean enableCancelPartial;

    public String getGrandTotalWithoutLP() {
        return grandTotalWithoutLP;
    }

    public void setGrandTotalWithoutLP(String grandTotalWithoutLP) {
        this.grandTotalWithoutLP = grandTotalWithoutLP;
    }

    public Integer getCheckoutNotifButton() {
        return checkoutNotifButton;
    }

    public void setCheckoutNotifButton(Integer checkoutNotifButton) {
        this.checkoutNotifButton = checkoutNotifButton;
    }

    public String getCashbackIdr() {
        return cashbackIdr;
    }

    public void setCashbackIdr(String cashbackIdr) {
        this.cashbackIdr = cashbackIdr;
    }

    public String getLpAmountIdr() {
        return lpAmountIdr;
    }

    public void setLpAmountIdr(String lpAmountIdr) {
        this.lpAmountIdr = lpAmountIdr;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public List<GatewayList> getGatewayList() {
        return gatewayList;
    }

    public void setGatewayList(List<GatewayList> gatewayList) {
        this.gatewayList = gatewayList;
    }

    public Integer getDeposit() {
        return deposit;
    }

    public void setDeposit(Integer deposit) {
        this.deposit = deposit;
    }

    public String getDepositIdr() {
        return depositIdr;
    }

    public void setDepositIdr(String depositIdr) {
        this.depositIdr = depositIdr;
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> list) {
        this.cartItemList = list;
    }

    public String getCheckoutNotifError() {
        return checkoutNotifError;
    }

    public void setCheckoutNotifError(String checkoutNotifError) {
        this.checkoutNotifError = checkoutNotifError;
    }

    public String getEcashFlag() {
        return ecashFlag;
    }

    public void setEcashFlag(String ecashFlag) {
        this.ecashFlag = ecashFlag;
    }

    public Integer getNotEmpty() {
        return notEmpty;
    }

    public void setNotEmpty(Integer notEmpty) {
        this.notEmpty = notEmpty;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public String getGrandTotalIdr() {
        return ValueConverter.getStringIdrFormat(grandTotal);
    }

    public void setGrandTotalIdr(String grandTotalIdr) {
        this.grandTotalIdr = grandTotalIdr;
    }

    public int getCashback() {
        return cashback;
    }

    public void setCashback(int cashback) {
        this.cashback = cashback;
    }

    public Integer getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Integer grandTotal) {
        this.grandTotal = grandTotal;
    }

    public int getLpAmount() {
        return lpAmount;
    }

    public void setLpAmount(int lpAmount) {
        this.lpAmount = lpAmount;
    }

    public String getGrandTotalWithoutLPIDR() {
        return grandTotalWithoutLPIDR;
    }

    public void setGrandTotalWithoutLPIDR(String grandTotalWithoutLPIDR) {
        this.grandTotalWithoutLPIDR = grandTotalWithoutLPIDR;
    }

    public long getCartShippingRate() {
        return cartShippingRate;
    }

    public void setCartShippingRate(long cartShippingRate) {
        this.cartShippingRate = cartShippingRate;
    }

    public String getTokenKero() {
        return tokenKero;
    }

    public void setTokenKero(String tokenKero) {
        this.tokenKero = tokenKero;
    }

    public String getUt() {
        return ut;
    }

    public void setUt(String ut) {
        this.ut = ut;
    }

    public CartDonation getDonation() {
        return donation;
    }

    public void setDonation(CartDonation donation) {
        this.donation = donation;
    }

    public CartPromo getCartPromo() {
        return cartPromo;
    }

    public void setCartPromo(CartPromo cartPromo) {
        this.cartPromo = cartPromo;
    }

    public int getIsCouponActive() {
        return isCouponActive;
    }

    public void setIsCouponActive(int isCouponActive) {
        this.isCouponActive = isCouponActive;
    }

    public boolean isEnableCancelPartial() {
        return enableCancelPartial;
    }

    public void setEnableCancelPartial(boolean enableCancelPartial) {
        this.enableCancelPartial = enableCancelPartial;
    }

    public CartData() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.checkoutNotifButton);
        dest.writeString(this.cashbackIdr);
        dest.writeString(this.lpAmountIdr);
        dest.writeString(this.gateway);
        dest.writeTypedList(this.gatewayList);
        dest.writeValue(this.deposit);
        dest.writeString(this.depositIdr);
        dest.writeTypedList(this.cartItemList);
        dest.writeString(this.checkoutNotifError);
        dest.writeString(this.ecashFlag);
        dest.writeValue(this.notEmpty);
        dest.writeString(this.voucherCode);
        dest.writeString(this.token);
        dest.writeString(this.tokenKero);
        dest.writeString(this.ut);
        dest.writeParcelable(this.creditCard, flags);
        dest.writeString(this.grandTotalIdr);
        dest.writeInt(this.cashback);
        dest.writeValue(this.grandTotal);
        dest.writeInt(this.lpAmount);
        dest.writeString(this.grandTotalWithoutLP);
        dest.writeString(this.grandTotalWithoutLPIDR);
        dest.writeLong(this.cartShippingRate);
        dest.writeParcelable(this.donation, flags);
        dest.writeParcelable(this.cartPromo, flags);
        dest.writeInt(this.isCouponActive);
        dest.writeByte((byte) (enableCancelPartial ? 1 : 0));
    }

    protected CartData(Parcel in) {
        this.checkoutNotifButton = (Integer) in.readValue(Integer.class.getClassLoader());
        this.cashbackIdr = in.readString();
        this.lpAmountIdr = in.readString();
        this.gateway = in.readString();
        this.gatewayList = in.createTypedArrayList(GatewayList.CREATOR);
        this.deposit = (Integer) in.readValue(Integer.class.getClassLoader());
        this.depositIdr = in.readString();
        this.cartItemList = in.createTypedArrayList(CartItem.CREATOR);
        this.checkoutNotifError = in.readString();
        this.ecashFlag = in.readString();
        this.notEmpty = (Integer) in.readValue(Integer.class.getClassLoader());
        this.voucherCode = in.readString();
        this.token = in.readString();
        this.tokenKero = in.readString();
        this.ut= in.readString();
        this.creditCard = in.readParcelable(CreditCard.class.getClassLoader());
        this.grandTotalIdr = in.readString();
        this.cashback = in.readInt();
        this.grandTotal = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lpAmount = in.readInt();
        this.grandTotalWithoutLP = in.readString();
        this.grandTotalWithoutLPIDR = in.readString();
        this.cartShippingRate = in.readLong();
        this.donation = in.readParcelable(CartDonation.class.getClassLoader());
        this.cartPromo = in.readParcelable(CartPromo.class.getClassLoader());
        this.isCouponActive = in.readInt();
        this.enableCancelPartial = in.readByte() != 0;
    }

    public static final Creator<CartData> CREATOR = new Creator<CartData>() {
        @Override
        public CartData createFromParcel(Parcel source) {
            return new CartData(source);
        }

        @Override
        public CartData[] newArray(int size) {
            return new CartData[size];
        }
    };
}
