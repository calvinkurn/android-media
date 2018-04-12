package com.tokopedia.seller.product.picker.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.picker.domain.GetProductSellingPromoTopAdsRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hadi.putra on 12/04/18.
 */

public class GetProductCheckPromoAdsUseCase extends UseCase<String> {
    private static final String PARAM_ITEM_ID = "item_id";
    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_SHOP_ID = "shop_id";

    private final GetProductSellingPromoTopAdsRepository getProductSellingPromoTopAdsRepository;

    @Inject
    public GetProductCheckPromoAdsUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                          GetProductSellingPromoTopAdsRepository getProductSellingPromoTopAdsRepository) {
        super(threadExecutor, postExecutionThread);
        this.getProductSellingPromoTopAdsRepository = getProductSellingPromoTopAdsRepository;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        return getProductSellingPromoTopAdsRepository.getProductPromoTopAds(requestParams);
    }

    public static RequestParams createRequestParams(String shopId, String itemId, String userId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_SHOP_ID, shopId);
        requestParams.putString(PARAM_ITEM_ID, itemId);
        requestParams.putString(PARAM_USER_ID, userId);
        return requestParams;
    }
}
