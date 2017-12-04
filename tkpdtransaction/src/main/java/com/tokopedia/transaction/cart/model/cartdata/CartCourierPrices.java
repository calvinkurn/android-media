package com.tokopedia.transaction.cart.model.cartdata;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.transaction.addtocart.model.kero.Product;
import com.tokopedia.transaction.utils.ValueConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 5/23/17. Tokopedia
 */

public class CartCourierPrices {

    private int cartIndex;

    private int shipmentPrice = 0;

    private int insurancePrice = 0;

    private int insuranceMode = 0;

    private int insuranceUsedType;

    private String insuranceUsedInfo;

    private int additionFee = 0;

    private int cartProductPrice = 0;

    private int cartSubtotal = 0;

    private int cartForceInsurance = 0;

    private int cartInsuranceProd = 0;

    private List<Integer> cartProductInsurances = new ArrayList<>();

    private String shipmentPriceIdr = "";

    private String insurancePriceIdr = "";

    private String additionFeeIdr = "";

    private String cartSubtotalIdr = "";

    private String keroWeight = "";

    private String key = "";

    private String keroValue;

    public int getShipmentPrice() {
        return shipmentPrice;
    }

    public void setShipmentPrice(int shipmentPrice) {
        this.shipmentPrice = shipmentPrice;
    }

    public int getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(int insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public int getInsuranceMode() {
        return insuranceMode;
    }

    public void setInsuranceMode(int insuranceMode) {
        this.insuranceMode = insuranceMode;
    }

    public int getAdditionFee() {
        return additionFee;
    }

    public void setAdditionFee(int additionFee) {
        this.additionFee = additionFee;
    }

    public int getCartSubtotal() {
        return cartSubtotal;
    }

    public void setCartSubtotal(boolean useInsurance) {
        cartSubtotal = cartProductPrice + shipmentPrice + additionFee;
        if (useInsurance) {
            cartSubtotal = cartSubtotal + insurancePrice;
        }
        CommonUtils.dumper(String.valueOf("PORING SUBTOTAL " + cartSubtotal));
    }

    public String getShipmentPriceIdr() {
        return getStringIdrFormat(shipmentPrice);
    }

    public void setShipmentPriceIdr(String shipmentPriceIdr) {
        this.shipmentPriceIdr = shipmentPriceIdr;
    }

    public String getInsurancePriceIdr() {
        return getStringIdrFormat(insurancePrice);
    }

    public void setInsurancePriceIdr(String insurancePriceIdr) {
        this.insurancePriceIdr = insurancePriceIdr;
    }

    public String getAdditionFeeIdr() {
        return getStringIdrFormat(additionFee);
    }

    public String getSumAdditionFeeInsuranceIdr() {
        return getStringIdrFormat(additionFee + insurancePrice);
    }

    public void setAdditionFeeIdr(String additionFeeIdr) {
        this.additionFeeIdr = additionFeeIdr;
    }

    public String getCartSubtotalIdr() {
        return getStringIdrFormat(cartSubtotal);
    }

    public void setCartSubtotalIdr(String cartSubtotalIdr) {
        this.cartSubtotalIdr = cartSubtotalIdr;
    }

    public String getCartProductPriceIdr() {
        return getStringIdrFormat(cartProductPrice);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getCartIndex() {
        return cartIndex;
    }

    public String getKeroWeight() {
        return keroWeight;
    }

    public void setKeroWeight(String keroWeight) {
        this.keroWeight = keroWeight;
    }

    public void setCartIndex(int cartIndex) {
        this.cartIndex = cartIndex;
    }

    public int getCartProductPrice() {
        return cartProductPrice;
    }

    public void setCartProductPrice(int cartProductPrice) {
        this.cartProductPrice = cartProductPrice;
    }

    public int getCartForceInsurance() {
        return cartForceInsurance;
    }

    public void setCartForceInsurance(int cartForceInsurance) {
        this.cartForceInsurance = cartForceInsurance;
    }

    public String getKeroValue() {
        return keroValue;
    }

    public void setKeroValue(Product selectedCourier) {
        this.keroValue = selectedCourier.getPrice().toString()
                + "|" + keroWeight
                + "|" + selectedCourier.getUt()
                + "|" + selectedCourier.getCheckSum();
    }

    private String getStringIdrFormat(int value) {
        return ValueConverter.getStringIdrFormat(value);
    }

    public int getCartInsuranceProd() {
        return cartInsuranceProd;
    }

    public void setCartInsuranceProd(int cartInsuranceProd) {
        this.cartInsuranceProd = cartInsuranceProd;
    }

    public List<Integer> getCartProductInsurances() {
        return cartProductInsurances;
    }

    public void setCartProductInsurances(List<Integer> cartProductInsurances) {
        this.cartProductInsurances = cartProductInsurances;
    }

    public int getInsuranceUsedType() {
        return insuranceUsedType;
    }

    public void setInsuranceUsedType(int insuranceUsedType) {
        this.insuranceUsedType = insuranceUsedType;
    }

    public String getInsuranceUsedInfo() {
        return insuranceUsedInfo;
    }

    public void setInsuranceUsedInfo(String insuranceUsedInfo) {
        this.insuranceUsedInfo = insuranceUsedInfo;
    }

}
