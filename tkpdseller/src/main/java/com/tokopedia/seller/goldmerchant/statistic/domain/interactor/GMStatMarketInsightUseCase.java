package com.tokopedia.seller.goldmerchant.statistic.domain.interactor;

import com.tokopedia.core.base.domain.CompositeUseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.goldmerchant.statistic.domain.KeywordModel;
import com.tokopedia.seller.goldmerchant.statistic.domain.mapper.GMStatMarketInsightMapper;
import com.tokopedia.seller.product.domain.interactor.AddProductShopInfoUseCase;
import com.tokopedia.seller.product.domain.model.AddProductShopInfoDomainModel;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by normansyahputa on 7/25/17.
 */

public class GMStatMarketInsightUseCase extends CompositeUseCase<KeywordModel> {
    private GMStatGetShopCategoryUseCase categoryUseCase;
    private GMStatMarketInsightMapper marketInsightMapper;
    private AddProductShopInfoUseCase shopInfoUseCase;

    @Inject
    public GMStatMarketInsightUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            GMStatGetShopCategoryUseCase categoryUseCase,
            GMStatMarketInsightMapper marketInsightMapper,
            AddProductShopInfoUseCase shopInfoUseCase
    ) {
        super(threadExecutor, postExecutionThread);
        this.categoryUseCase = categoryUseCase;
        this.marketInsightMapper = marketInsightMapper;
        this.shopInfoUseCase = shopInfoUseCase;
    }

    @Override
    public Observable<KeywordModel> createObservable(final RequestParams requestParams) {
        return shopInfoUseCase.createObservable(null).flatMap(new Func1<AddProductShopInfoDomainModel, Observable<KeywordModel>>() {
            @Override
            public Observable<KeywordModel> call(AddProductShopInfoDomainModel addProductShopInfoDomainModel) {
                Observable<KeywordModel> test = test(requestParams);
                return Observable.zip(test, Observable.just(addProductShopInfoDomainModel), new Func2<KeywordModel, AddProductShopInfoDomainModel, KeywordModel>() {
                    @Override
                    public KeywordModel call(KeywordModel keywordModel, AddProductShopInfoDomainModel o) {
                        keywordModel.setIsGoldMerchant(o.isGoldMerchant());
                        return keywordModel;
                    }
                });
            }
        });
    }

    private Observable<KeywordModel> test(RequestParams requestParams) {
        return categoryUseCase.createObservable(requestParams).flatMap(marketInsightMapper);
    }


}
