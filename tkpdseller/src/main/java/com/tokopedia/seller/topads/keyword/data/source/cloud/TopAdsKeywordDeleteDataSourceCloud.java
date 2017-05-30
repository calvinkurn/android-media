package com.tokopedia.seller.topads.keyword.data.source.cloud;

import com.tokopedia.seller.topads.keyword.data.source.cloud.api.KeywordApi;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 5/29/17.
 */

public class TopAdsKeywordDeleteDataSourceCloud {

    private final KeywordApi keywordApi;

    @Inject
    public TopAdsKeywordDeleteDataSourceCloud(KeywordApi keywordApi) {
        this.keywordApi = keywordApi;
    }


    public Observable<Object> deleteAd(String id) {
        return null;
    }
}
