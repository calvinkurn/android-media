package com.tokopedia.tkpd.tkpdfeed.feedplus.data.source;

import android.content.Context;

import com.apollographql.android.rx.RxApollo;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloWatcher;
import com.tkpdfeed.feeds.FeedDetail;
import com.tkpdfeed.feeds.GetKolComments;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedDetailListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.KolCommentMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetFeedsDetailUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetKolCommentsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolComments;

import rx.Observable;

/**
 * @author by nisie on 10/31/17.
 */

public class KolCommentSource {

    private ApolloClient apolloClient;
    private KolCommentMapper kolCommentMapper;

    public KolCommentSource(ApolloClient apolloClient, KolCommentMapper kolCommentMapper) {
        this.apolloClient = apolloClient;
        this.kolCommentMapper = kolCommentMapper;
    }

    public Observable<KolComments> getComments(RequestParams requestParams) {
        ApolloWatcher<GetKolComments.Data> apolloWatcher = apolloClient.newCall(
                GetKolComments.builder()
                        .idPost(requestParams.getInt(GetKolCommentsUseCase.PARAM_ID, 0))
                        .cursor(requestParams.getString(GetKolCommentsUseCase.PARAM_CURSOR, ""))
                        .limit(requestParams.getInt(GetKolCommentsUseCase.PARAM_LIMIT,
                                GetKolCommentsUseCase.DEFAULT_LIMIT))
                        .build()).watcher();

        return RxApollo.from(apolloWatcher).map(kolCommentMapper);
    }
}
