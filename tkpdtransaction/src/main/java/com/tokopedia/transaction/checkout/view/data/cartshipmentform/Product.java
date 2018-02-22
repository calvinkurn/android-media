package com.tokopedia.transaction.checkout.view.data.cartshipmentform;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class Product {

    private List<String> errors = new ArrayList<>();
    private int productId;
    private String productName;
    private String productPriceFmt;
    private int productPrice;
    private int productWholesalePrice;
    private String productWholesalePriceFmt;
    private String productWeightFmt;
    private int productWeight;
    private int productCondition;
    private String productUrl;
    private boolean productReturnable;
    private boolean productIsFreeReturns;
    private boolean productIsPreorder;
    private String productCashback;
    private int productMinOrder;
    private int productInvenageValue;
    private int productSwitchInvenage;
    private int productPriceCurrency;
    private String productImageSrc200Square;
    private String productNotes;
    private int productQuantity;
    private int productMenuId;
    private boolean productFinsurance;
    private boolean productFcancelPartial;
    private List<ProductShipment> productShipment = new ArrayList<>();
    private List<ProductShipmentMapping> productShipmentMapping = new ArrayList<>();
    private int productCatId;
    private int productCatalogId;


    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPriceFmt() {
        return productPriceFmt;
    }

    public void setProductPriceFmt(String productPriceFmt) {
        this.productPriceFmt = productPriceFmt;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductWholesalePrice() {
        return productWholesalePrice;
    }

    public void setProductWholesalePrice(int productWholesalePrice) {
        this.productWholesalePrice = productWholesalePrice;
    }

    public String getProductWholesalePriceFmt() {
        return productWholesalePriceFmt;
    }

    public void setProductWholesalePriceFmt(String productWholesalePriceFmt) {
        this.productWholesalePriceFmt = productWholesalePriceFmt;
    }

    public String getProductWeightFmt() {
        return productWeightFmt;
    }

    public void setProductWeightFmt(String productWeightFmt) {
        this.productWeightFmt = productWeightFmt;
    }

    public int getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(int productWeight) {
        this.productWeight = productWeight;
    }

    public int getProductCondition() {
        return productCondition;
    }

    public void setProductCondition(int productCondition) {
        this.productCondition = productCondition;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public boolean isProductReturnable() {
        return productReturnable;
    }

    public void setProductReturnable(boolean productReturnable) {
        this.productReturnable = productReturnable;
    }

    public boolean isProductIsFreeReturns() {
        return productIsFreeReturns;
    }

    public void setProductIsFreeReturns(boolean productIsFreeReturns) {
        this.productIsFreeReturns = productIsFreeReturns;
    }

    public boolean isProductIsPreorder() {
        return productIsPreorder;
    }

    public void setProductIsPreorder(boolean productIsPreorder) {
        this.productIsPreorder = productIsPreorder;
    }

    public String getProductCashback() {
        return productCashback;
    }

    public void setProductCashback(String productCashback) {
        this.productCashback = productCashback;
    }

    public int getProductMinOrder() {
        return productMinOrder;
    }

    public void setProductMinOrder(int productMinOrder) {
        this.productMinOrder = productMinOrder;
    }

    public int getProductInvenageValue() {
        return productInvenageValue;
    }

    public void setProductInvenageValue(int productInvenageValue) {
        this.productInvenageValue = productInvenageValue;
    }

    public int getProductSwitchInvenage() {
        return productSwitchInvenage;
    }

    public void setProductSwitchInvenage(int productSwitchInvenage) {
        this.productSwitchInvenage = productSwitchInvenage;
    }

    public int getProductPriceCurrency() {
        return productPriceCurrency;
    }

    public void setProductPriceCurrency(int productPriceCurrency) {
        this.productPriceCurrency = productPriceCurrency;
    }

    public String getProductImageSrc200Square() {
        return productImageSrc200Square;
    }

    public void setProductImageSrc200Square(String productImageSrc200Square) {
        this.productImageSrc200Square = productImageSrc200Square;
    }

    public String getProductNotes() {
        return productNotes;
    }

    public void setProductNotes(String productNotes) {
        this.productNotes = productNotes;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getProductMenuId() {
        return productMenuId;
    }

    public void setProductMenuId(int productMenuId) {
        this.productMenuId = productMenuId;
    }

    public boolean isProductFinsurance() {
        return productFinsurance;
    }

    public void setProductFinsurance(boolean productFinsurance) {
        this.productFinsurance = productFinsurance;
    }

    public boolean isProductFcancelPartial() {
        return productFcancelPartial;
    }

    public void setProductFcancelPartial(boolean productFcancelPartial) {
        this.productFcancelPartial = productFcancelPartial;
    }

    public List<ProductShipment> getProductShipment() {
        return productShipment;
    }

    public void setProductShipment(List<ProductShipment> productShipment) {
        this.productShipment = productShipment;
    }

    public List<ProductShipmentMapping> getProductShipmentMapping() {
        return productShipmentMapping;
    }

    public void setProductShipmentMapping(List<ProductShipmentMapping> productShipmentMapping) {
        this.productShipmentMapping = productShipmentMapping;
    }

    public int getProductCatId() {
        return productCatId;
    }

    public void setProductCatId(int productCatId) {
        this.productCatId = productCatId;
    }

    public int getProductCatalogId() {
        return productCatalogId;
    }

    public void setProductCatalogId(int productCatalogId) {
        this.productCatalogId = productCatalogId;
    }
}
