package com.tokopedia.seller.product.etalase.view.model;


import com.tokopedia.seller.R;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class MyEtalaseItemViewModel implements EtalaseViewModel {
    public static final int LAYOUT = R.layout.item_product_etalase_picker;
    private Integer etalaseId;
    private String etalaseName;

    public void setEtalaseId(Integer etalaseId) {
        this.etalaseId = etalaseId;
    }

    public void setEtalaseName(String etalaseName) {
        this.etalaseName = etalaseName;
    }

    public Integer getEtalaseId() {
        return etalaseId;
    }

    public String getEtalaseName() {
        return etalaseName;
    }

    @Override
    public int getType() {
        return LAYOUT;
    }
}
