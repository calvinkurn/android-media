package com.tokopedia.seller.topads.keyword.data.source;

import com.tokopedia.seller.topads.keyword.data.source.cloud.TopAdsKeywordDeleteDataSourceCloud;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 5/29/17.
 */

public class TopAdsKeywordDeleteDataSource {
    private final TopAdsKeywordDeleteDataSourceCloud topAdsKeywordDeleteDataSourceCloud;

    @Inject
    public TopAdsKeywordDeleteDataSource(TopAdsKeywordDeleteDataSourceCloud topAdsKeywordDeleteDataSourceCloud) {
        this.topAdsKeywordDeleteDataSourceCloud = topAdsKeywordDeleteDataSourceCloud;
    }

    public Observable<Object> deleteAd(String id) {
        return topAdsKeywordDeleteDataSourceCloud.deleteAd(id);
    }
}
