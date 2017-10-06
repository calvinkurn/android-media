package com.tokopedia.seller.product.manage.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.product.common.constant.ProductNetworkConstant;
import com.tokopedia.seller.product.manage.constant.ProductManageConstant;
import com.tokopedia.seller.product.manage.domain.model.MultipleDeleteProductModel;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 10/2/17.
 */

public class MultipleDeleteProductUseCase extends UseCase<MultipleDeleteProductModel> {

    private final ActionProductManageRepository actionProductManageRepository;

    @Inject
    public MultipleDeleteProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                        ActionProductManageRepository actionProductManageRepository) {
        super(threadExecutor, postExecutionThread);
        this.actionProductManageRepository = actionProductManageRepository;
    }

    @Override
    public Observable<MultipleDeleteProductModel> createObservable(RequestParams requestParams) {
        List<String> productIds = (List<String>) requestParams.getObject(ProductManageConstant.LIST_ID_DELETED);
        return Observable.from(productIds)
                .flatMap(new Func1<String, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(String productId) {
                        RequestParams requestParams = RequestParams.create();
                        requestParams.putString(ProductNetworkConstant.PRODUCT_ID, productId);
                        return actionProductManageRepository.deleteProduct(requestParams.getParamsAllValueInString());
                    }
                }).toList()
                .flatMap(new Func1<List<Boolean>, Observable<MultipleDeleteProductModel>>() {
                    @Override
                    public Observable<MultipleDeleteProductModel> call(List<Boolean> booleen) {
                        boolean isError = false;
                        int counterIsSuccess = 0;
                        int counterIsError = 0;
                        for(Boolean isSuccess : booleen){
                            if(!isSuccess){
                                counterIsSuccess += 1;
                                isError = true;
                            }else{
                                counterIsError += 1;
                            }
                        }
                        MultipleDeleteProductModel multipleDeleteProductModel = new MultipleDeleteProductModel();
                        multipleDeleteProductModel.setCountOfError(counterIsError);
                        multipleDeleteProductModel.setCountOfSuccess(counterIsSuccess);
                        multipleDeleteProductModel.setSuccess(!isError);
                        return Observable.just(multipleDeleteProductModel);
                    }
                });
    }

    public static RequestParams createRequestParams(List<String> productIds){
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(ProductManageConstant.LIST_ID_DELETED, productIds);
        return requestParams;
    }
}
