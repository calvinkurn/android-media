package com.tokopedia.seller.product.data.source.cloud.model;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductPictureInputServiceModel {
    private String duplicate;
    private String productPhoto;
    private String productPhotoDefault;
    private String productPhotoDesc;
    private String serverId;

    public TKPDMapParam<String, String> generateMapParam() {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put("duplicate", getDuplicate());
        params.put("product_photo", getProductPhoto());
        params.put("product_photo_default", getProductPhotoDefault());
        params.put("product_photo_desc", getProductPhotoDesc());
        params.put("server_id", getServerId());
        return params;
    }

    public String getDuplicate() {
        return duplicate;
    }

    public String getProductPhoto() {
        return productPhoto;
    }

    public String getProductPhotoDefault() {
        return productPhotoDefault;
    }

    public String getProductPhotoDesc() {
        return productPhotoDesc;
    }

    public String getServerId() {
        return serverId;
    }

    public void setDuplicate(String duplicate) {
        this.duplicate = duplicate;
    }

    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }

    public void setProductPhotoDefault(String productPhotoDefault) {
        this.productPhotoDefault = productPhotoDefault;
    }

    public void setProductPhotoDesc(String productPhotoDesc) {
        this.productPhotoDesc = productPhotoDesc;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
}
