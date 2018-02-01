
package com.tokopedia.seller.product.edit.view.model.edit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductBrandViewModel {

    @SerializedName("brand_id")
    @Expose
    private long brandId;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("brand_status")
    @Expose
    private long brandStatus;

    public long getBrandId() {
        return brandId;
    }

    public void setBrandId(long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public long getBrandStatus() {
        return brandStatus;
    }

    public void setBrandStatus(long brandStatus) {
        this.brandStatus = brandStatus;
    }

}
