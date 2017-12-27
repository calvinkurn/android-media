package com.tokopedia.seller.product.edit.data.repository;

import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.seller.product.edit.data.source.YoutubeVideoLinkDataSource;
import com.tokopedia.seller.product.edit.domain.YoutubeVideoRepository;
import com.tokopedia.seller.product.edit.domain.model.YoutubeVideoModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author normansyahputa on 4/11/17.
 */
@ActivityScope
public class YoutubeRepositoryImpl implements YoutubeVideoRepository {
    private final YoutubeVideoLinkDataSource youtubeVideoLinkDataSource;

    @Inject
    public YoutubeRepositoryImpl(YoutubeVideoLinkDataSource youtubeVideoLinkDataSource) {
        this.youtubeVideoLinkDataSource = youtubeVideoLinkDataSource;
    }

    @Override
    public Observable<YoutubeVideoModel> fetchYoutubeVideoInfo(String videoId, String keyId) {
        return youtubeVideoLinkDataSource.fetchDataFromNetwork(videoId, keyId);
    }
}
