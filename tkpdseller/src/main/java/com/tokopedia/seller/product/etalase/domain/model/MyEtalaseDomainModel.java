package com.tokopedia.seller.product.etalase.domain.model;

import java.util.List;

/**
 * @author sebastianuskh on 5/3/17.
 */

public class MyEtalaseDomainModel {
    private List<MyEtalaseItemDomainModel> etalaseItems;
    private boolean hasNext;

    public List<MyEtalaseItemDomainModel> getEtalaseItems() {
        return etalaseItems;
    }

    public void setEtalaseItems(List<MyEtalaseItemDomainModel> etalaseItems) {
        this.etalaseItems = etalaseItems;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
}
