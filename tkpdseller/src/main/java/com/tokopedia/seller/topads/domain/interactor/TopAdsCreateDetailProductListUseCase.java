package com.tokopedia.seller.topads.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.domain.TopAdsProductAdsRepository;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailShopDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/3/17.
 */

public class TopAdsCreateDetailProductListUseCase extends UseCase<TopAdsDetailProductDomainModel> {

    private final TopAdsProductAdsRepository topAdsProductAdsRepository;

    public TopAdsCreateDetailProductListUseCase(ThreadExecutor threadExecutor,
                                                PostExecutionThread postExecutionThread,
                                                TopAdsProductAdsRepository topAdsProductAdsRepository) {
        super(threadExecutor, postExecutionThread);
        this.topAdsProductAdsRepository = topAdsProductAdsRepository;
    }

    @Override
    public Observable<TopAdsDetailProductDomainModel> createObservable(RequestParams requestParams) {
        return topAdsProductAdsRepository.saveDetailListProduct((List<TopAdsDetailProductDomainModel>) requestParams.getObject(TopAdsNetworkConstant.PARAM_AD));
    }

    public static RequestParams createRequestParams(List<TopAdsDetailProductDomainModel> topAdsDetailProductDomainModels){
        RequestParams params = RequestParams.create();
        params.putObject(TopAdsNetworkConstant.PARAM_AD, topAdsDetailProductDomainModels);
        return params;
    }
}
