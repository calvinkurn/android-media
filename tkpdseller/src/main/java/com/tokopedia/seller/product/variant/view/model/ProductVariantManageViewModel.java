package com.tokopedia.seller.product.variant.view.model;

import com.tokopedia.seller.base.view.adapter.ItemType;

/**
 * Created by nathan on 8/20/17.
 */

public class ProductVariantManageViewModel implements ItemType {

    public final static int TYPE = 123;

    private long temporaryId;
    private String title;
    private String content;
    private String hexCode;
    private String imageUrl;
    private boolean stockAvailable;

    public long getTemporaryId() {
        return temporaryId;
    }

    public void setTemporaryId(long temporaryId) {
        this.temporaryId = temporaryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHexCode() {
        return hexCode;
    }

    public void setHexCode(String hexCode) {
        this.hexCode = hexCode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isStockAvailable() {
        return stockAvailable;
    }

    public void setStockAvailable(boolean stockAvailable) {
        this.stockAvailable = stockAvailable;
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
