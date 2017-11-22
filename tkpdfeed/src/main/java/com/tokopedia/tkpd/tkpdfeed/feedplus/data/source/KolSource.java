package com.tokopedia.tkpd.tkpdfeed.feedplus.data.source;

import com.apollographql.android.rx.RxApollo;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloWatcher;
import com.tkpdfeed.feeds.FollowKol;
import com.tkpdfeed.feeds.LikeKolPost;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FollowKolMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.LikeKolMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.FollowKolDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.LikeKolDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.FollowKolPostUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.LikeKolPostUseCase;

import rx.Observable;

/**
 * @author by nisie on 11/3/17.
 */

public class KolSource {

    private final FollowKolMapper followKolMapper;
    private ApolloClient apolloClient;
    private LikeKolMapper likeKolMapper;

    public KolSource(ApolloClient apolloClient,
                     LikeKolMapper likeKolMapper,
                     FollowKolMapper followKolMapper) {
        this.apolloClient = apolloClient;
        this.likeKolMapper = likeKolMapper;
        this.followKolMapper = followKolMapper;
    }

    public Observable<LikeKolDomain> likeKolPost(RequestParams requestParams) {
        ApolloWatcher<LikeKolPost.Data> apolloWatcher = apolloClient.newCall(
                LikeKolPost.builder()
                        .idPost(requestParams.getInt(LikeKolPostUseCase.PARAM_ID, 0))
                        .action(requestParams.getInt(LikeKolPostUseCase.PARAM_ACTION, -1))
                        .build()).watcher();

        return RxApollo.from(apolloWatcher).map(likeKolMapper);
    }

    public Observable<FollowKolDomain> followKolPost(RequestParams requestParams) {
        ApolloWatcher<FollowKol.Data> apolloWatcher = apolloClient.newCall(
                FollowKol.builder()
                        .userID(requestParams.getInt(FollowKolPostUseCase.PARAM_USER_ID, 0))
                        .action(requestParams.getInt(FollowKolPostUseCase.PARAM_ACTION, -1))
                        .build()).watcher();

        return RxApollo.from(apolloWatcher).map(followKolMapper);
    }
}
