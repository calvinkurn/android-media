package com.tokopedia.seller.shopscore.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.shopscore.domain.ShopScoreRepository;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreMainDomainModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class GetShopScoreMainDataUseCase extends UseCase<ShopScoreMainDomainModel> {
    private final ShopScoreRepository shopScoreRepository;

    @Inject
    public GetShopScoreMainDataUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                       ShopScoreRepository shopScoreRepository) {
        super(threadExecutor, postExecutionThread);
        this.shopScoreRepository = shopScoreRepository;
    }

    @Override
    public Observable<ShopScoreMainDomainModel> createObservable(RequestParams requestParams) {
        return shopScoreRepository.getShopScoreSummary();
    }
}
