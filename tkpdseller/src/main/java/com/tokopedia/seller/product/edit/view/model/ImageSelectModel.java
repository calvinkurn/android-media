package com.tokopedia.seller.product.edit.view.model;

import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.io.File;
import java.net.URI;

/**
 * Created by m.normansyah on 03/12/2015.
 */

public class ImageSelectModel implements Parcelable{
    // this is for url / path from sdcard
    private String uriOrPath;
    private String description;
    private boolean isPrimary;
    private int width;
    private int height;
    private boolean isValidURL;
    private boolean allowDelete;

    public ImageSelectModel(String uriOrPath) {
        this(uriOrPath, null, false, true);
    }

    public ImageSelectModel(String uriOrPath,
                            @Nullable String description,
                            boolean isPrimary,
                            boolean allowDelete) {
        setUriOrPath(uriOrPath);
        this.description = description;
        this.isPrimary = isPrimary;
        this.allowDelete = allowDelete;
    }

    public boolean allowDelete() {
        return allowDelete;
    }

    public String getUriOrPath() {
        return uriOrPath;
    }

    public boolean isValidURL(String urlStr) {
        try {
            URI uri = new URI(urlStr);
            return uri.getScheme().equals("http") || uri.getScheme().equals("https");
        } catch (Exception e) {
            return false;
        }
    }

    public void setUriOrPath(String uriOrPath) {
        this.uriOrPath = uriOrPath;

        // when uri change, recalculate its width/height
        this.isValidURL = isValidURL(uriOrPath);
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
            BitmapFactory.decodeFile(new File(uriOrPath).getAbsolutePath(), options);
            this.width = options.outWidth;
            this.height = options.outHeight;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uriOrPath);
        dest.writeString(this.description);
        dest.writeByte(this.isPrimary ? (byte) 1 : (byte) 0);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeByte(this.isValidURL ? (byte) 1 : (byte) 0);
        dest.writeByte(this.allowDelete ? (byte) 1 : (byte) 0);
    }

    protected ImageSelectModel(Parcel in) {
        this.uriOrPath = in.readString();
        this.description = in.readString();
        this.isPrimary = in.readByte() != 0;
        this.width = in.readInt();
        this.height = in.readInt();
        this.isValidURL = in.readByte() != 0;
        this.allowDelete = in.readByte() != 0;
    }

    public static final Creator<ImageSelectModel> CREATOR = new Creator<ImageSelectModel>() {
        @Override
        public ImageSelectModel createFromParcel(Parcel source) {
            return new ImageSelectModel(source);
        }

        @Override
        public ImageSelectModel[] newArray(int size) {
            return new ImageSelectModel[size];
        }
    };
}
