
package com.tokopedia.seller.product.edit.view.model.edit;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.base.view.adapter.ItemType;

public class ProductViewModel implements ItemType {

    public static final int TYPE = 382;
    @SerializedName("product_id")
    @Expose
    private long productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_price")
    @Expose
    private double productPrice;
    @SerializedName("product_price_currency")
    @Expose
    private long productPriceCurrency;
    @SerializedName("product_status")
    @Expose
    private int productStatus;
    @SerializedName("product_min_order")
    @Expose
    private long productMinOrder;
    @SerializedName("product_weight")
    @Expose
    private long productWeight;
    @SerializedName("product_weight_unit")
    @Expose
    private long productWeightUnit;
    @SerializedName("product_condition")
    @Expose
    private long productCondition;
    @SerializedName("product_etalase")
    @Expose
    private ProductEtalaseViewModel productEtalase;
    @SerializedName("product_primary_picture")
    @Expose
    private ProductPictureViewModel productPrimaryPicture;
    @SerializedName("product_picture")
    @Expose
    private List<ProductPictureViewModel> productPicture = new ArrayList<>();
    @SerializedName("product_shop")
    @Expose
    private ProductShopViewModel productShop;
    @SerializedName("product_alias")
    @Expose
    private String productAlias;
    @SerializedName("product_url")
    @Expose
    private String productUrl;
    @SerializedName("product_last_update_price")
    @Expose
    private String productLastUpdatePrice;
    @SerializedName("product_position")
    @Expose
    private long productPosition;
    @SerializedName("product_description")
    @Expose
    private String productDescription;
    @SerializedName("product_must_insurance")
    @Expose
    private boolean productMustInsurance;
    @SerializedName("product_free_return")
    @Expose
    private boolean productFreeReturn;
    @SerializedName("product_stock")
    @Expose
    private long productStock;
    @SerializedName("product_sku")
    @Expose
    private String productSku;
    @SerializedName("product_max_order")
    @Expose
    private long productMaxOrder;
    @SerializedName("product_category")
    @Expose
    private ProductCategoryViewModel productCategory;
    @SerializedName("product_catalog")
    @Expose
    private ProductCatalogViewModel productCatalog;
    @SerializedName("product_wholesale")
    @Expose
    private List<ProductWholesaleViewModel> productWholesale = new ArrayList<>();
    @SerializedName("product_preorder")
    @Expose
    private ProductPreorderViewModel productPreorder;
    @SerializedName("product_brand")
    @Expose
    private ProductBrandViewModel productBrand;
    @SerializedName("product_video")
    @Expose
    private List<ProductVideoViewModel> productVideo = new ArrayList<>();
    @SerializedName("product_variant")
    @Expose
    private ProductVariantViewModel productVariant;

    @SerializedName("product_gtin")
    @Expose
    private String productGtin;

    @SerializedName("product_name_editable")
    @Expose
    private boolean productNameEditable;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public long getProductPriceCurrency() {
        return productPriceCurrency;
    }

    public void setProductPriceCurrency(long productPriceCurrency) {
        this.productPriceCurrency = productPriceCurrency;
    }

    public int getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(int productStatus) {
        this.productStatus = productStatus;
    }

    public long getProductMinOrder() {
        return productMinOrder;
    }

    public void setProductMinOrder(long productMinOrder) {
        this.productMinOrder = productMinOrder;
    }

    public long getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(long productWeight) {
        this.productWeight = productWeight;
    }

    public long getProductWeightUnit() {
        return productWeightUnit;
    }

    public void setProductWeightUnit(long productWeightUnit) {
        this.productWeightUnit = productWeightUnit;
    }

    public long getProductCondition() {
        return productCondition;
    }

    public void setProductCondition(long productCondition) {
        this.productCondition = productCondition;
    }

    public ProductEtalaseViewModel getProductEtalase() {
        return productEtalase;
    }

    public void setProductEtalase(ProductEtalaseViewModel productEtalase) {
        this.productEtalase = productEtalase;
    }

    public List<ProductPictureViewModel> getProductPicture() {
        return productPicture;
    }

    public void setProductPicture(List<ProductPictureViewModel> productPicture) {
        this.productPicture = productPicture;
    }

    public ProductShopViewModel getProductShop() {
        return productShop;
    }

    public void setProductShop(ProductShopViewModel productShop) {
        this.productShop = productShop;
    }

    public String getProductAlias() {
        return productAlias;
    }

    public void setProductAlias(String productAlias) {
        this.productAlias = productAlias;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductLastUpdatePrice() {
        return productLastUpdatePrice;
    }

    public void setProductLastUpdatePrice(String productLastUpdatePrice) {
        this.productLastUpdatePrice = productLastUpdatePrice;
    }

    public ProductPictureViewModel getProductPrimaryPicture() {
        return productPrimaryPicture;
    }

    public void setProductPrimaryPicture(ProductPictureViewModel productPrimaryPicture) {
        this.productPrimaryPicture = productPrimaryPicture;
    }

    public long getProductPosition() {
        return productPosition;
    }

    public void setProductPosition(long productPosition) {
        this.productPosition = productPosition;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public boolean isProductMustInsurance() {
        return productMustInsurance;
    }

    public void setProductMustInsurance(boolean productMustInsurance) {
        this.productMustInsurance = productMustInsurance;
    }

    public boolean isProductFreeReturn() {
        return productFreeReturn;
    }

    public void setProductFreeReturn(boolean productFreeReturn) {
        this.productFreeReturn = productFreeReturn;
    }

    public long getProductStock() {
        return productStock;
    }

    public void setProductStock(long productStock) {
        this.productStock = productStock;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public long getProductMaxOrder() {
        return productMaxOrder;
    }

    public void setProductMaxOrder(long productMaxOrder) {
        this.productMaxOrder = productMaxOrder;
    }

    public ProductCategoryViewModel getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategoryViewModel productCategory) {
        this.productCategory = productCategory;
    }

    public ProductCatalogViewModel getProductCatalog() {
        return productCatalog;
    }

    public void setProductCatalog(ProductCatalogViewModel productCatalog) {
        this.productCatalog = productCatalog;
    }

    public List<ProductWholesaleViewModel> getProductWholesale() {
        return productWholesale;
    }

    public void setProductWholesale(List<ProductWholesaleViewModel> productWholesale) {
        this.productWholesale = productWholesale;
    }

    public ProductPreorderViewModel getProductPreorder() {
        return productPreorder;
    }

    public void setProductPreorder(ProductPreorderViewModel productPreorder) {
        this.productPreorder = productPreorder;
    }

    public ProductBrandViewModel getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(ProductBrandViewModel productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductGtin() {
        return productGtin;
    }

    public void setProductGtin(String productGtin) {
        this.productGtin = productGtin;
    }

    public List<ProductVideoViewModel> getProductVideo() {
        return productVideo;
    }

    public void setProductVideo(List<ProductVideoViewModel> productVideo) {
        this.productVideo = productVideo;
    }

    public ProductVariantViewModel getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariantViewModel productVariant) {
        this.productVariant = productVariant;
    }

    public void setProductNameEditable(boolean productNameEditable) {
        this.productNameEditable = productNameEditable;
    }

    public boolean isProductNameEditable() {
        return productNameEditable;
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
