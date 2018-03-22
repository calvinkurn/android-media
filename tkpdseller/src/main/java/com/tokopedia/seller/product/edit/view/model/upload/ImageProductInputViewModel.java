package com.tokopedia.seller.product.edit.view.model.upload;

import android.text.TextUtils;

import com.tokopedia.seller.product.edit.constant.ImageStatusTypeDef;

/**
 * @author sebastianuskh on 4/13/17.
 */
@Deprecated
public class ImageProductInputViewModel {
    private String imagePath;
    private String imageDescription;
    private String picId;
    private String url;
    private boolean canDelete = true;

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

    public boolean canDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
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
