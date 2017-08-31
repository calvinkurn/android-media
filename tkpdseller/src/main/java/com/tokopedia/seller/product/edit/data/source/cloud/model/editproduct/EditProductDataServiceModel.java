
package com.tokopedia.seller.product.edit.data.source.cloud.model.editproduct;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditProductDataServiceModel {

    @SerializedName("is_success")
    @Expose
    private Integer isSuccess;

    public Integer getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }

}
