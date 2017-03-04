package com.tokopedia.seller.topads.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.domain.TopAdsGroupAdsRepository;
import com.tokopedia.seller.topads.domain.TopAdsProductAdsRepository;
import com.tokopedia.seller.topads.domain.TopAdsShopAdsRepository;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailGroupDomainModel;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailShopDomainModel;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsGetDetailGroupUseCase extends UseCase<TopAdsDetailGroupDomainModel> {

    private final TopAdsGroupAdsRepository topAdsGroupAdsRepository;

    public TopAdsGetDetailGroupUseCase(ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread,
                                       TopAdsGroupAdsRepository topAdsGroupAdsRepository) {
        super(threadExecutor, postExecutionThread);
        this.topAdsGroupAdsRepository = topAdsGroupAdsRepository;
    }

    @Override
    public Observable<TopAdsDetailGroupDomainModel> createObservable(RequestParams requestParams) {
        //return null
        // TODO
        return null;
        // return topAdsGroupAdsRepository.getDetail(requestParams.getString(TopAdsNetworkConstant.PARAM_AD_ID, ""));
    }

    public static RequestParams createRequestParams(String adId){
        RequestParams params = RequestParams.create();
        params.putString(TopAdsNetworkConstant.PARAM_AD_ID, adId);
        return params;
    }
}
