package com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.FavoriteShopDomain;

import rx.Observable;

/**
 * @author by nisie on 9/26/17.
 */

public class FavoriteShopUseCase extends UseCase<FavoriteShopDomain> {

    private static final String PARAM_SHOP_ID = "shop_id";
    private static final String PARAM_SOURCE = "src";

    private ReputationRepository reputationRepository;

    public FavoriteShopUseCase(ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread,
                               ReputationRepository reputationRepository) {
        super(threadExecutor, postExecutionThread);
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<FavoriteShopDomain> createObservable(RequestParams requestParams) {
        return reputationRepository.favoriteShop(requestParams);
    }

    public static RequestParams getParam(int shopId, String src) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_SHOP_ID, String.valueOf(shopId));
        params.putString(PARAM_SOURCE, src);
        return params;
    }
}
