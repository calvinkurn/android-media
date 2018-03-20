package com.tokopedia.seller.product.edit.view.model.edit;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nakama on 06/03/18.
 */

public abstract class BasePictureViewModel implements Parcelable {
    public static final int ACTIVE_STATUS = 1;

    // currently not used in UI, only for backend
    //0 -> deleted
    //1 -> active
    //2 -> primary
    //-3 -> sizechart
    @SerializedName("status")
    @Expose
    protected long status = ACTIVE_STATUS;

    @SerializedName("file_name")
    @Expose
    protected String fileName;
    @SerializedName("file_path")
    @Expose
    protected String filePath; // or store the local file in local android

    @SerializedName("url_original")
    @Expose
    protected String urlOriginal;
    @SerializedName("url_thumbnail")
    @Expose
    protected String urlThumbnail;

    @SerializedName("x")
    @Expose
    protected long x;
    @SerializedName("y")
    @Expose
    protected long y;

    @SerializedName("from_ig")
    @Expose
    protected int fromIg;

    public abstract long getId();
    public abstract void setId(long id);

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUrlOriginal() {
        return urlOriginal;
    }

    public String getUriOrPath(){
        if (getId() <= 0 && !TextUtils.isEmpty(getFilePath())) {
            return getFilePath();
        }
        return getUrlOriginal();
    }

    public void setUrlOriginal(String urlOriginal) {
        this.urlOriginal = urlOriginal;
    }

    public String getUrlThumbnail() {
        return urlThumbnail;
    }

    public void setUrlThumbnail(String urlThumbnail) {
        this.urlThumbnail = urlThumbnail;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }

    public int getFromIg() {
        return fromIg;
    }

    public void setFromIg(int fromIg) {
        this.fromIg = fromIg;
    }


}
