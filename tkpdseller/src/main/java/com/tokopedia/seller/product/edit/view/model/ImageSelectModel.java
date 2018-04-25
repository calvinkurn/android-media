package com.tokopedia.seller.product.edit.view.model;

import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.product.common.utils.UrlUtils;

import java.io.File;

/**
 * Created by m.normansyah on 03/12/2015.
 */

public class ImageSelectModel implements Parcelable{
    // this is for url / path from sdcard
    private String uriOrPath;
    private String description;
    private long width;
    private long height;
    private boolean isValidURL;
    private long id;

    private String serverFileName;
    private String serverFilePath;

    public ImageSelectModel(String uriOrPath) {
        this(uriOrPath, null, 0, 0, 0, null, null);
    }

    public ImageSelectModel(String uriOrPath, String description, long width, long height, long id,
                            String serverFilePath, String serverFileName) {
        this.description = description;
        this.width = width;
        this.height = height;
        this.id = id;

        this.serverFilePath = serverFilePath;
        this.serverFileName = serverFileName;

        setUriOrPath(uriOrPath);
    }

    public String getUriOrPath() {
        return uriOrPath;
    }

    public String getServerFileName() {
        return serverFileName;
    }

    public String getServerFilePath() {
        return serverFilePath;
    }

    public void setUriOrPath(String uriOrPath) {
        this.uriOrPath = uriOrPath;

        // when uri change, recalculate its width/height
        this.isValidURL = UrlUtils.isValidURL(uriOrPath);
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

    public long getWidth() {
        return width;
    }

    public long getHeight() {
        return height;
    }

    public long getMinResolution() {
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

    public long getId() {
        return id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uriOrPath);
        dest.writeString(this.description);
        dest.writeLong(this.width);
        dest.writeLong(this.height);
        dest.writeByte(this.isValidURL ? (byte) 1 : (byte) 0);
        dest.writeLong(this.id);
        dest.writeString(this.serverFileName);
        dest.writeString(this.serverFilePath);
    }

    protected ImageSelectModel(Parcel in) {
        this.uriOrPath = in.readString();
        this.description = in.readString();
        this.width = in.readLong();
        this.height = in.readLong();
        this.isValidURL = in.readByte() != 0;
        this.id = in.readLong();
        this.serverFileName = in.readString();
        this.serverFilePath = in.readString();
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
