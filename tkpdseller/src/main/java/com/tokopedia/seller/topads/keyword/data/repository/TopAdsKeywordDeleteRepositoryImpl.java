package com.tokopedia.seller.topads.keyword.data.repository;

import com.tokopedia.seller.topads.keyword.data.mapper.TopAdsKeywordDeleteMapper;
import com.tokopedia.seller.topads.keyword.data.source.TopAdsKeywordDeleteDataSource;
import com.tokopedia.seller.topads.keyword.domain.TopAdsKeywordDeleteRepository;

import rx.Observable;

/**
 * Created by zulfikarrahman on 5/29/17.
 */

public class TopAdsKeywordDeleteRepositoryImpl implements TopAdsKeywordDeleteRepository {
    private final TopAdsKeywordDeleteDataSource topAdsKeywordDeleteDataSource;
    private final TopAdsKeywordDeleteMapper topAdsKeywordDeleteMapper;

    public TopAdsKeywordDeleteRepositoryImpl(TopAdsKeywordDeleteDataSource topAdsKeywordDeleteDataSource,
                                             TopAdsKeywordDeleteMapper topAdsKeywordDeleteMapper) {
        this.topAdsKeywordDeleteDataSource = topAdsKeywordDeleteDataSource;
        this.topAdsKeywordDeleteMapper = topAdsKeywordDeleteMapper;
    }

    @Override
    public Observable<Boolean> deleteAd(String id) {
        return topAdsKeywordDeleteDataSource.deleteAd(id)
                .map(topAdsKeywordDeleteMapper);
    }
}
