package com.tokopedia.seller.product.data.source.cloud.model;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.List;
import java.util.Map;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductValidationInputServiceModel {
    public static final String CLICK_NAME = "click_name";
    public static final String DUPLICATE = "duplicate";
    public static final String PRODUCT_CATALOG_ID = "product_catalog_id";
    public static final String PRODUCT_CONDITION = "product_condition";
    public static final String PRODUCT_DEPARTMENT_ID = "product_department_id";
    public static final String PRODUCT_DESCRIPTION = "product_description";
    public static final String PRODUCT_ETALASE_ID = "product_etalase_id";
    public static final String PRODUCT_ETALASE_NAME = "product_etalase_name";
    public static final String PRODUCT_MIN_ORDER = "product_min_order";
    public static final String PRODUCT_MUST_INSURANCE = "product_must_insurance";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_PHOTO = "product_photo";
    public static final String PRODUCT_PHOTO_DEFAULT = "product_photo_default";
    public static final String PRODUCT_PHOTO_DESC = "product_photo_desc";
    public static final String PRODUCT_PRICE = "product_price";
    public static final String PRODUCT_PRICE_CURRENCY = "product_price_currency";
    public static final String PRODUCT_RETURNABLE = "product_returnable";
    public static final String PRODUCT_UPLOAD_TO = "product_upload_to";
    public static final String PRODUCT_WEIGHT = "product_weight";
    public static final String PRODUCT_WEIGHT_UNIT = "product_weight_unit";
    public static final String PO_PROCESS_TYPE = "po_process_type";
    public static final String PO_PROCESS_VALUE = "po_process_value";
    public static final String SERVER_ID = "server_id";
    public static final String PRD_PRC_ = "prd_prc_";
    public static final String QTY_MAX_ = "qty_max_";
    public static final String QTY_MIN_ = "qty_min_";

    private List<ProductWholesaleServiceModel> productWholesale;
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
    private String productPhoto;
    private String productPhotoDefault;
    private String productPhotoDescription;
    private String productPrice;
    private String productPriceCurrency;
    private String productReturnable;
    private String productUploadTo;
    private String productWeight;
    private String productWeightUnit;
    private String poProcessType;
    private String poProcessValue;
    private String serverId;

    public TKPDMapParam<String, String> generateMapParam() {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(CLICK_NAME, getClickName());
        params.put(DUPLICATE, getDuplicate());
        params.put(PRODUCT_CATALOG_ID, getProductCatalogId());
        params.put(PRODUCT_CONDITION, getProductCondition());
        params.put(PRODUCT_DEPARTMENT_ID, getProductDepartmentId());
        params.put(PRODUCT_DESCRIPTION, getProductDescription());
        params.put(PRODUCT_ETALASE_ID, getProductEtalaseId());
        params.put(PRODUCT_ETALASE_NAME, getProductEtalaseName());
        params.put(PRODUCT_MIN_ORDER, getProductMinOrder());
        params.put(PRODUCT_MUST_INSURANCE, getProductMustInsurance());
        params.put(PRODUCT_NAME, getProductName());
        params.put(PRODUCT_PHOTO, getProductPhoto());
        params.put(PRODUCT_PHOTO_DEFAULT, getProductPhotoDefault());
        params.put(PRODUCT_PHOTO_DESC, getProductPhotoDescription());
        params.put(PRODUCT_PRICE, getProductPrice());
        params.put(PRODUCT_PRICE_CURRENCY, getProductPriceCurrency());
        params.put(PRODUCT_RETURNABLE, getProductReturnable());
        params.put(PRODUCT_UPLOAD_TO, getProductUploadTo());
        params.put(PRODUCT_WEIGHT, getProductWeight());
        params.put(PRODUCT_WEIGHT_UNIT, getProductWeightUnit());
        params.put(PO_PROCESS_TYPE, getPoProcessType());
        params.put(PO_PROCESS_VALUE, getPoProcessValue());
        params.put(SERVER_ID, getServerId());
        params.putAll(getWholesaleParams());
        return params;
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

    public String getProductPhoto() {
        return productPhoto;
    }

    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }

    public String getProductPhotoDefault() {
        return productPhotoDefault;
    }

    public void setProductPhotoDefault(String productPhotoDefault) {
        this.productPhotoDefault = productPhotoDefault;
    }

    public String getProductPhotoDescription() {
        return productPhotoDescription;
    }

    public void setProductPhotoDescription(String productPhotoDescription) {
        this.productPhotoDescription = productPhotoDescription;
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

    public void setProductWholesale(List<ProductWholesaleServiceModel> productWholesale) {
        this.productWholesale = productWholesale;
    }

    public TKPDMapParam<String, String> getWholesaleParams() {
        TKPDMapParam<String, String> wholesaleParams = new TKPDMapParam<>();
        for (int i = 0; i < productWholesale.size(); i++){
            wholesaleParams.put(PRD_PRC_ + i, productWholesale.get(i).getPrice());
            wholesaleParams.put(QTY_MAX_ + i, productWholesale.get(i).getQtyMax());
            wholesaleParams.put(QTY_MIN_ + i, productWholesale.get(i).getQtyMin());
        }
        return wholesaleParams;
    }
}
