package com.tokopedia.seller.topads.keyword.domain;

import rx.Observable;

/**
 * Created by zulfikarrahman on 5/29/17.
 */

public interface TopAdsKeywordDeleteRepository {
    Observable<Boolean> deleteAd(String id);
}
