package com.tokopedia.seller.product.edit.data.source.cloud.model;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

/**
 * @author sebastianuskh on 4/12/17.
 */
@Deprecated
public class EditProductInputServiceModel extends AddProductValidationInputServiceModel{

    public static final String PRODUCT_CHANGE_CATALOG = "product_change_catalog";
    public static final String PRODUCT_CHANGE_WHOLESALE = "product_change_wholesale";
    public static final String PRODUCT_CHANGE_PHOTO = "product_change_photo";
    public static final String PRODUCT_ID = "product_id";
    private int productChangeCatalog;
    private int productChangeWholesale;
    private int productChangePhoto;
    private String productId;

    @Override
    public TKPDMapParam<String, String> generateMapParam() {
        TKPDMapParam<String, String> param = super.generateMapParam();
        param.put(PRODUCT_CHANGE_CATALOG, String.valueOf(getProductChangeCatalog()));
        param.put(PRODUCT_CHANGE_WHOLESALE, String.valueOf(getProductChangeWholesale()));
        param.put(PRODUCT_CHANGE_PHOTO, String.valueOf(getProductChangePhoto()));
        param.put(PRODUCT_ID, getProductId());
        return param;
    }

    @Override
    protected String getPhotoParam(ProductPhotoServiceModel productPhoto) {
        return productPhoto.getPicureId();
    }

    public int getProductChangeCatalog() {
        return productChangeCatalog;
    }

    public void setProductChangeCatalog(int productChangeCatalog) {
        this.productChangeCatalog = productChangeCatalog;
    }

    public int getProductChangeWholesale() {
        return productChangeWholesale;
    }

    public void setProductChangeWholesale(int productChangeWholesale) {
        this.productChangeWholesale = productChangeWholesale;
    }

    public int getProductChangePhoto() {
        return productChangePhoto;
    }

    public void setProductChangePhoto(int productChangePhoto) {
        this.productChangePhoto = productChangePhoto;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
