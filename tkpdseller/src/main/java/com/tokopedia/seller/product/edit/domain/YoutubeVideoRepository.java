package com.tokopedia.seller.product.edit.domain;

import com.tokopedia.seller.product.edit.domain.model.YoutubeVideoModel;

import rx.Observable;

/**
 * @author normansyahputa on 4/11/17.
 */
public interface YoutubeVideoRepository {
    Observable<YoutubeVideoModel> fetchYoutubeVideoInfo(String videoId, String keyId);
}
