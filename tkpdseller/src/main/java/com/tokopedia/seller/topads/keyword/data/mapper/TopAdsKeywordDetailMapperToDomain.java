package com.tokopedia.seller.topads.keyword.data.mapper;

import com.tokopedia.seller.topads.data.model.response.PageDataResponse;
import com.tokopedia.seller.topads.keyword.data.model.KeywordDetailData;
import com.tokopedia.seller.topads.keyword.data.model.cloud.Datum;
import com.tokopedia.seller.topads.keyword.domain.model.KeywordDetailDomain;

import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

public class TopAdsKeywordDetailMapperToDomain implements Func1<PageDataResponse<List<Datum>>, KeywordDetailDomain> {

    @Inject
    public TopAdsKeywordDetailMapperToDomain() {
    }

    @Override
    public KeywordDetailDomain call(PageDataResponse<List<Datum>> listPageDataResponse) {
        return null;
    }
}
