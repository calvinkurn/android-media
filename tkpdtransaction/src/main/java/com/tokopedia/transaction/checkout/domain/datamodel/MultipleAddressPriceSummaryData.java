package com.tokopedia.transaction.checkout.domain.datamodel;

import com.tkpd.library.utils.CurrencyFormatHelper;

/**
 * Created by kris on 1/29/18. Tokopedia
 */

public class MultipleAddressPriceSummaryData {

    private long quantity;

    private long totalProductPrice;

    private long totalShippingPrice;

    private long insurancePrice;

    private long additionalFee;

    private long promoDiscount;

    private long totalPayment;

    private String totalProductPriceText;

    private String totalShippingPriceText;

    private String insurancePriceText;

    private String additionalFeeText;

    private String totalPaymentText;

    private boolean isCouponActive;

    private boolean isPromoUsed;

    private boolean hasPromoSuggestion;

    private boolean isSuggestionVisible;

    private String promoSuggestionDescription;

    private String promoSuggestionCta;

    private String promoCtaColor;

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getTotalProductPrice() {
        return totalProductPrice;
    }

    public void setTotalProductPrice(long totalProductPrice) {
        this.totalProductPrice = totalProductPrice;
    }

    public long getTotalShippingPrice() {
        return totalShippingPrice;
    }

    public void setTotalShippingPrice(long totalShippingPrice) {
        this.totalShippingPrice = totalShippingPrice;
    }

    public long getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(long insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public long getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(long additionalFee) {
        this.additionalFee = additionalFee;
    }

    public long getPromoDiscount() {
        return promoDiscount;
    }

    public void setPromoDiscount(long promoDiscount) {
        this.promoDiscount = promoDiscount;
    }

    public long getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(long totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getQuantityText() {
        return String.valueOf(quantity);
    }

    public String getTotalProductPriceText() {
        return totalProductPriceText;
    }

    public void setTotalProductPriceText(String totalProductPriceText) {
        this.totalProductPriceText = totalProductPriceText;
    }

    public String getTotalShippingPriceText() {
        return totalShippingPriceText;
    }

    public void setTotalShippingPriceText(String totalShippingPriceText) {
        this.totalShippingPriceText = totalShippingPriceText;
    }

    public String getInsurancePriceText() {
        return insurancePriceText;
    }

    public void setInsurancePriceText(String insurancePriceText) {
        this.insurancePriceText = insurancePriceText;
    }

    public String getAdditionalFeeText() {
        return additionalFeeText;
    }

    public void setAdditionalFeeText(String additionalFeeText) {
        this.additionalFeeText = additionalFeeText;
    }

    public String getTotalPaymentText() {
        return formatPrice(getTotalPayment());
    }

    public void setTotalPaymentText(String totalPaymentText) {
        this.totalPaymentText = totalPaymentText;
    }

    public boolean isCouponActive() {
        return isCouponActive;
    }

    public void setCouponActive(boolean couponActive) {
        isCouponActive = couponActive;
    }

    public boolean isPromoUsed() {
        return isPromoUsed;
    }

    public void setPromoUsed(boolean promoUsed) {
        isPromoUsed = promoUsed;
    }

    public boolean isHasPromoSuggestion() {
        return hasPromoSuggestion;
    }

    public void setHasPromoSuggestion(boolean hasPromoSuggestion) {
        this.hasPromoSuggestion = hasPromoSuggestion;
    }

    public boolean isSuggestionVisible() {
        return isSuggestionVisible;
    }

    public void setSuggestionVisible(boolean suggestionVisible) {
        isSuggestionVisible = suggestionVisible;
    }

    public String getPromoSuggestionDescription() {
        return promoSuggestionDescription;
    }

    public void setPromoSuggestionDescription(String promoSuggestionDescription) {
        this.promoSuggestionDescription = promoSuggestionDescription;
    }

    public String getPromoSuggestionCta() {
        return promoSuggestionCta;
    }

    public void setPromoSuggestionCta(String promoSuggestionCta) {
        this.promoSuggestionCta = promoSuggestionCta;
    }

    public String getPromoCtaColor() {
        return promoCtaColor;
    }

    public void setPromoCtaColor(String promoCtaColor) {
        this.promoCtaColor = promoCtaColor;
    }

    private String formatPrice(long unformattedPrice) {
        String formattedPrice = CurrencyFormatHelper
                .ConvertToRupiah(String.valueOf(unformattedPrice));
        formattedPrice = formattedPrice.replace(",", ".");
        return formattedPrice;
    }
}
