package com.tokopedia.seller.product.data.source.db.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductDraftModel {

    @SerializedName("product_photos")
    private ProductPhotoListDraftModel productPhotos;

    @SerializedName("product_wholesale")
    private List<ProductWholesaleDraftModel> productWholesaleList;

    @SerializedName("product_videos")
    private List<String> productVideos;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("product_description")
    private String productDescription;

    @SerializedName("product_etalase_name")
    private String productEtalaseName;

    @SerializedName("product_price")
    private double productPrice;

    @SerializedName("product_change_photos")
    private int productChangePhoto;

    @SerializedName("product_catalog_id")
    private int productCatalogId;

    @SerializedName("product_department_id")
    private int productDepartmentId;

    @SerializedName("product_condition")
    private int productCondition;

    @SerializedName("product_etalase_id")
    private int productEtalaseId;

    @SerializedName("product_min_order")
    private int productMinOrder;

    @SerializedName("product_must_insurance")
    private int productMustInsurance;

    @SerializedName("product_price_currency")
    private int productPriceCurrency;

    @SerializedName("product_returnable")
    private int productReturnable;

    @SerializedName("product_upload_to")
    private int productUploadTo;

    @SerializedName("product_weight")
    private int productWeight;

    @SerializedName("product_weight_unit")
    private int productWeightUnit;

    @SerializedName("po_process_type")
    private int poProcessType;

    @SerializedName("po_process_value")
    private int poProcessValue;

    @SerializedName("server_id")
    private int serverId;

    public ProductPhotoListDraftModel getProductPhotos() {
        return productPhotos;
    }

    public void setProductPhotos(ProductPhotoListDraftModel productPhotos) {
        this.productPhotos = productPhotos;
    }

    public List<ProductWholesaleDraftModel> getProductWholesaleList() {
        return productWholesaleList;
    }

    public void setProductWholesaleList(List<ProductWholesaleDraftModel> productWholesaleList) {
        this.productWholesaleList = productWholesaleList;
    }

    public List<String> getProductVideos() {
        return productVideos;
    }

    public void setProductVideos(List<String> productVideos) {
        this.productVideos = productVideos;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductEtalaseName() {
        return productEtalaseName;
    }

    public void setProductEtalaseName(String productEtalaseName) {
        this.productEtalaseName = productEtalaseName;
    }

    public int getProductChangePhoto() {
        return productChangePhoto;
    }

    public void setProductChangePhoto(int productChangePhoto) {
        this.productChangePhoto = productChangePhoto;
    }

    public int getProductCatalogId() {
        return productCatalogId;
    }

    public void setProductCatalogId(int productCatalogId) {
        this.productCatalogId = productCatalogId;
    }

    public int getProductDepartmentId() {
        return productDepartmentId;
    }

    public void setProductDepartmentId(int productDepartmentId) {
        this.productDepartmentId = productDepartmentId;
    }

    public int getProductCondition() {
        return productCondition;
    }

    public void setProductCondition(int productCondition) {
        this.productCondition = productCondition;
    }

    public int getProductEtalaseId() {
        return productEtalaseId;
    }

    public void setProductEtalaseId(int productEtalaseId) {
        this.productEtalaseId = productEtalaseId;
    }

    public int getProductMinOrder() {
        return productMinOrder;
    }

    public void setProductMinOrder(int productMinOrder) {
        this.productMinOrder = productMinOrder;
    }

    public int getProductMustInsurance() {
        return productMustInsurance;
    }

    public void setProductMustInsurance(int productMustInsurance) {
        this.productMustInsurance = productMustInsurance;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductPriceCurrency() {
        return productPriceCurrency;
    }

    public void setProductPriceCurrency(int productPriceCurrency) {
        this.productPriceCurrency = productPriceCurrency;
    }

    public int getProductReturnable() {
        return productReturnable;
    }

    public void setProductReturnable(int productReturnable) {
        this.productReturnable = productReturnable;
    }

    public int getProductUploadTo() {
        return productUploadTo;
    }

    public void setProductUploadTo(int productUploadTo) {
        this.productUploadTo = productUploadTo;
    }

    public int getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(int productWeight) {
        this.productWeight = productWeight;
    }

    public int getProductWeightUnit() {
        return productWeightUnit;
    }

    public void setProductWeightUnit(int productWeightUnit) {
        this.productWeightUnit = productWeightUnit;
    }

    public int getPoProcessType() {
        return poProcessType;
    }

    public void setPoProcessType(int poProcessType) {
        this.poProcessType = poProcessType;
    }

    public int getPoProcessValue() {
        return poProcessValue;
    }

    public void setPoProcessValue(int poProcessValue) {
        this.poProcessValue = poProcessValue;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}
