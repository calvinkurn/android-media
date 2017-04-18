package com.tokopedia.seller.product.view.model;

import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import java.io.File;

/**
 * Created by m.normansyah on 03/12/2015.
 */

public class ImageSelectModel {
    // this is for url / path from sdcard
    private String uri;
    private String description;
    private boolean isPrimary;
    private int width;
    private int height;

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
        setUri(uri);
        this.description = description;
        this.isPrimary = isPrimary;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
        // when uri change, recalculate its width/height
        calculateWidthAndHeight();
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

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public int getMinResolution(){
        return Math.min(width, height);
    }

    private void calculateWidthAndHeight(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(uri).getAbsolutePath(), options);
        this.width = options.outWidth;
        this.height = options.outHeight;

    }
}
