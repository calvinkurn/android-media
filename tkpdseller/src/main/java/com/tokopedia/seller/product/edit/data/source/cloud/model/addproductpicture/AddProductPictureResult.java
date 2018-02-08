
package com.tokopedia.seller.product.edit.data.source.cloud.model.addproductpicture;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddProductPictureResult {

    @SerializedName("file_uploaded")
    @Expose
    private String fileUploaded;

    public String getFileUploaded() {
        return fileUploaded;
    }

    public void setFileUploaded(String fileUploaded) {
        this.fileUploaded = fileUploaded;
    }

}
