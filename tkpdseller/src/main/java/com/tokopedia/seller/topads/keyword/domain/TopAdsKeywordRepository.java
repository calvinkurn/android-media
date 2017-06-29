package com.tokopedia.seller.topads.keyword.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.topads.keyword.domain.model.EditTopAdsKeywordDetailDomainModel;
import com.tokopedia.seller.topads.keyword.domain.model.KeywordDashboardDomain;
import com.tokopedia.seller.topads.keyword.domain.model.TopAdsKeywordEditDetailInputDomainModel;
import com.tokopedia.seller.topads.keyword.domain.model.keywordadd.AddKeywordDomainModel;

import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public interface TopAdsKeywordRepository {
    Observable<KeywordDashboardDomain> getDashboardKeyword(RequestParams requestParams);

    Observable<EditTopAdsKeywordDetailDomainModel> editTopAdsKeywordDetail(TopAdsKeywordEditDetailInputDomainModel modelInput);

    Observable<AddKeywordDomainModel> addKeyword(AddKeywordDomainModel addKeywordDomainModel);
}
