package com.tokopedia.seller.product.data.source.cloud.model;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductPictureInputServiceModel {
    public static final String SERVER_ID = "server_id";
    public static final String PRODUCT_PHOTO = "product_photo";
    public static final String PRODUCT_PHOTO_DEFAULT = "product_photo_default";
    public static final String PRODUCT_PHOTO_DESC = "product_photo_desc";
    public static final String DELIMITER = "~";
    private ProductPhotoListServiceModel productPhoto;
    private String serverId;

    public TKPDMapParam<String, String> generateMapParam() {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(SERVER_ID, getServerId());
        params.putAll(getPhotosParams());
        return params;
    }

    public TKPDMapParam<String, String> getPhotosParams() {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        if (getProductPhoto().getPhotosServiceModelList().isEmpty()) {
            String productPhotosString = "";
            String productPhotosDefault = "";
            String productPhotosDescriptionString = "";
            for (int i = 0; i < getProductPhoto().getPhotosServiceModelList().size(); i++) {
                ProductPhotoServiceModel productPhoto = getProductPhoto().getPhotosServiceModelList().get(i);
                productPhotosString += productPhoto.getUrl();
                productPhotosDescriptionString += productPhoto.getDescription();
                if (productPhoto.isDefault()) {
                    productPhotosDefault = String.valueOf(i);
                }
                if (i < getProductPhoto().getPhotosServiceModelList().size() - 1) {
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

    public ProductPhotoListServiceModel getProductPhoto() {
        return productPhoto;
    }

    public String getServerId() {
        return serverId;
    }

    public void setProductPhoto(ProductPhotoListServiceModel productPhoto) {
        this.productPhoto = productPhoto;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
}
