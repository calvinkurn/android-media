package com.tokopedia.seller.topads.dashboard.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.dashboard.domain.TopAdsShopAdsRepository;
import com.tokopedia.seller.topads.dashboard.domain.model.TopAdsDetailShopDomainModel;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsSaveDetailShopUseCase extends UseCase<TopAdsDetailShopDomainModel> {

    protected final TopAdsShopAdsRepository topAdsShopAdsRepository;

    public TopAdsSaveDetailShopUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, TopAdsShopAdsRepository topAdsShopAdsRepository) {
        super(threadExecutor, postExecutionThread);
        this.topAdsShopAdsRepository = topAdsShopAdsRepository;
    }

    @Override
    public Observable<TopAdsDetailShopDomainModel> createObservable(RequestParams requestParams) {
        return topAdsShopAdsRepository.saveDetail((TopAdsDetailShopDomainModel) requestParams.getObject(TopAdsNetworkConstant.PARAM_AD));
    }

    public static RequestParams createRequestParams(TopAdsDetailShopDomainModel topAdsDetailShopDomainModel){
        RequestParams params = RequestParams.create();
        params.putObject(TopAdsNetworkConstant.PARAM_AD, topAdsDetailShopDomainModel);
        return params;
    }
}