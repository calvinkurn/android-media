package com.tokopedia.transaction.checkout.data.entity.response.cartlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class Product {

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
    @SerializedName("category_id")
    @Expose
    private int categoryId;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("catalog_id")
    @Expose
    private int catalogId;
    @SerializedName("wholesale_price")
    @Expose
    private Object wholesalePrice;
    @SerializedName("product_weight_fmt")
    @Expose
    private String productWeightFmt;
    @SerializedName("product_condition")
    @Expose
    private int productCondition;
    @SerializedName("product_status")
    @Expose
    private int productStatus;
    @SerializedName("product_url")
    @Expose
    private String productUrl;
    @SerializedName("product_returnable")
    @Expose
    private int productReturnable;
    @SerializedName("is_freereturns")
    @Expose
    private int isFreereturns;
    @SerializedName("is_preorder")
    @Expose
    private int isPreorder;
    @SerializedName("product_cashback")
    @Expose
    private String productCashback;
    @SerializedName("product_min_order")
    @Expose
    private int productMinOrder;
    @SerializedName("product_rating")
    @Expose
    private int productRating;
    @SerializedName("product_invenage_value")
    @Expose
    private int productInvenageValue;
    @SerializedName("product_switch_invenage")
    @Expose
    private int productSwitchInvenage;
    @SerializedName("product_price_currency")
    @Expose
    private int productPriceCurrency;
    @SerializedName("product_image")
    @Expose
    private ProductImage productImage;
    @SerializedName("product_all_images")
    @Expose
    private Object productAllImages;
    @SerializedName("product_notes")
    @Expose
    private String productNotes;
    @SerializedName("product_quantity")
    @Expose
    private int productQuantity;
    @SerializedName("product_weight")
    @Expose
    private int productWeight;
    @SerializedName("product_weight_unit_code")
    @Expose
    private int productWeightUnitCode;
    @SerializedName("product_weight_unit_text")
    @Expose
    private String productWeightUnitText;
    @SerializedName("last_update_price")
    @Expose
    private int lastUpdatePrice;
    @SerializedName("is_update_price")
    @Expose
    private boolean isUpdatePrice;
    @SerializedName("product_preorder")
    @Expose
    private ProductPreorder productPreorder;
    @SerializedName("product_showcase")
    @Expose
    private ProductShowCase productShowcase;

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

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategory() {
        return category;
    }

    public int getCatalogId() {
        return catalogId;
    }

    public Object getWholesalePrice() {
        return wholesalePrice;
    }

    public String getProductWeightFmt() {
        return productWeightFmt;
    }

    public int getProductCondition() {
        return productCondition;
    }

    public int getProductStatus() {
        return productStatus;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public int getProductReturnable() {
        return productReturnable;
    }

    public int getIsFreereturns() {
        return isFreereturns;
    }

    public int getIsPreorder() {
        return isPreorder;
    }

    public String getProductCashback() {
        return productCashback;
    }

    public int getProductMinOrder() {
        return productMinOrder;
    }

    public int getProductRating() {
        return productRating;
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

    public ProductImage getProductImage() {
        return productImage;
    }

    public Object getProductAllImages() {
        return productAllImages;
    }

    public String getProductNotes() {
        return productNotes;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public int getProductWeight() {
        return productWeight;
    }

    public int getProductWeightUnitCode() {
        return productWeightUnitCode;
    }

    public String getProductWeightUnitText() {
        return productWeightUnitText;
    }

    public int getLastUpdatePrice() {
        return lastUpdatePrice;
    }

    public boolean isUpdatePrice() {
        return isUpdatePrice;
    }

    public ProductPreorder getProductPreorder() {
        return productPreorder;
    }

    public ProductShowCase getProductShowcase() {
        return productShowcase;
    }
}
