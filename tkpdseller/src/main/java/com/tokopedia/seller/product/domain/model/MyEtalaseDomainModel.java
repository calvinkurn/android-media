package com.tokopedia.seller.product.domain.model;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class MyEtalaseDomainModel {
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
}
