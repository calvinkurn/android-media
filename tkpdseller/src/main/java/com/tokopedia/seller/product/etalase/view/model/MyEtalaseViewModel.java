package com.tokopedia.seller.product.etalase.view.model;

import java.util.List;

/**
 * @author sebastianuskh on 5/3/17.
 */

public class MyEtalaseViewModel {
    private List<MyEtalaseItemViewModel> etalaseList;
    private boolean hasNextPage;

    public List<MyEtalaseItemViewModel> getEtalaseList() {
        return etalaseList;
    }

    public void setEtalaseList(List<MyEtalaseItemViewModel> etalaseList) {
        this.etalaseList = etalaseList;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }
}
