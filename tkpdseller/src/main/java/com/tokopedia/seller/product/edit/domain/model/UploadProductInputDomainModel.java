package com.tokopedia.seller.product.edit.domain.model;

import com.tokopedia.seller.product.edit.constant.InvenageSwitchTypeDef;
import com.tokopedia.seller.product.edit.constant.UploadToTypeDef;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantByPrdModel;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantSubmit;

import java.util.List;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class UploadProductInputDomainModel {

    private long id;
    private ProductPhotoListDomainModel productPhotos;
    private List<ProductWholesaleDomainModel> productWholesaleList;
    private List<String> productVideos;
    private String productName;
    private String productDescription;
    private double productPrice;
    private int productChangePhoto;
    private long productCatalogId;
    private String productCatalogName;
    private long productDepartmentId;
    private int productCondition;
    private long productEtalaseId;
    private String productEtalaseName;
    private int productMinOrder;
    private int productMustInsurance;
    private int productPriceCurrency;
    private int productReturnable;
    @UploadToTypeDef
    private int productUploadTo;
    @InvenageSwitchTypeDef
    private int productInvenageSwitch;
    private int productInvenageValue;
    private int productWeight;
    private int productWeightUnit;
    private int poProcessType;
    private int poProcessValue;
    private int serverId;
    private String hostUrl;
    private String productId;
    private int nameEditable;
    @ProductStatus
    private int productStatus;
    private String postKey;
    private int productChangeCatalog;
    private int productChangeWholesale;
    private int switchVariant;
    private ProductVariantSubmit productVariantSubmit;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProductPhotoListDomainModel getProductPhotos() {
        return productPhotos;
    }

    public void setProductPhotos(ProductPhotoListDomainModel productPhotos) {
        this.productPhotos = productPhotos;
    }

    public List<ProductWholesaleDomainModel> getProductPriceOne() {
        return productWholesaleList;
    }

    public List<ProductWholesaleDomainModel> getProductWholesaleList() {
        return productWholesaleList;
    }

    public void setProductWholesaleList(List<ProductWholesaleDomainModel> productWholesaleList) {
        this.productWholesaleList = productWholesaleList;
    }

    public long getProductCatalogId() {
        return productCatalogId;
    }

    public void setProductCatalogId(long productCatalogId) {
        this.productCatalogId = productCatalogId;
    }

    public String getProductCatalogName() {
        return productCatalogName;
    }

    public void setProductCatalogName(String productCatalogName) {
        this.productCatalogName = productCatalogName;
    }

    public int getProductCondition() {
        return productCondition;
    }

    public void setProductCondition(int productCondition) {
        this.productCondition = productCondition;
    }

    public long getProductDepartmentId() {
        return productDepartmentId;
    }

    public void setProductDepartmentId(long productDepartmentId) {
        this.productDepartmentId = productDepartmentId;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public long getProductEtalaseId() {
        return productEtalaseId;
    }

    public void setProductEtalaseId(long productEtalaseId) {
        this.productEtalaseId = productEtalaseId;
    }

    public String getProductEtalaseName() {
        return productEtalaseName;
    }

    public void setProductEtalaseName(String productEtalaseName) {
        this.productEtalaseName = productEtalaseName;
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

    public void setProductUploadTo(@UploadToTypeDef int productUploadTo) {
        this.productUploadTo = productUploadTo;
    }

    public int getProductInvenageSwitch() {
        return productInvenageSwitch;
    }

    public void setProductInvenageSwitch(int productInvenageSwitch) {
        this.productInvenageSwitch = productInvenageSwitch;
    }

    public int getProductInvenageValue() {
        return productInvenageValue;
    }

    public void setProductInvenageValue(int productInvenageValue) {
        this.productInvenageValue = productInvenageValue;
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

    public int getProductChangePhoto() {
        return productChangePhoto;
    }

    public void setProductChangePhoto(int productChangePhoto) {
        this.productChangePhoto = productChangePhoto;
    }

    public List<String> getProductVideos() {
        return productVideos;
    }

    public void setProductVideos(List<String> productVideos) {
        this.productVideos = productVideos;
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setNameEditable(int nameEditable) {
        this.nameEditable = nameEditable;
    }

    public void setProductStatus(@ProductStatus int productStatus) {
        this.productStatus = productStatus;
    }

    @ProductStatus
    public int getProductStatus() {
        return productStatus;
    }

    public String getProductId() {
        return productId;
    }

    public int getNameEditable() {
        return nameEditable;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getPostKey() {
        return postKey;
    }

    public int getProductChangeCatalog() {
        return productChangeCatalog;
    }

    public int getProductChangeWholesale() {
        return productChangeWholesale;
    }

    public void setProductChangeCatalog(int productChangeCatalog) {
        this.productChangeCatalog = productChangeCatalog;
    }

    public void setProductChangeWholesale(int productChangeWholesale) {
        this.productChangeWholesale = productChangeWholesale;
    }

    public int getSwitchVariant() {
        return switchVariant;
    }

    public ProductVariantSubmit getProductVariantSubmit() {
        return productVariantSubmit;
    }

    public void setProductVariantSubmit(ProductVariantSubmit productVariantSubmit) {
        this.productVariantSubmit = productVariantSubmit;
    }

    public void setSwitchVariant(int switchVariant) {
        this.switchVariant = switchVariant;
    }
}
