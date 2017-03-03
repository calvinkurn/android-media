package com.tokopedia.seller.topads.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.domain.TopAdsGroupAdsRepository;
import com.tokopedia.seller.topads.domain.TopAdsProductAdsRepository;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailGroupDomainModel;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailProductDomainModel;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsSaveDetailGroupUseCase extends UseCase<TopAdsDetailGroupDomainModel> {

    private final TopAdsGroupAdsRepository topAdsGroupAdsRepository;

    public TopAdsSaveDetailGroupUseCase(ThreadExecutor threadExecutor,
                                        PostExecutionThread postExecutionThread,
                                        TopAdsGroupAdsRepository topAdsGroupAdsRepository) {
        super(threadExecutor, postExecutionThread);
        this.topAdsGroupAdsRepository = topAdsGroupAdsRepository;
    }

    @Override
    public Observable<TopAdsDetailGroupDomainModel> createObservable(RequestParams requestParams) {
        return null;
        // TODO
        // return topAdsGroupAdsRepository.saveDetail((TopAdsDetailGroupDomainModel) requestParams.getObject(TopAdsNetworkConstant.PARAM_AD));
    }

    public static RequestParams createRequestParams(TopAdsDetailProductDomainModel topAdsDetailProductDomainModel){
        RequestParams params = RequestParams.create();
        params.putObject(TopAdsNetworkConstant.PARAM_AD, topAdsDetailProductDomainModel);
        return params;
    }
}