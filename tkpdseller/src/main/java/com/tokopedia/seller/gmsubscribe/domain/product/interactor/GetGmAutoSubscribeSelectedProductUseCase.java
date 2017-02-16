package com.tokopedia.seller.gmsubscribe.domain.product.interactor;

import com.tokopedia.seller.common.domain.RequestParams;
import com.tokopedia.seller.common.domain.UseCase;
import com.tokopedia.seller.common.domain.executor.PostExecutionThread;
import com.tokopedia.seller.common.domain.executor.ThreadExecutor;
import com.tokopedia.seller.gmsubscribe.domain.product.GmSubscribeProductRepository;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GmAutoSubscribeDomainModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GetGmAutoSubscribeSelectedProductUseCase extends UseCase<GmAutoSubscribeDomainModel> {

    public static final String AUTOSUBSCRIBE_PRODUCT_ID = "AUTOSUBSCRIBE_PRODUCT_ID";
    public static final String CURRENT_PRODUCT_ID = "CURRENT_PRODUCT_ID";
    public static final int UNDEFINED_SELECTED = -1;
    private final GmSubscribeProductRepository gmSubscribeProductRepository;

    public GetGmAutoSubscribeSelectedProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, GmSubscribeProductRepository gmSubscribeProductRepository) {
        super(threadExecutor, postExecutionThread);
        this.gmSubscribeProductRepository = gmSubscribeProductRepository;
    }

    public static RequestParams createRequestParams(int currentProductId, int autoSubscribeProductId) {
        RequestParams params = RequestParams.create();
        params.putInt(AUTOSUBSCRIBE_PRODUCT_ID, autoSubscribeProductId);
        params.putInt(CURRENT_PRODUCT_ID, currentProductId);
        return params;
    }

    @Override
    public Observable<GmAutoSubscribeDomainModel> createObservable(RequestParams requestParams) {
        return gmSubscribeProductRepository.getExtendProductSelectedData(
                requestParams.getInt(AUTOSUBSCRIBE_PRODUCT_ID, UNDEFINED_SELECTED),
                requestParams.getInt(CURRENT_PRODUCT_ID, UNDEFINED_SELECTED));
    }
}
