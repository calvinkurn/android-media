
package com.tokopedia.seller.product.edit.view.model.edit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductPictureViewModel {

    @SerializedName("id")
    @Expose
    private long id;

    // currently not used in UI, only for backend
    //0 -> deleted
    //1 -> active
    //2 -> primary
    //-3 -> sizechart
    //@SerializedName("status")
    //@Expose
    //private long status;

    @SerializedName("file_name")
    @Expose
    private String fileName;
    @SerializedName("file_path")
    @Expose
    private String filePath;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("x")
    @Expose
    private long x;
    @SerializedName("y")
    @Expose
    private long y;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

}
