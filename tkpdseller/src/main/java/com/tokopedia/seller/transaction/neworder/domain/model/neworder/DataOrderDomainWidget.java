package com.tokopedia.seller.transaction.neworder.domain.model.neworder;

import java.util.List;

/**
 * Created by zulfikarrahman on 7/17/17.
 */

public class DataOrderDomainWidget {
    private int dataOrderCount;
    private List<DataOrderDetailDomain> dataOrderDetailDomains;

    public void setDataOrderCount(int dataOrderCount) {
        this.dataOrderCount = dataOrderCount;
    }

    public void setDataOrderDetailDomains(List<DataOrderDetailDomain> dataOrderDetailDomains) {
        this.dataOrderDetailDomains = dataOrderDetailDomains;
    }

    public int getDataOrderCount() {
        return dataOrderCount;
    }

    public List<DataOrderDetailDomain> getDataOrderDetailDomains() {
        return dataOrderDetailDomains;
    }
}
