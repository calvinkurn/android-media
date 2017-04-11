package com.tokopedia.seller.product.data.source.cloud.model;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductValidationInputServiceModel {
    public static final String CLICK_NAME = "click_name";
    public static final String DUPLICATE = "duplicate";
    public static final String PRD_PRC_1 = "prd_prc_1";
    public static final String PRD_PRC_2 = "prd_prc_2";
    public static final String PRD_PRC_3 = "prd_prc_3";
    public static final String PRD_PRC_4 = "prd_prc_4";
    public static final String PRD_PRC_5 = "prd_prc_5";
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
    public static final String QTY_MAX_1 = "qty_max_1";
    public static final String QTY_MAX_2 = "qty_max_2";
    public static final String QTY_MAX_3 = "qty_max_3";
    public static final String QTY_MAX_4 = "qty_max_4";
    public static final String QTY_MAX_5 = "qty_max_5";
    public static final String QTY_MIN_1 = "qty_min_1";
    public static final String QTY_MIN_2 = "qty_min_2";
    public static final String QTY_MIN_3 = "qty_min_3";
    public static final String QTY_MIN_4 = "qty_min_4";
    public static final String QTY_MIN_5 = "qty_min_5";
    public static final String PO_PROCESS_TYPE = "po_process_type";
    public static final String PO_PROCESS_VALUE = "po_process_value";
    public static final String SERVER_ID = "server_id";
    public static final String USER_ID = "user_id";

    private String clickName;
    private String duplicate;
    private String productPriceOne;
    private String productPriceTwo;
    private String productPriceThree;
    private String productPriceFour;
    private String productPriceFive;
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
    private String quantityMaxOne;
    private String quantityMaxTwo;
    private String quantityMaxThree;
    private String quantityMaxFour;
    private String quantityMaxFive;
    private String quantityMinOne;
    private String quantityMinTwo;
    private String quantityMinThree;
    private String quantityMinFour;
    private String quantityMinFive;
    private String poProcessType;
    private String poProcessValue;
    private String serverId;

    public TKPDMapParam<String, String> generateMapParam() {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(CLICK_NAME, getClickName());
        params.put(DUPLICATE, getDuplicate());
        params.put(PRD_PRC_1, getProductPriceOne());
        params.put(PRD_PRC_2, getProductPriceTwo());
        params.put(PRD_PRC_3, getProductPriceThree());
        params.put(PRD_PRC_4, getProductPriceFour());
        params.put(PRD_PRC_5, getProductPriceFive());
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
        params.put(QTY_MAX_1, getQuantityMaxOne());
        params.put(QTY_MAX_2, getQuantityMaxTwo());
        params.put(QTY_MAX_3, getQuantityMaxThree());
        params.put(QTY_MAX_4, getQuantityMaxFour());
        params.put(QTY_MAX_5, getQuantityMaxFive());
        params.put(QTY_MIN_1, getQuantityMinOne());
        params.put(QTY_MIN_2, getQuantityMinTwo());
        params.put(QTY_MIN_3, getQuantityMinThree());
        params.put(QTY_MIN_4, getQuantityMinFour());
        params.put(QTY_MIN_5, getQuantityMinFive());
        params.put(PO_PROCESS_TYPE, getPoProcessType());
        params.put(PO_PROCESS_VALUE, getPoProcessValue());
        params.put(SERVER_ID, getServerId());
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

    public String getProductPriceOne() {
        return productPriceOne;
    }

    public void setProductPriceOne(String productPriceOne) {
        this.productPriceOne = productPriceOne;
    }

    public String getProductPriceTwo() {
        return productPriceTwo;
    }

    public void setProductPriceTwo(String productPriceTwo) {
        this.productPriceTwo = productPriceTwo;
    }

    public String getProductPriceThree() {
        return productPriceThree;
    }

    public void setProductPriceThree(String productPriceThree) {
        this.productPriceThree = productPriceThree;
    }

    public String getProductPriceFour() {
        return productPriceFour;
    }

    public void setProductPriceFour(String productPriceFour) {
        this.productPriceFour = productPriceFour;
    }

    public String getProductPriceFive() {
        return productPriceFive;
    }

    public void setProductPriceFive(String productPriceFive) {
        this.productPriceFive = productPriceFive;
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

    public String getQuantityMaxOne() {
        return quantityMaxOne;
    }

    public void setQuantityMaxOne(String quantityMaxOne) {
        this.quantityMaxOne = quantityMaxOne;
    }

    public String getQuantityMaxTwo() {
        return quantityMaxTwo;
    }

    public void setQuantityMaxTwo(String quantityMaxTwo) {
        this.quantityMaxTwo = quantityMaxTwo;
    }

    public String getQuantityMaxThree() {
        return quantityMaxThree;
    }

    public void setQuantityMaxThree(String quantityMaxThree) {
        this.quantityMaxThree = quantityMaxThree;
    }

    public String getQuantityMaxFour() {
        return quantityMaxFour;
    }

    public void setQuantityMaxFour(String quantityMaxFour) {
        this.quantityMaxFour = quantityMaxFour;
    }

    public String getQuantityMaxFive() {
        return quantityMaxFive;
    }

    public void setQuantityMaxFive(String quantityMaxFive) {
        this.quantityMaxFive = quantityMaxFive;
    }

    public String getQuantityMinOne() {
        return quantityMinOne;
    }

    public void setQuantityMinOne(String quantityMinOne) {
        this.quantityMinOne = quantityMinOne;
    }

    public String getQuantityMinTwo() {
        return quantityMinTwo;
    }

    public void setQuantityMinTwo(String quantityMinTwo) {
        this.quantityMinTwo = quantityMinTwo;
    }

    public String getQuantityMinThree() {
        return quantityMinThree;
    }

    public void setQuantityMinThree(String quantityMinThree) {
        this.quantityMinThree = quantityMinThree;
    }

    public String getQuantityMinFour() {
        return quantityMinFour;
    }

    public void setQuantityMinFour(String quantityMinFour) {
        this.quantityMinFour = quantityMinFour;
    }

    public String getQuantityMinFive() {
        return quantityMinFive;
    }

    public void setQuantityMinFive(String quantityMinFive) {
        this.quantityMinFive = quantityMinFive;
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
}
