package com.tokopedia.seller.product.data.repository;

import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.seller.product.data.source.cloud.YoutubeVideoLinkCloud;
import com.tokopedia.seller.product.domain.YoutubeVideoRepository;
import com.tokopedia.seller.product.domain.model.YoutubeVideoModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author normansyahputa on 4/11/17.
 */
@ActivityScope
public class YoutubeRepositoryImpl implements YoutubeVideoRepository {
    private final YoutubeVideoLinkCloud youtubeVideoLinkCloud;

    @Inject
    public YoutubeRepositoryImpl(YoutubeVideoLinkCloud youtubeVideoLinkCloud) {
        this.youtubeVideoLinkCloud = youtubeVideoLinkCloud;
    }

    @Override
    public Observable<YoutubeVideoModel> fetchYoutubeVideoInfo(String videoId, String keyId) {
        return youtubeVideoLinkCloud.fetchDataFromNetwork(videoId, keyId);
    }
}
