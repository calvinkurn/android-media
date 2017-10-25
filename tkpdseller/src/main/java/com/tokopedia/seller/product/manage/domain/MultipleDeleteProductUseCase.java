package com.tokopedia.seller.product.manage.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.common.constant.ProductNetworkConstant;
import com.tokopedia.seller.product.manage.constant.ProductManageConstant;
import com.tokopedia.seller.product.manage.domain.model.MultipleDeleteProductModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
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
        final List<String> productIdList = (List<String>) requestParams.getObject(ProductManageConstant.LIST_ID_DELETED);
        return Observable.from(productIdList)
                .flatMap(new Func1<String, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(final String productId) {
                        RequestParams requestParams = RequestParams.create();
                        requestParams.putString(ProductNetworkConstant.PRODUCT_ID, productId);
                        return actionProductManageRepository.deleteProduct(requestParams.getParamsAllValueInString()).onErrorResumeNext(new Func1<Throwable, Observable<? extends Boolean>>() {
                            @Override
                            public Observable<? extends Boolean> call(Throwable throwable) {
                                return Observable.just(false);
                            }
                        });
                    }
                }).toList()
                .flatMap(new Func1<List<Boolean>, Observable<MultipleDeleteProductModel>>() {
                    @Override
                    public Observable<MultipleDeleteProductModel> call(List<Boolean> booleen) {
                        List<String> productIdDeletedList = new ArrayList<>();
                        List<String> productIdFailToDeleteList = new ArrayList<>();
                        int i = 0;
                        for (Boolean isSuccess : booleen) {
                            if (isSuccess) {
                                productIdDeletedList.add(productIdList.get(i));
                            } else {
                                productIdFailToDeleteList.add(productIdList.get(i));
                            }
                            i++;
                        }
                        MultipleDeleteProductModel multipleDeleteProductModel = new MultipleDeleteProductModel();
                        multipleDeleteProductModel.setProductIdDeletedList(productIdDeletedList);
                        multipleDeleteProductModel.setProductIdFailedToDeleteList(productIdFailToDeleteList);
                        return Observable.just(multipleDeleteProductModel);
                    }
                });
    }

    public static RequestParams createRequestParams(List<String> productIds) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(ProductManageConstant.LIST_ID_DELETED, productIds);
        return requestParams;
    }
}
