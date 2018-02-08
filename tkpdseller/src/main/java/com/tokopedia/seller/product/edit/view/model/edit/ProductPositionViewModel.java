package com.tokopedia.seller.product.edit.view.model.edit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 2/8/18.
 */

public class ProductPositionViewModel {
    @SerializedName("position")
    @Expose
    private long productPosition;


    public long getProductPosition() {
        return productPosition;
    }

    public void setProductPosition(long productPosition) {
        this.productPosition = productPosition;
    }
}
