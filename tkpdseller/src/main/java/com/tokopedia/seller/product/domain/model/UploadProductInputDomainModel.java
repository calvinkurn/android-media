package com.tokopedia.seller.product.domain.model;

import java.util.List;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class UploadProductInputDomainModel {

    public static final String DELIMITER = "~";
    private List<ImageProductInputDomainModel> images;
    private List<ProductWholesaleDomainModel> wholesaleList;
    private String clickName;
    private String duplicate;
    private String productCatalogId;
    private String productCondition;
    private String productDepartmentId;
    private String productDescription;
    private String productEtalaseId;
    private String productEtalaseName;
    private String productMinOrder;
    private String productMustInsurance;
    private String productName;
    private String productPhotoDefault;
    private String productPrice;
    private String productPriceCurrency;
    private String productReturnable;
    private String productUploadTo;
    private String productWeight;
    private String productWeightUnit;
    private String poProcessType;
    private String poProcessValue;
    private String serverId;

    public List<ImageProductInputDomainModel> getImages() {
        return images;
    }

    public void setImages(List<ImageProductInputDomainModel> images) {
        this.images = images;
    }

    public String getClickName() {
        return clickName;
    }

    public void setClickName(String clickName) {
        this.clickName = clickName;
    }

    public String getDuplicate() {
        return duplicate;
    }

    public void setDuplicate(String duplicate) {
        this.duplicate = duplicate;
    }

    public List<ProductWholesaleDomainModel> getProductPriceOne() {
        return wholesaleList;
    }

    public List<ProductWholesaleDomainModel> getWholesaleList() {
        return wholesaleList;
    }

    public void setWholesaleList(List<ProductWholesaleDomainModel> wholesaleList) {
        this.wholesaleList = wholesaleList;
    }

    public String getProductCatalogId() {
        return productCatalogId;
    }

    public void setProductCatalogId(String productCatalogId) {
        this.productCatalogId = productCatalogId;
    }

    public String getProductCondition() {
        return productCondition;
    }

    public void setProductCondition(String productCondition) {
        this.productCondition = productCondition;
    }

    public String getProductDepartmentId() {
        return productDepartmentId;
    }

    public void setProductDepartmentId(String productDepartmentId) {
        this.productDepartmentId = productDepartmentId;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductEtalaseId() {
        return productEtalaseId;
    }

    public void setProductEtalaseId(String productEtalaseId) {
        this.productEtalaseId = productEtalaseId;
    }

    public String getProductEtalaseName() {
        return productEtalaseName;
    }

    public void setProductEtalaseName(String productEtalaseName) {
        this.productEtalaseName = productEtalaseName;
    }

    public String getProductMinOrder() {
        return productMinOrder;
    }

    public void setProductMinOrder(String productMinOrder) {
        this.productMinOrder = productMinOrder;
    }

    public String getProductMustInsurance() {
        return productMustInsurance;
    }

    public void setProductMustInsurance(String productMustInsurance) {
        this.productMustInsurance = productMustInsurance;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPhotoDefault() {
        return productPhotoDefault;
    }

    public void setProductPhotoDefault(String productPhotoDefault) {
        this.productPhotoDefault = productPhotoDefault;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductPriceCurrency() {
        return productPriceCurrency;
    }

    public void setProductPriceCurrency(String productPriceCurrency) {
        this.productPriceCurrency = productPriceCurrency;
    }

    public String getProductReturnable() {
        return productReturnable;
    }

    public void setProductReturnable(String productReturnable) {
        this.productReturnable = productReturnable;
    }

    public String getProductUploadTo() {
        return productUploadTo;
    }

    public void setProductUploadTo(String productUploadTo) {
        this.productUploadTo = productUploadTo;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getProductWeightUnit() {
        return productWeightUnit;
    }

    public void setProductWeightUnit(String productWeightUnit) {
        this.productWeightUnit = productWeightUnit;
    }

    public String getPoProcessType() {
        return poProcessType;
    }

    public void setPoProcessType(String poProcessType) {
        this.poProcessType = poProcessType;
    }

    public String getPoProcessValue() {
        return poProcessValue;
    }

    public void setPoProcessValue(String poProcessValue) {
        this.poProcessValue = poProcessValue;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getProductPhoto() {
        String productPhoto = "";
        for (int i = 0; i < images.size(); i++){
            productPhoto += images.get(i).getUrl();
            if (i + 1 < images.size()){
                productPhoto += DELIMITER;
            }
        }
        return productPhoto;
    }

    public String getProductPhotoDescription() {
        String productPhotoDescription = "";
        for (int i = 0; i < images.size(); i++){
            productPhotoDescription += images.get(i).getDescription();
            if (i + 1 < images.size()){
                productPhotoDescription += DELIMITER;
            }
        }
        return productPhotoDescription;
    }
}
