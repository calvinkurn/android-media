package com.tokopedia.seller.topads.keyword.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.keyword.domain.TopAdsKeywordDeleteRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 5/29/17.
 */

public class TopAdsKeywordDeleteUseCase extends UseCase<Boolean> {

    private final TopAdsKeywordDeleteRepository topAdsKeywordDeleteRepository;

    @Inject
    public TopAdsKeywordDeleteUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                      TopAdsKeywordDeleteRepository topAdsKeywordDeleteRepository) {
        super(threadExecutor, postExecutionThread);
        this.topAdsKeywordDeleteRepository = topAdsKeywordDeleteRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return topAdsKeywordDeleteRepository.deleteAd(requestParams.getString(TopAdsNetworkConstant.PARAM_AD_ID, ""));
    }

    public static RequestParams createRequestParams(String adId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TopAdsNetworkConstant.PARAM_AD_ID, adId);
        return requestParams;
    }
}
