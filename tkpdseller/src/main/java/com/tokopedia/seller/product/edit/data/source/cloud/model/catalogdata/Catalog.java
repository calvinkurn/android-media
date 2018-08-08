package com.tokopedia.seller.product.edit.data.source.cloud.model.catalogdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hendry on 4/5/2017.
 */

public class Catalog {

    @SerializedName("catalog_id")
    @Expose
    private int catalogId;
    @SerializedName("catalog_name")
    @Expose
    private String catalogName;
    @SerializedName("catalog_price")
    @Expose
    private String catalogPrice;
    @SerializedName("catalog_uri")
    @Expose
    private String catalogUri;
    @SerializedName("catalog_image")
    @Expose
    private String catalogImage;
    @SerializedName("catalog_image_300")
    @Expose
    private String catalogImage300;
    @SerializedName("catalog_description")
    @Expose
    private String catalogDescription;
    @SerializedName("catalog_count_product")
    @Expose
    private int catalogCountProduct;

    public int getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getCatalogPrice() {
        return catalogPrice;
    }

    public void setCatalogPrice(String catalogPrice) {
        this.catalogPrice = catalogPrice;
    }

    public String getCatalogUri() {
        return catalogUri;
    }

    public void setCatalogUri(String catalogUri) {
        this.catalogUri = catalogUri;
    }

    public String getCatalogImage() {
        return catalogImage;
    }

    public void setCatalogImage(String catalogImage) {
        this.catalogImage = catalogImage;
    }

    public String getCatalogImage300() {
        return catalogImage300;
    }

    public void setCatalogImage300(String catalogImage300) {
        this.catalogImage300 = catalogImage300;
    }

    public String getCatalogDescription() {
        return catalogDescription;
    }

    public void setCatalogDescription(String catalogDescription) {
        this.catalogDescription = catalogDescription;
    }

    public int getCatalogCountProduct() {
        return catalogCountProduct;
    }

    public void setCatalogCountProduct(int catalogCountProduct) {
        this.catalogCountProduct = catalogCountProduct;
    }

}
