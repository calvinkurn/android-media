package com.tokopedia.gm.subscribe.domain.product.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.gm.subscribe.domain.product.GmSubscribeProductRepository;
import com.tokopedia.gm.subscribe.domain.product.exception.GmProductException;
import com.tokopedia.gm.subscribe.domain.product.model.GmProductDomainModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 2/3/17.
 */
public class GetGmCurrentSelectedProductUseCase extends UseCase<GmProductDomainModel> {
    public static final String PRODUCT_SELECTED_ID = "PRODUCT_SELECTED_ID";
    public static final int UNDEFINED_SELECTED = -1;

    protected final GmSubscribeProductRepository gmSubscribeProductRepository;

    @Inject
    public GetGmCurrentSelectedProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, GmSubscribeProductRepository gmSubscribeProductRepository) {
        super(threadExecutor, postExecutionThread);
        this.gmSubscribeProductRepository = gmSubscribeProductRepository;
    }

    public static RequestParams createRequestParams(int productId) {
        RequestParams params = RequestParams.create();
        params.putInt(PRODUCT_SELECTED_ID, productId);
        return params;
    }

    @Override
    public Observable<GmProductDomainModel> createObservable(RequestParams requestParams) {
        int currentSelectedProductId = requestParams.getInt(PRODUCT_SELECTED_ID, UNDEFINED_SELECTED);
        if (currentSelectedProductId == UNDEFINED_SELECTED) {
            throw new GmProductException("Wrong Selected Product Id");
        }
        return gmSubscribeProductRepository.getCurrentProductSelectedData(currentSelectedProductId);
    }
}
