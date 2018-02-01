
package com.tokopedia.seller.product.edit.view.model.edit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductVariantImageViewModel {

    @SerializedName("image_id")
    @Expose
    private String imageId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("file_path")
    @Expose
    private String filePath;
    @SerializedName("file_name")
    @Expose
    private String fileName;
    @SerializedName("x")
    @Expose
    private long x;
    @SerializedName("y")
    @Expose
    private long y;
    @SerializedName("status")
    @Expose
    private long status;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

}
