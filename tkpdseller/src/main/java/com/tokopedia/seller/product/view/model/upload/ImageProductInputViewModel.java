package com.tokopedia.seller.product.view.model.upload;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.tokopedia.seller.product.constant.ImageStatusTypeDef;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ImageProductInputViewModel {
    private String imagePath;
    private String imageDescription;
    private String picId;
    private String url;

    private int imageResolution;
    @ImageStatusTypeDef
    private int status;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    public String getImageDescription() {
        if (TextUtils.isEmpty(imageDescription)) {
            return "";
        }
        return imageDescription;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPicId() {
        return picId;
    }

    public String getUrl() {
        return url;
    }

    public void setImageResolution(int imageResolution) {
        this.imageResolution = imageResolution;
    }

    public int getImageResolution() {
        return imageResolution;
    }

    public void setStatus(@ImageStatusTypeDef int status) {
        this.status = status;
    }

    @ImageStatusTypeDef
    public int getStatus() {
        return status;
    }
}
