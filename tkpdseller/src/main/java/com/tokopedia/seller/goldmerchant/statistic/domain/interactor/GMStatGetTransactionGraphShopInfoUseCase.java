package com.tokopedia.seller.goldmerchant.statistic.domain.interactor;

import com.tokopedia.core.base.domain.CompositeUseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.seller.product.edit.domain.interactor.AddProductShopInfoUseCase;
import com.tokopedia.seller.product.edit.domain.model.AddProductShopInfoDomainModel;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class GMStatGetTransactionGraphShopInfoUseCase extends CompositeUseCase<GMTransactionGraphMergeModel> {

    private GMStatGetTransactionGraphUseCase gmStatGetTransactionGraphUseCase;
    private AddProductShopInfoUseCase shopInfoUseCase;

    @Inject
    public GMStatGetTransactionGraphShopInfoUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            GMStatGetTransactionGraphUseCase gmStatGetTransactionGraphUseCase,
            AddProductShopInfoUseCase addProductShopInfoUseCase
    ) {
        super(threadExecutor, postExecutionThread);
        this.gmStatGetTransactionGraphUseCase = gmStatGetTransactionGraphUseCase;
        this.shopInfoUseCase = addProductShopInfoUseCase;
    }

    @Override
    public Observable<GMTransactionGraphMergeModel> createObservable(final RequestParams requestParams) {
        return shopInfoUseCase.createObservable(null).flatMap(new Func1<AddProductShopInfoDomainModel, Observable<GMTransactionGraphMergeModel>>() {
            @Override
            public Observable<GMTransactionGraphMergeModel> call(AddProductShopInfoDomainModel addProductShopInfoDomainModel) {
                return Observable.zip(
                        gmStatGetTransactionGraphUseCase.createObservable(requestParams),
                        Observable.just(addProductShopInfoDomainModel), new Func2<GMTransactionGraphMergeModel, AddProductShopInfoDomainModel, GMTransactionGraphMergeModel>() {
                            @Override
                            public GMTransactionGraphMergeModel call(GMTransactionGraphMergeModel gmTransactionGraphMergeModel, AddProductShopInfoDomainModel o) {
                                gmTransactionGraphMergeModel.setIsGoldMerchant(o.isGoldMerchant());
                                return gmTransactionGraphMergeModel;
                            }
                        });
            }
        });
    }
}
