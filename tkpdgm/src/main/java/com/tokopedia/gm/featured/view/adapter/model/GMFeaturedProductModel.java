package com.tokopedia.gm.featured.view.adapter.model;

import com.tokopedia.seller.base.view.adapter.ItemIdType;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class GMFeaturedProductModel implements ItemIdType {
    public static final int TYPE = 128391;
    private long productId;

    private String productName;
    private String productPrice;
    private String imageUrl;

    public GMFeaturedProductModel(long productId, String productName, String productPrice, String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.imageUrl = imageUrl;
    }

    public GMFeaturedProductModel() {
    }

    @Override
    public int getType() {
        return TYPE;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String getId() {
        return Long.toString(productId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GMFeaturedProductModel that = (GMFeaturedProductModel) o;

        return productId == that.productId;

    }

    @Override
    public int hashCode() {
        return (int) (productId ^ (productId >>> 32));
    }
}
