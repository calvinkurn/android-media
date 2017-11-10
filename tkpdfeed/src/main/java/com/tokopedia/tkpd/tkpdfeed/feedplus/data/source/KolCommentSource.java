package com.tokopedia.tkpd.tkpdfeed.feedplus.data.source;

import com.apollographql.android.rx.RxApollo;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloWatcher;
import com.tkpdfeed.feeds.CreateKolComment;
import com.tkpdfeed.feeds.DeleteKolComment;
import com.tkpdfeed.feeds.GetKolComments;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.KolCommentMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.KolDeleteCommentMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.KolSendCommentMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.DeleteKolCommentDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.SendKolCommentDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.DeleteKolCommentUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetKolCommentsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.SendKolCommentUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolComments;

import rx.Observable;

/**
 * @author by nisie on 10/31/17.
 */

public class KolCommentSource {

    private final ApolloClient apolloClient;
    private final KolCommentMapper kolCommentMapper;
    private final KolSendCommentMapper kolSendCommentMapper;
    private final KolDeleteCommentMapper kolDeleteCommentMapper;


    public KolCommentSource(ApolloClient apolloClient, KolCommentMapper kolCommentMapper,
                            KolSendCommentMapper kolSendCommentMapper,
                            KolDeleteCommentMapper kolDeleteCommentMapper) {
        this.apolloClient = apolloClient;
        this.kolCommentMapper = kolCommentMapper;
        this.kolSendCommentMapper = kolSendCommentMapper;
        this.kolDeleteCommentMapper = kolDeleteCommentMapper;
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

    public Observable<SendKolCommentDomain> sendComment(RequestParams requestParams) {
        ApolloWatcher<CreateKolComment.Data> apolloWatcher = apolloClient.newCall(
                CreateKolComment.builder()
                        .comment(requestParams.getString(SendKolCommentUseCase.PARAM_COMMENT, ""))
                        .idPost(requestParams.getInt(SendKolCommentUseCase.PARAM_ID, 0))
                        .build()).watcher();
        return RxApollo.from(apolloWatcher).map(kolSendCommentMapper);
    }

    public Observable<DeleteKolCommentDomain> deleteKolComment(RequestParams requestParams) {
        ApolloWatcher<DeleteKolComment.Data> apolloWatcher = apolloClient.newCall(
                DeleteKolComment.builder()
                        .idComment(requestParams.getInt(DeleteKolCommentUseCase.PARAM_ID, 0))
                        .build()).watcher();

        return RxApollo.from(apolloWatcher).map(kolDeleteCommentMapper);
    }
}
