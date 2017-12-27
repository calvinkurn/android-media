package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.topads.dashboard.domain.TopAdsEtalaseListRepository;
import com.tokopedia.topads.dashboard.data.model.data.Etalase;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsEtalaseListUseCase extends UseCase<List<Etalase>> {

    public static final String SHOP_ID = "shop_id";
    private final TopAdsEtalaseListRepository topAdsEtalaseListRepository;

    public TopAdsEtalaseListUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                    TopAdsEtalaseListRepository topAdsEtalaseListRepository) {
        super(threadExecutor, postExecutionThread);
        this.topAdsEtalaseListRepository = topAdsEtalaseListRepository;
    }

    @Override
    public Observable<List<Etalase>> createObservable(RequestParams requestParams) {
        return topAdsEtalaseListRepository.getEtalaseList(requestParams.getString(SHOP_ID, ""));
    }

    public static RequestParams createRequestParams(String shopId){
        RequestParams params = RequestParams.create();
        params.putString(SHOP_ID, shopId);
        return params;
    }
}
