package com.tokopedia.topads.keyword.domain.interactor;

import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.keyword.data.mapper.TopAdsKeywordActionBulkMapperToDomain;
import com.tokopedia.topads.keyword.domain.TopAdsKeywordActionBulkRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 5/29/17.
 */

public class TopAdsKeywordActionBulkUseCase extends UseCase<Boolean> {

    private final TopAdsKeywordActionBulkRepository topAdsKeywordActionBulkRepository;
    private final TopAdsKeywordActionBulkMapperToDomain topAdsKeywordActionBulkMapperToDomain;

    @Inject
    public TopAdsKeywordActionBulkUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                          TopAdsKeywordActionBulkRepository topAdsKeywordActionBulkRepository,
                                          TopAdsKeywordActionBulkMapperToDomain topAdsKeywordActionBulkMapperToDomain) {
        super(threadExecutor, postExecutionThread);
        this.topAdsKeywordActionBulkRepository = topAdsKeywordActionBulkRepository;
        this.topAdsKeywordActionBulkMapperToDomain = topAdsKeywordActionBulkMapperToDomain;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return topAdsKeywordActionBulkRepository.actionBulk(requestParams)
                .map(topAdsKeywordActionBulkMapperToDomain);
    }

    public static RequestParams createRequestParams(String adId, String groupId, String shopId, String action){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TopAdsNetworkConstant.PARAM_KEYWORD_AD_ID, adId);
        requestParams.putString(TopAdsNetworkConstant.PARAM_GROUP_ID, groupId);
        requestParams.putString(TopAdsNetworkConstant.PARAM_SHOP_ID, shopId);
        requestParams.putString(TopAdsNetworkConstant.PARAM_ACTION, action);
        return requestParams;
    }
}
