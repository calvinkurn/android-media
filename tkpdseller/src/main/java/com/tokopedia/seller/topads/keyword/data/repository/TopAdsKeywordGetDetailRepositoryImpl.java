package com.tokopedia.seller.topads.keyword.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.seller.topads.keyword.data.model.cloud.Datum;
import com.tokopedia.seller.topads.keyword.data.source.TopAdsKeywordGetDetailDataSource;
import com.tokopedia.seller.topads.keyword.domain.TopAdsKeywordGetDetailRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

public class TopAdsKeywordGetDetailRepositoryImpl implements TopAdsKeywordGetDetailRepository {

    private final TopAdsKeywordGetDetailDataSource topAdsKeywordGetDetailDataSource;

    public TopAdsKeywordGetDetailRepositoryImpl(TopAdsKeywordGetDetailDataSource topAdsKeywordGetDetailDataSource) {
        this.topAdsKeywordGetDetailDataSource = topAdsKeywordGetDetailDataSource;
    }

    @Override
    public Observable<PageDataResponse<List<Datum>>> getKeywordDetail(RequestParams requestParams) {
        return topAdsKeywordGetDetailDataSource.getKeywordDetail(requestParams);
    }
}
