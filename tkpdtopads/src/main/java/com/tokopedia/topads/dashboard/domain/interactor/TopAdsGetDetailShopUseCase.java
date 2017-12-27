package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.domain.TopAdsShopAdsRepository;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailShopDomainModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsGetDetailShopUseCase extends UseCase<TopAdsDetailShopDomainModel> {

    private final TopAdsShopAdsRepository topAdsShopAdsRepository;

    @Inject
    public TopAdsGetDetailShopUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, TopAdsShopAdsRepository topAdsShopAdsRepository) {
        super(threadExecutor, postExecutionThread);
        this.topAdsShopAdsRepository = topAdsShopAdsRepository;
    }

    @Override
    public Observable<TopAdsDetailShopDomainModel> createObservable(RequestParams requestParams) {
        return topAdsShopAdsRepository.getDetail(requestParams.getString(TopAdsNetworkConstant.PARAM_AD_ID, ""));
    }

    public static RequestParams createRequestParams(String adId){
        RequestParams params = RequestParams.create();
        params.putString(TopAdsNetworkConstant.PARAM_AD_ID, adId);
        return params;
    }
}
