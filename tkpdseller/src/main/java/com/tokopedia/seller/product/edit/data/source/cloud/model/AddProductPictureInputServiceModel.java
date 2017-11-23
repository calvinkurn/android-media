package com.tokopedia.seller.product.edit.data.source.cloud.model;

import android.text.TextUtils;

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
    private int serverId;
    private String hostUrl;

    public TKPDMapParam<String, String> generateMapParam() {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(SERVER_ID, String.valueOf(getServerId()));
        params.putAll(getPhotosParams());
        return params;
    }

    public TKPDMapParam<String, String> getPhotosParams() {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        if (!getProductPhoto().getPhotosServiceModelList().isEmpty()) {
            String productPhotosString = "";
            String productPhotosDescriptionString = "";
            String defaultPhoto = "";
            for (int i = 0; i < getProductPhoto().getPhotosServiceModelList().size(); i++) {
                if (i == getProductPhoto().getProductDefaultPhoto()){
                    String picureId = getProductPhoto().getPhotosServiceModelList().get(i).getPicureId();
                    // If add, need to put index, if edit need to put picture id
                    if (!TextUtils.isEmpty(picureId)) {
                        defaultPhoto = picureId;
                    } else {
                        defaultPhoto = String.valueOf(i);
                    }
                }
                ProductPhotoServiceModel productPhoto = getProductPhoto().getPhotosServiceModelList().get(i);
                productPhotosString += getPhotoParam(productPhoto);
                productPhotosDescriptionString += productPhoto.getDescription();
                if (i < getProductPhoto().getPhotosServiceModelList().size() - 1) {
                    productPhotosString += DELIMITER;
                    productPhotosDescriptionString += DELIMITER;
                }
            }
            params.put(PRODUCT_PHOTO, productPhotosString);
            params.put(PRODUCT_PHOTO_DEFAULT, defaultPhoto);
            params.put(PRODUCT_PHOTO_DESC, productPhotosDescriptionString);
        }
        return params;
    }

    protected String getPhotoParam(ProductPhotoServiceModel productPhoto) {
        return productPhoto.getUrl();
    }

    public ProductPhotoListServiceModel getProductPhoto() {
        return productPhoto;
    }

    public void setProductPhoto(ProductPhotoListServiceModel productPhoto) {
        this.productPhoto = productPhoto;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }
}
