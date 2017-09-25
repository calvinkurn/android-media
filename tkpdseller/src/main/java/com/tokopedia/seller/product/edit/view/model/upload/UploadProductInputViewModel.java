package com.tokopedia.seller.product.edit.view.model.upload;

import android.text.TextUtils;

import com.tokopedia.seller.product.edit.constant.InvenageSwitchTypeDef;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;

import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantDataSubmit;

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
    @InvenageSwitchTypeDef
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
    private int productChangeWholesale;
    private int productChangeCatalog;
    private int switchVariant;
    private ProductVariantDataSubmit productVariantDataSubmit;
    private String variantStringSelection;

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
        if (productPhotos == null) {
            this.productPhotos = new ProductPhotoListViewModel();
        } else {
            this.productPhotos = productPhotos;
        }
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

    public int getSwitchVariant() {
        return switchVariant;
    }

    public ProductVariantDataSubmit getProductVariantDataSubmit() {
        return productVariantDataSubmit;
    }

    public String getVariantStringSelection() {
        return variantStringSelection;
    }

    public void setVariantStringSelection(String variantStringSelection) {
        this.variantStringSelection = variantStringSelection;
    }

    public void setProductVariantData(ProductVariantDataSubmit productVariantDataSubmit) {
        this.productVariantDataSubmit = productVariantDataSubmit;
        if (productVariantDataSubmit== null
                || productVariantDataSubmit.getProductVariantUnitSubmitList()== null
                || productVariantDataSubmit.getProductVariantUnitSubmitList().size() == 0) {
            switchVariant = ProductVariantConstant.SWITCH_VARIANT_NOT_EXIST;
        } else {
            switchVariant = ProductVariantConstant.SWITCH_VARIANT_EXIST;
        }
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

    @InvenageSwitchTypeDef
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

    public int getProductChangeWholesale() {
        return productChangeWholesale;
    }

    public void setProductChangeWholesale(int productChangeWholesale) {
        this.productChangeWholesale = productChangeWholesale;
    }

    public void setProductChangeCatalog(int productChangeCatalog) {
        this.productChangeCatalog = productChangeCatalog;
    }

    public int getProductChangeCatalog() {
        return productChangeCatalog;
    }

    /**
     * check if the modelview has the same value as default
     */
    public boolean equalsDefault(UploadProductInputViewModel defaultModel) {
        List<ImageProductInputViewModel> imageSelectModelList = productPhotos.getPhotos();
        if (imageSelectModelList!= null && imageSelectModelList.size() > 0) {
            return false;
        }
        if (!TextUtils.isEmpty(productName)){
            return false;
        }
        if (productVideos!= null && productVideos.size() > 0) {
            return false;
        }
        if (productWholesaleList!= null && productWholesaleList.size() > 0) {
            return false;
        }
        if (!TextUtils.isEmpty(productDescription)){
            return false;
        }
        if (productPrice > 0) {
            return false;
        }
        if (productCatalogId > 0) {
            return false;
        }
        if (productDepartmentId > 0) {
            return false;
        }
        if (productCondition!= defaultModel.getProductCondition()) {
            return false;
        }
        if (productEtalaseId > 0) {
            return false;
        }
        if (productMinOrder!= defaultModel.getProductMinOrder()) {
            return false;
        }
        if (productMustInsurance!= defaultModel.getProductMustInsurance()) {
            return false;
        }
        if (productReturnable!= defaultModel.getProductReturnable()) {
            return false;
        }
        if (productInvenageValue!= defaultModel.getProductInvenageValue()) {
            return false;
        }
        if (productWeight!= defaultModel.getProductWeight()) {
            return false;
        }
        if (poProcessType!= defaultModel.getPoProcessType()) {
            return false;
        }
        if (poProcessValue!= defaultModel.getPoProcessValue()) {
            return false;
        }
        if (productVariantDataSubmit != null && productVariantDataSubmit.getProductVariantUnitSubmitList()!= null &&
                productVariantDataSubmit.getProductVariantUnitSubmitList().size() > 0){
            return false;
        }
        return true;
    }

}
