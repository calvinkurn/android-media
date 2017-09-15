package com.tokopedia.seller.topads.dashboard.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.dashboard.domain.TopAdsProductAdsRepository;
import com.tokopedia.seller.topads.dashboard.domain.model.TopAdsDetailProductDomainModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsGetDetailProductUseCase extends UseCase<TopAdsDetailProductDomainModel> {

    private final TopAdsProductAdsRepository topAdsProductAdsRepository;

    @Inject
    public TopAdsGetDetailProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, TopAdsProductAdsRepository topAdsProductAdsRepository) {
        super(threadExecutor, postExecutionThread);
        this.topAdsProductAdsRepository = topAdsProductAdsRepository;
    }

    @Override
    public Observable<TopAdsDetailProductDomainModel> createObservable(RequestParams requestParams) {
        return topAdsProductAdsRepository.getDetail(requestParams.getString(TopAdsNetworkConstant.PARAM_AD_ID, ""));
    }

    public static RequestParams createRequestParams(String adId){
        RequestParams params = RequestParams.create();
        params.putString(TopAdsNetworkConstant.PARAM_AD_ID, adId);
        return params;
    }
}
