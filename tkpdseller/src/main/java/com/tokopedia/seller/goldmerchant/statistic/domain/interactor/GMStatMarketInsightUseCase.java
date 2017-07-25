package com.tokopedia.seller.goldmerchant.statistic.domain.interactor;

import com.tokopedia.core.base.domain.CompositeUseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.goldmerchant.statistic.domain.KeywordModel;
import com.tokopedia.seller.goldmerchant.statistic.domain.mapper.GMStatMarketInsightMapper;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 7/25/17.
 */

public class GMStatMarketInsightUseCase extends CompositeUseCase<KeywordModel> {
    private GMStatGetShopCategoryUseCase categoryUseCase;
    private GMStatMarketInsightMapper marketInsightMapper;

    @Inject
    public GMStatMarketInsightUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            GMStatGetShopCategoryUseCase categoryUseCase,
            GMStatMarketInsightMapper marketInsightMapper
    ) {
        super(threadExecutor, postExecutionThread);
        this.categoryUseCase = categoryUseCase;
        this.marketInsightMapper = marketInsightMapper;
    }

    @Override
    public Observable<KeywordModel> createObservable(RequestParams requestParams) {
        return categoryUseCase.createObservable(requestParams).flatMap(marketInsightMapper);
    }
}
