package com.tokopedia.seller.shopscore.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.shopscore.domain.ShopScoreRepository;
import com.tokopedia.seller.shopscore.domain.model.ShopScoreDetailDomainModel;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public class GetShopScoreDetailUseCase extends UseCase<List<ShopScoreDetailDomainModel>> {
    private final ShopScoreRepository shopScoreRepository;

    public GetShopScoreDetailUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, ShopScoreRepository shopScoreRepository) {
        super(threadExecutor, postExecutionThread);
        this.shopScoreRepository = shopScoreRepository;
    }

    @Override
    public Observable<List<ShopScoreDetailDomainModel>> createObservable(RequestParams requestParams) {
        return shopScoreRepository.getShopScoreDetail();
    }
}
