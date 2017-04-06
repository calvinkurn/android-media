package com.tokopedia.seller.product.view.model.etalase;


import com.tokopedia.seller.R;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class MyEtalaseViewModel implements EtalaseViewModel {
    public static final int LAYOUT = R.layout.etalase_picker_item_layout;
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
