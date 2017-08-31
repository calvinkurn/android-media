
package com.tokopedia.seller.product.edit.data.source.cloud.model.editimageproduct;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditIageProductDataServiceModule {

    @SerializedName("pic_id")
    @Expose
    private String picId;
    @SerializedName("is_success")
    @Expose
    private Integer isSuccess;

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

    public Integer getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }

}
