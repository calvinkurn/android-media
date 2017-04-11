package com.tokopedia.seller.product.view.model;

import android.support.annotation.Nullable;

/**
 * Created by m.normansyah on 03/12/2015.
 */

public class ImageSelectModel {
    // this is for url / path from sdcard
    private String uri;
    private String description;
    private boolean isPrimary;

    public ImageSelectModel(String uri) {
        this(uri, null, false);
    }

    public ImageSelectModel(String uri,
                            @Nullable String description) {
        this(uri, description, false);
    }

    public ImageSelectModel(String uri,
                            @Nullable String description,
                            boolean isPrimary) {
        this.uri = uri;
        this.description = description;
        this.isPrimary = isPrimary;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

}
