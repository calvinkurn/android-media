package com.tokopedia.seller.product.view.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
    private boolean isValidURL;

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

    public boolean isValidURL(String urlStr) {
        try {
            URI uri = new URI(urlStr);
            return uri.getScheme().equals("http") || uri.getScheme().equals("https");
        } catch (Exception e) {
            return false;
        }
    }

    public void setUri(String uri) {
        this.uri = uri;

        // when uri change, recalculate its width/height
        this.isValidURL = isValidURL(uri);
        calculateWidthAndHeight(isValidURL);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isValidURL() {
        return isValidURL;
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMinResolution() {
        return Math.min(width, height);
    }

    private void calculateWidthAndHeight(boolean isValidURL) {
        if (! isValidURL) { // local URI
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(new File(uri).getAbsolutePath(), options);
            this.width = options.outWidth;
            this.height = options.outHeight;
        }
    }
}
