package com.tokopedia.seller.reputation.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.reputation.domain.ReputationReviewRepository;
import com.tokopedia.seller.util.ShopNetworkController;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by normansyahputa on 3/17/17.
 */

@Deprecated
public class ShopInfoUseCase extends UseCase<ShopModel> {
    private ReputationReviewRepository reputationReviewRepository;

    public ShopInfoUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ReputationReviewRepository reputationReviewRepository) {
        super(threadExecutor, postExecutionThread);
        this.reputationReviewRepository = reputationReviewRepository;
    }

    @Override
    public Observable<ShopModel> createObservable(RequestParams requestParams) {
        return reputationReviewRepository.getShopInfo(requestParams);
    }

    public void execute(String userid, String deviceId,
                        ShopNetworkController.ShopInfoParam shopInfoParam,
                        Subscriber<ShopModel> subscriber) {
        this.subscription = createObservable(userid, deviceId, shopInfoParam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public Observable<ShopModel> createObservable(String userid, String deviceId, ShopNetworkController.ShopInfoParam shopInfoParam) {
        return reputationReviewRepository.getShopInfo(userid, deviceId, shopInfoParam);
    }
}
