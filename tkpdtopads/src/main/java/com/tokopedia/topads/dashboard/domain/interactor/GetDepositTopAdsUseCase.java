package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.domain.GetDepositTopAdsRepository;

import rx.Observable;

/**
 * Created by zulfikarrahman on 9/18/17.
 */

public class GetDepositTopAdsUseCase extends UseCase<DataDeposit> {

    private GetDepositTopAdsRepository getDepositTopAdsRepository;

    public GetDepositTopAdsUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, GetDepositTopAdsRepository getDepositTopAdsRepository) {
        super(threadExecutor, postExecutionThread);
        this.getDepositTopAdsRepository = getDepositTopAdsRepository;
    }

    @Override
    public Observable<DataDeposit> createObservable(RequestParams requestParams) {
        return getDepositTopAdsRepository.getDeposit(requestParams.getParamsAllValueInString());
    }

    public static RequestParams createRequestParams(String shopId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TopAdsNetworkConstant.PARAM_SHOP_ID, shopId);
        return requestParams;
    }
}
