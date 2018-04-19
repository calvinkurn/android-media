package com.tokopedia.transaction.cart.model.cartdata;

/**
 * Created by kris on 5/3/17. Tokopedia
 */

public class CartRatesData {

    public static final String REQ_TARIFF_KEY = "req_tariff-";
    private int ratesIndex;

    private String ratesResponse;

    private String errorResponse;

    private String keroRatesKey;

    private Integer courierServiceId;

    private Integer cartTotalProductPrice;

    private Integer cartAdditionalLogisticFee;

    private boolean isInsuranced;

    public int getRatesIndex() {
        return ratesIndex;
    }

    public void setRatesIndex(int ratesIndex) {
        this.ratesIndex = ratesIndex;
    }

    public String getRatesResponse() {
        return ratesResponse;
    }

    public void setRatesResponse(String ratesResponse) {
        this.ratesResponse = ratesResponse;
    }

    public Integer getCourierServiceId() {
        return courierServiceId;
    }

    public void setCourierServiceId(Integer courierServiceId) {
        this.courierServiceId = courierServiceId;
    }

    public Integer getCartTotalProductPrice() {
        return cartTotalProductPrice;
    }

    public void setCartTotalProductPrice(Integer cartTotalProductPrice) {
        this.cartTotalProductPrice = cartTotalProductPrice;
    }

    public Integer getCartAdditionalLogisticFee() {
        return cartAdditionalLogisticFee;
    }

    public void setCartAdditionalLogisticFee(Integer cartAdditionalLogisticFee) {
        this.cartAdditionalLogisticFee = cartAdditionalLogisticFee;
    }

    public String getKeroRatesKey() {
        return REQ_TARIFF_KEY + keroRatesKey;
    }

    public void setKeroRatesKey(String keroRatesKey) {
        this.keroRatesKey = keroRatesKey;
    }

    public boolean isInsuranced() {
        return isInsuranced;
    }

    public void setInsuranced(boolean insuranced) {
        isInsuranced = insuranced;
    }

    public String getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(String errorResponse) {
        this.errorResponse = errorResponse;
    }
}
