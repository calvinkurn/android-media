package com.tokopedia.seller.product.edit.data.source.cloud.model;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantDataSubmit;

import java.util.List;
import java.util.Locale;

/**
 * @author sebastianuskh on 4/11/17.
 */

@Deprecated
public class AddProductValidationInputServiceModel extends AddProductPictureInputServiceModel {
    public static final String PRODUCT_CATALOG_ID = "product_catalog_id";
    public static final String PRODUCT_CONDITION = "product_condition";
    public static final String PRODUCT_DEPARTMENT_ID = "product_department_id";
    public static final String PRODUCT_DESCRIPTION = "product_description";
    public static final String PRODUCT_ETALASE_ID = "product_etalase_id";
    public static final String PRODUCT_MIN_ORDER = "product_min_order";
    public static final String PRODUCT_MUST_INSURANCE = "product_must_insurance";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_PRICE = "product_price";
    public static final String PRODUCT_PRICE_CURRENCY = "product_price_currency";
    public static final String PRODUCT_RETURNABLE = "product_returnable";
    public static final String PRODUCT_UPLOAD_TO = "product_upload_to";
    public static final String PRODUCT_INVENAGE_SWITCH = "product_switch_invenage";
    public static final String PRODUCT_INVENAGE_VALUE = "product_invenage_value";
    public static final String PRODUCT_WEIGHT = "product_weight";
    public static final String PRODUCT_WEIGHT_UNIT = "product_weight_unit";
    public static final String PO_PROCESS_TYPE = "po_process_type";
    public static final String PO_PROCESS_VALUE = "po_process_value";
    public static final String PRODUCT_VIDEO_SIZE = "product_video_size";
    public static final String PRODUCT_VIDEO_ = "product_video_";
    public static final String PRD_PRC_ = "prd_prc_";
    public static final String QTY_MAX_ = "qty_max_";
    public static final String QTY_MIN_ = "qty_min_";
    public static final String SWITCH_VARIANT = "switch_variant";
    public static final String VARIANT_DATA = "variant_data";

    private List<ProductWholesaleServiceModel> productWholesale;
    private List<String> productVideo;
    private String productDescription;
    private String productName;
    private double productPrice;
    private long productCatalogId;
    private int productCondition;
    private long productDepartmentId;
    private long productEtalaseId;
    private int productMinOrder;
    private int productMustInsurance;
    private int productPriceCurrency;
    private int productReturnable;
    private int productUploadTo;
    private int productInvenageSwitch;
    private int productInvenageValue;
    private int productWeight;
    private int productWeightUnit;
    private int poProcessType;
    private int poProcessValue;
    private int switchVariant;
    private ProductVariantDataSubmit productVariantDataSubmit;

    public TKPDMapParam<String, String> generateMapParam() {
        TKPDMapParam<String, String> params = super.generateMapParam();
        if (StringUtils.isNotBlank(getProductName())) {
            params.put(PRODUCT_NAME, getProductName());
        }

        if (StringUtils.isNotBlank(getProductDescription())) {
            params.put(PRODUCT_DESCRIPTION, getProductDescription());
        }
        params.put(PRODUCT_DEPARTMENT_ID, String.valueOf(getProductDepartmentId()));
        params.put(PRODUCT_CATALOG_ID, String.valueOf(getProductCatalogId()));
        params.put(PRODUCT_PRICE, formatDecimal(getProductPrice()));
        params.put(PRODUCT_CONDITION, String.valueOf(getProductCondition()));
        params.put(PRODUCT_ETALASE_ID, String.valueOf(getProductEtalaseId()));
        params.put(PRODUCT_MIN_ORDER, String.valueOf(getProductMinOrder()));
        params.put(PRODUCT_MUST_INSURANCE, String.valueOf(getProductMustInsurance()));
        params.put(PRODUCT_PRICE_CURRENCY, String.valueOf(getProductPriceCurrency()));
        params.put(PRODUCT_RETURNABLE, String.valueOf(getProductReturnable()));
        params.put(PRODUCT_UPLOAD_TO, String.valueOf(getProductUploadTo()));
        params.put(PRODUCT_INVENAGE_SWITCH, String.valueOf(getProductInvenageSwitch()));
        params.put(PRODUCT_INVENAGE_VALUE, String.valueOf(getProductInvenageValue()));
        params.put(PRODUCT_WEIGHT, String.valueOf(getProductWeight()));
        params.put(PRODUCT_WEIGHT_UNIT, String.valueOf(getProductWeightUnit()));
        params.put(PO_PROCESS_TYPE, String.valueOf(getPoProcessType()));
        params.put(PO_PROCESS_VALUE, String.valueOf(getPoProcessValue()));
        params.put(SERVER_ID, String.valueOf(getServerId()));
        params.putAll(getWholesaleParams());
        params.putAll(getVideosParams());
        params.put(SWITCH_VARIANT, String.valueOf(getSwitchVariant()));
        if (switchVariant > 0) {
            params.put(VARIANT_DATA,
                    CacheUtil.convertModelToString(getProductVariantDataSubmit(), new TypeToken<ProductVariantDataSubmit>() {
                    }.getType()));
        }
        return params;
    }

    public int getSwitchVariant() {
        return switchVariant;
    }

    public void setSwitchVariant(int switchVariant) {
        this.switchVariant = switchVariant;
    }

    public ProductVariantDataSubmit getProductVariantDataSubmit() {
        return productVariantDataSubmit;
    }

    public void setProductVariantDataSubmit(ProductVariantDataSubmit productVariantDataSubmit) {
        this.productVariantDataSubmit = productVariantDataSubmit;
    }

    public TKPDMapParam<String, String> getWholesaleParams() {
        TKPDMapParam<String, String> wholesaleParams = new TKPDMapParam<>();
        for (int i = 1; i <= productWholesale.size(); i++) {
            wholesaleParams.put(PRD_PRC_ + i, formatDecimal(productWholesale.get(i - 1).getPrice()));
            wholesaleParams.put(QTY_MAX_ + i, String.valueOf(productWholesale.get(i - 1).getQtyMax()));
            wholesaleParams.put(QTY_MIN_ + i, String.valueOf(productWholesale.get(i - 1).getQtyMin()));
        }
        return wholesaleParams;
    }

    public TKPDMapParam<String, String> getVideosParams() {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(PRODUCT_VIDEO_SIZE, String.valueOf(getProductVideo().size()));
        for (int i = 0; i < getProductVideo().size(); i++) {
            int count = i + 1;
            params.put(PRODUCT_VIDEO_ + count, getProductVideo().get(i));
        }
        return params;
    }

    private String formatDecimal(double productPrice) {
        if (productPrice == (long) productPrice)
            return String.format(Locale.US, "%d", (long) productPrice);
        else
            return String.format("%s", productPrice);
    }

    public long getProductCatalogId() {
        return productCatalogId;
    }

    public void setProductCatalogId(long productCatalogId) {
        this.productCatalogId = productCatalogId;
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

    public void setProductUploadTo(int productUploadTo) {
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

    public List<ProductWholesaleServiceModel> getProductWholesale() {
        return productWholesale;
    }

    public void setProductWholesale(List<ProductWholesaleServiceModel> productWholesale) {
        this.productWholesale = productWholesale;
    }

    public List<String> getProductVideo() {
        return productVideo;
    }

    public void setProductVideo(List<String> productVideo) {
        this.productVideo = productVideo;
    }
}
