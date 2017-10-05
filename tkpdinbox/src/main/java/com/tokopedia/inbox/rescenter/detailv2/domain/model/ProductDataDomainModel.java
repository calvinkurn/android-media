package com.tokopedia.inbox.rescenter.detailv2.domain.model;

import java.util.List;

/**
 * Created by hangnadi on 3/17/17.
 */

public class ProductDataDomainModel {
    private List<ProductComplainedDomainModel> list;

    public List<ProductComplainedDomainModel> getList() {
        return list;
    }

    public void setList(List<ProductComplainedDomainModel> list) {
        this.list = list;
    }
}
