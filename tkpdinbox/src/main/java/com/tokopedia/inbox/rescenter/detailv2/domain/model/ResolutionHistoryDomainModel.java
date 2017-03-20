package com.tokopedia.inbox.rescenter.detailv2.domain.model;

import java.util.List;

/**
 * Created by hangnadi on 3/17/17.
 */

public class ResolutionHistoryDomainModel {

    List<ResolutionHistoryItemDomainModel> list;

    public List<ResolutionHistoryItemDomainModel> getList() {
        return list;
    }

    public void setList(List<ResolutionHistoryItemDomainModel> list) {
        this.list = list;
    }
}
