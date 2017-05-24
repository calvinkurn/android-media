package com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.source;

import android.content.Context;

import com.apollographql.android.rx.RxApollo;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloWatcher;
import com.tkpdfeed.feeds.FeedDetail;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedDetailListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.feeddetail.DataFeedDetailDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase.GetFeedsDetailUseCase;

import java.util.List;

import rx.Observable;

/**
 * @author by nisie on 5/24/17.
 */

public class CloudFeedDetailDataSource {

    private static final int LIMIT_DETAIL = 30;
    private ApolloClient apolloClient;
    private Context context;
    private FeedDetailListMapper feedDetailListMapper;

    public CloudFeedDetailDataSource(Context context,
                                     ApolloClient apolloClient,
                                     FeedDetailListMapper feedDetailListMapper) {

        this.apolloClient = apolloClient;
        this.context = context;
        this.feedDetailListMapper = feedDetailListMapper;
    }

    public Observable<List<DataFeedDetailDomain>> getFeedsDetailList(RequestParams requestParams) {
        ApolloWatcher<FeedDetail.Data> apolloWatcher = apolloClient.newCall(
                FeedDetail.builder()
                .detailID(getDetailId(requestParams))
                .limitDetail(LIMIT_DETAIL)
                .pageDetail(getPaging(requestParams))
                .build()).watcher();

        return RxApollo.from(apolloWatcher).map(feedDetailListMapper);
    }

    private int getPaging(RequestParams requestParams) {
        return requestParams.getInt(GetFeedsDetailUseCase.PARAM_PAGE,1);
    }

    private String getDetailId(RequestParams requestParams) {
        return requestParams.getString(GetFeedsDetailUseCase.PARAM_DETAIL_ID,"");
    }


}
