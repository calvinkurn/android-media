package com.tokopedia.seller.reputation.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.reputation.speed.SpeedReputation;
import com.tokopedia.seller.reputation.domain.ReputationRepository;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 2/13/18.
 */

public class SpeedReputationUseCase extends UseCase<SpeedReputation> {

    public static final String KEY_SHOP_ID = "shop_id";
    private ReputationRepository reputationRepository;

    @Inject
    public SpeedReputationUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, ReputationRepository reputationRepository) {
        super(threadExecutor, postExecutionThread);
        this.reputationRepository = reputationRepository;
    }

    @Override
    public Observable<SpeedReputation> createObservable(RequestParams requestParams) {
        return reputationRepository.getReputationSpeed(requestParams);
    }

    public static RequestParams generateRequestParam(String shopId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(KEY_SHOP_ID, shopId);
        return requestParams;
    }
}
