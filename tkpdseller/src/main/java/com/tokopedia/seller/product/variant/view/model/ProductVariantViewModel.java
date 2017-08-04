package com.tokopedia.seller.product.variant.view.model;

import com.tokopedia.seller.base.view.adapter.ItemPickerType;

/**
 * Created by nathan on 8/4/17.
 */

public class ProductVariantViewModel implements ItemPickerType {

    public final static int TYPE = 123;

    private String id;
    private String title;
    private String imageUrl;

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public int getType() {
        return TYPE;
    }
}