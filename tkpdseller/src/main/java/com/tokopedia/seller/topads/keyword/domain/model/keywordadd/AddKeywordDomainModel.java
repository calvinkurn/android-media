package com.tokopedia.seller.topads.keyword.domain.model.keywordadd;

import java.util.List;

/**
 * Created by Test on 5/26/2017.
 */

public class AddKeywordDomainModel {
    private List<AddKeywordDomainModelDatum> addKeywordDomainModelDatumList;

    public AddKeywordDomainModel(List<AddKeywordDomainModelDatum> addKeywordDomainModelDatumList) {
        this.addKeywordDomainModelDatumList = addKeywordDomainModelDatumList;
    }

    public List<AddKeywordDomainModelDatum> getAddKeywordDomainModelDatumList() {
        return addKeywordDomainModelDatumList;
    }
}
