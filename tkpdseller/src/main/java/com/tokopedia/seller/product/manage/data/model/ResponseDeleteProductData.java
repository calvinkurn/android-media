package com.tokopedia.seller.product.manage.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public class ResponseDeleteProductData {
    @SerializedName("is_success")
    @Expose
    int is_success;

    public int getIs_success() {
        return is_success;
    }

    public void setIs_success(int is_success) {
        this.is_success = is_success;
    }
}
