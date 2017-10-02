package com.tokopedia.seller.product.manage.domain;


import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.common.constant.ProductNetworkConstant;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public class DeleteProductUseCase extends UseCase<Boolean> {
    private ActionProductManageRepository actionProductManageRepository;

    @Inject
    public DeleteProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                   ActionProductManageRepository actionProductManageRepository) {
        super(threadExecutor, postExecutionThread);
        this.actionProductManageRepository = actionProductManageRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return actionProductManageRepository.deleteProduct(requestParams.getParamsAllValueInString());
    }

    public static RequestParams createRequestParams(String productId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ProductNetworkConstant.PRODUCT_ID, productId);
        return requestParams;
    }
}
