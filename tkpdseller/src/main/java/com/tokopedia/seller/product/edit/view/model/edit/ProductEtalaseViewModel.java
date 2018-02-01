
package com.tokopedia.seller.product.edit.view.model.edit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductEtalaseViewModel {

    @SerializedName("etalase_id")
    @Expose
    private long etalaseId;
    @SerializedName("etalase_name")
    @Expose
    private String etalaseName;

    public long getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(long etalaseId) {
        this.etalaseId = etalaseId;
    }

    public String getEtalaseName() {
        return etalaseName;
    }

    public void setEtalaseName(String etalaseName) {
        this.etalaseName = etalaseName;
    }

}
