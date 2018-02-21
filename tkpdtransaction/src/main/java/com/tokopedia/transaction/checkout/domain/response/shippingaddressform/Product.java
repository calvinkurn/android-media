package com.tokopedia.transaction.checkout.domain.response.shippingaddressform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class Product {

    @SerializedName("errors")
    @Expose
    private Object errors;
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_price_fmt")
    @Expose
    private String productPriceFmt;
    @SerializedName("product_price")
    @Expose
    private int productPrice;
    @SerializedName("product_wholesale_price")
    @Expose
    private int productWholesalePrice;
    @SerializedName("product_wholesale_price_fmt")
    @Expose
    private String productWholesalePriceFmt;
    @SerializedName("product_weight_fmt")
    @Expose
    private String productWeightFmt;
    @SerializedName("product_weight")
    @Expose
    private int productWeight;
    @SerializedName("product_condition")
    @Expose
    private int productCondition;
    @SerializedName("product_url")
    @Expose
    private String productUrl;
    @SerializedName("product_returnable")
    @Expose
    private int productReturnable;
    @SerializedName("product_is_free_returns")
    @Expose
    private int productIsFreeReturns;
    @SerializedName("product_is_preorder")
    @Expose
    private int productIsPreorder;
    @SerializedName("product_cashback")
    @Expose
    private String productCashback;
    @SerializedName("product_min_order")
    @Expose
    private int productMinOrder;
    @SerializedName("product_invenage_value")
    @Expose
    private int productInvenageValue;
    @SerializedName("product_switch_invenage")
    @Expose
    private int productSwitchInvenage;
    @SerializedName("product_price_currency")
    @Expose
    private int productPriceCurrency;
    @SerializedName("product_image_src_200_square")
    @Expose
    private String productImageSrc200Square;
    @SerializedName("product_notes")
    @Expose
    private String productNotes;
    @SerializedName("product_quantity")
    @Expose
    private int productQuantity;
    @SerializedName("product_menu_id")
    @Expose
    private int productMenuId;
    @SerializedName("product_finsurance")
    @Expose
    private int productFinsurance;
    @SerializedName("product_fcancel_partial")
    @Expose
    private int productFcancelPartial;
    @SerializedName("product_shipment")
    @Expose
    private List<ProductShipment> productShipment = new ArrayList<>();
    @SerializedName("product_shipment_mapping")
    @Expose
    private List<ProductShipmentMapping> productShipmentMapping = new ArrayList<>();
    @SerializedName("product_cat_id")
    @Expose
    private int productCatId;
    @SerializedName("product_catalog_id")
    @Expose
    private int productCatalogId;

    public Object getErrors() {
        return errors;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPriceFmt() {
        return productPriceFmt;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getProductWholesalePrice() {
        return productWholesalePrice;
    }

    public String getProductWholesalePriceFmt() {
        return productWholesalePriceFmt;
    }

    public String getProductWeightFmt() {
        return productWeightFmt;
    }

    public int getProductWeight() {
        return productWeight;
    }

    public int getProductCondition() {
        return productCondition;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public int getProductReturnable() {
        return productReturnable;
    }

    public int getProductIsFreeReturns() {
        return productIsFreeReturns;
    }

    public int getProductIsPreorder() {
        return productIsPreorder;
    }

    public String getProductCashback() {
        return productCashback;
    }

    public int getProductMinOrder() {
        return productMinOrder;
    }

    public int getProductInvenageValue() {
        return productInvenageValue;
    }

    public int getProductSwitchInvenage() {
        return productSwitchInvenage;
    }

    public int getProductPriceCurrency() {
        return productPriceCurrency;
    }

    public String getProductImageSrc200Square() {
        return productImageSrc200Square;
    }

    public String getProductNotes() {
        return productNotes;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public int getProductMenuId() {
        return productMenuId;
    }

    public int getProductFinsurance() {
        return productFinsurance;
    }

    public int getProductFcancelPartial() {
        return productFcancelPartial;
    }

    public List<ProductShipment> getProductShipment() {
        return productShipment;
    }

    public List<ProductShipmentMapping> getProductShipmentMapping() {
        return productShipmentMapping;
    }

    public int getProductCatId() {
        return productCatId;
    }

    public int getProductCatalogId() {
        return productCatalogId;
    }
}
