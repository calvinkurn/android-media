package com.tokopedia.seller.product.data.source.cloud.model;

import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.List;
import java.util.Map;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductValidationInputServiceModel {
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
    public static final String PRODUCT_VIDEO_SIZE = "product_video_size";
    public static final String PRODUCT_VIDEO_ = "product_video_";
    public static final String SERVER_ID = "server_id";
    public static final String PRD_PRC_ = "prd_prc_";
    public static final String QTY_MAX_ = "qty_max_";
    public static final String QTY_MIN_ = "qty_min_";
    public static final String DELIMITER = "~";

    private ProductPhotoListServiceModel productPhotos;
    private List<ProductWholesaleServiceModel> productWholesale;
    private List<String> productVideo;
    private String productDescription;
    private String productEtalaseName;
    private String productName;
    private int productCatalogId;
    private int productCondition;
    private int productDepartmentId;
    private int productEtalaseId;
    private int productMinOrder;
    private int productMustInsurance;
    private int productPrice;
    private int productPriceCurrency;
    private int productReturnable;
    private int productUploadTo;
    private int productWeight;
    private int productWeightUnit;
    private int poProcessType;
    private int poProcessValue;
    private int serverId;

    public TKPDMapParam<String, String> generateMapParam() {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(PRODUCT_NAME, getProductName());
        params.put(PRODUCT_DEPARTMENT_ID, String.valueOf(getProductDepartmentId()));
        params.put(PRODUCT_CATALOG_ID, String.valueOf(getProductCatalogId()));
        params.put(PRODUCT_PRICE, String.valueOf(getProductPrice()));
        params.put(PRODUCT_CONDITION, String.valueOf(getProductCondition()));
        params.put(PRODUCT_DESCRIPTION, getProductDescription());
        params.put(PRODUCT_ETALASE_ID, String.valueOf(getProductEtalaseId()));
        params.put(PRODUCT_ETALASE_NAME, getProductEtalaseName());
        params.put(PRODUCT_MIN_ORDER, String.valueOf(getProductMinOrder()));
        params.put(PRODUCT_MUST_INSURANCE, String.valueOf(getProductMustInsurance()));
        params.put(PRODUCT_PRICE_CURRENCY, String.valueOf(getProductPriceCurrency()));
        params.put(PRODUCT_RETURNABLE, String.valueOf(getProductReturnable()));
        params.put(PRODUCT_UPLOAD_TO, String.valueOf(getProductUploadTo()));
        params.put(PRODUCT_WEIGHT, String.valueOf(getProductWeight()));
        params.put(PRODUCT_WEIGHT_UNIT, String.valueOf(getProductWeightUnit()));
        params.put(PO_PROCESS_TYPE, String.valueOf(getPoProcessType()));
        params.put(PO_PROCESS_VALUE, String.valueOf(getPoProcessValue()));
        params.put(SERVER_ID, String.valueOf(getServerId()));
        params.putAll(getPhotosParams());
        params.putAll(getWholesaleParams());
        params.putAll(getVideosParams());
        return params;
    }

    public TKPDMapParam<String, String> getWholesaleParams() {
        TKPDMapParam<String, String> wholesaleParams = new TKPDMapParam<>();
        for (int i = 0; i < productWholesale.size(); i++) {
            wholesaleParams.put(PRD_PRC_ + i, productWholesale.get(i).getPrice());
            wholesaleParams.put(QTY_MAX_ + i, productWholesale.get(i).getQtyMax());
            wholesaleParams.put(QTY_MIN_ + i, productWholesale.get(i).getQtyMin());
        }
        return wholesaleParams;
    }

    public TKPDMapParam<String, String> getPhotosParams() {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        if (getProductPhotos().getPhotosServiceModelList().isEmpty()) {
            String productPhotosString = "";
            String productPhotosDefault = "";
            String productPhotosDescriptionString = "";
            for (int i = 0; i < getProductPhotos().getPhotosServiceModelList().size(); i++) {
                ProductPhotoServiceModel productPhoto = getProductPhotos().getPhotosServiceModelList().get(i);
                productPhotosString += productPhoto.getUrl();
                productPhotosDescriptionString += productPhoto.getDescription();
                if (productPhoto.isDefault()) {
                    productPhotosDefault = String.valueOf(i);
                }
                if (i < getProductPhotos().getPhotosServiceModelList().size() - 1) {
                    productPhotosString += DELIMITER;
                    productPhotosDescriptionString += DELIMITER;
                }
            }
            params.put(PRODUCT_PHOTO, productPhotosString);
            params.put(PRODUCT_PHOTO_DEFAULT, productPhotosDefault);
            params.put(PRODUCT_PHOTO_DESC, productPhotosDescriptionString);
        }
        return params;
    }

    public TKPDMapParam<String, String> getVideosParams() {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(PRODUCT_VIDEO_SIZE, String.valueOf(getProductVideo().size()));
        for (int i = 0; i < getProductVideo().size(); i++) {
            params.put(PRODUCT_VIDEO_ + i, getProductVideo().get(i));
        }
        return params;
    }

    public int getProductCatalogId() {
        return productCatalogId;
    }

    public void setProductCatalogId(int productCatalogId) {
        this.productCatalogId = productCatalogId;
    }

    public int getProductCondition() {
        return productCondition;
    }

    public void setProductCondition(int productCondition) {
        this.productCondition = productCondition;
    }

    public int getProductDepartmentId() {
        return productDepartmentId;
    }

    public void setProductDepartmentId(int productDepartmentId) {
        this.productDepartmentId = productDepartmentId;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getProductEtalaseId() {
        return productEtalaseId;
    }

    public void setProductEtalaseId(int productEtalaseId) {
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

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
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

    public void setProductWholesale(List<ProductWholesaleServiceModel> productWholesale) {
        this.productWholesale = productWholesale;
    }

    public void setProductPhotos(ProductPhotoListServiceModel productPhotos) {
        this.productPhotos = productPhotos;
    }

    public List<ProductWholesaleServiceModel> getProductWholesale() {
        return productWholesale;
    }

    public ProductPhotoListServiceModel getProductPhotos() {
        return productPhotos;
    }

    public List<String> getProductVideo() {
        return productVideo;
    }

    public void setProductVideo(List<String> productVideo) {
        this.productVideo = productVideo;
    }
}
