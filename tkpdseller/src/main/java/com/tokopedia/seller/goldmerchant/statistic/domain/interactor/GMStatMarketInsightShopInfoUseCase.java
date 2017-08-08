package com.tokopedia.seller.goldmerchant.statistic.domain.interactor;

import com.tokopedia.core.base.domain.CompositeUseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.goldmerchant.statistic.domain.KeywordModel;
import com.tokopedia.seller.product.edit.domain.interactor.AddProductShopInfoUseCase;
import com.tokopedia.seller.product.edit.domain.model.AddProductShopInfoDomainModel;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by normansyahputa on 7/25/17.
 */

public class GMStatMarketInsightShopInfoUseCase extends CompositeUseCase<KeywordModel> {
    private GMStatMarketInsightUseCase gmStatMarketInsightUseCase;
    private AddProductShopInfoUseCase shopInfoUseCase;

    @Inject
    public GMStatMarketInsightShopInfoUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            GMStatMarketInsightUseCase gmStatMarketInsightUseCase,
            AddProductShopInfoUseCase shopInfoUseCase
    ) {
        super(threadExecutor, postExecutionThread);
        this.gmStatMarketInsightUseCase = gmStatMarketInsightUseCase;
        this.shopInfoUseCase = shopInfoUseCase;
    }

    @Override
    public Observable<KeywordModel> createObservable(final RequestParams requestParams) {
        return shopInfoUseCase.createObservable(null).flatMap(new Func1<AddProductShopInfoDomainModel, Observable<KeywordModel>>() {
            @Override
            public Observable<KeywordModel> call(AddProductShopInfoDomainModel addProductShopInfoDomainModel) {
                return Observable.zip(
                        gmStatMarketInsightUseCase.createObservable(requestParams),
                        Observable.just(addProductShopInfoDomainModel), new Func2<KeywordModel, AddProductShopInfoDomainModel, KeywordModel>() {
                            @Override
                            public KeywordModel call(KeywordModel keywordModel, AddProductShopInfoDomainModel o) {
                                keywordModel.setIsGoldMerchant(o.isGoldMerchant());
                                return keywordModel;
                            }
                        });
            }
        });
    }

}
