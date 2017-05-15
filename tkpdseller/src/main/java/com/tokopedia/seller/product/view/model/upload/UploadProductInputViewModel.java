package com.tokopedia.seller.product.view.model.upload;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.product.constant.SwitchTypeDef;
import com.tokopedia.seller.product.view.model.upload.intdef.ProductStatus;

import com.tokopedia.seller.product.constant.CurrencyTypeDef;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class UploadProductInputViewModel {
    private ProductPhotoListViewModel productPhotos;
    private List<ProductWholesaleViewModel> productWholesaleList;
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
    @CurrencyTypeDef
    private int productPriceCurrency;
    private int productReturnable;
    private int productUploadTo;
    @SwitchTypeDef
    private int productInvenageSwitch;
    private int productInvenageValue;
    private int productWeight;
    private int productWeightUnit;
    private int poProcessType;
    private int poProcessValue;
    private int serverId;
    @ProductStatus
    private int productStatus;
    private String productId;
    private int productNameEditable;

    public UploadProductInputViewModel() {
        productPhotos = new ProductPhotoListViewModel();
        productWholesaleList = new ArrayList<>();
        productVideos = new ArrayList<>();
        productName = "";
        productDescription = "";
    }

    public ProductPhotoListViewModel getProductPhotos() {
        return productPhotos;
    }

    public void setProductPhotos(ProductPhotoListViewModel productPhotos) {
        this.productPhotos = productPhotos;
    }

    public List<ProductWholesaleViewModel> getProductWholesaleList() {
        return productWholesaleList;
    }

    public void setProductWholesaleList(List<ProductWholesaleViewModel> productWholesaleList) {
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
        if ("0".equals(productDescription)) {
            return "";
        }
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getProductChangePhoto() {
        return productChangePhoto;
    }

    public void setProductChangePhoto(int productChangePhoto) {
        this.productChangePhoto = productChangePhoto;
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

    public long getProductDepartmentId() {
        return productDepartmentId;
    }

    public void setProductDepartmentId(long productDepartmentId) {
        this.productDepartmentId = productDepartmentId;
    }

    public int getProductCondition() {
        return productCondition;
    }

    public void setProductCondition(int productCondition) {
        this.productCondition = productCondition;
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

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    @CurrencyTypeDef
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

    @SwitchTypeDef
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

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    @ProductStatus
    public int getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(@ProductStatus int productStatus) {
        this.productStatus = productStatus;
    }

    public void setProductNameEditable(int productNameEditable) {
        this.productNameEditable = productNameEditable;
    }

    public int getProductNameEditable() {
        return productNameEditable;
    }

}
