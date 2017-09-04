package com.tokopedia.seller.reputation.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.util.ShopNetworkController;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.tokopedia.seller.reputation.domain.interactor.ReviewReputationMergeUseCase.RequestParamFactory.KEY_REVIEW_REPUTATION_CONTAINER_PARAM;
import static com.tokopedia.seller.reputation.domain.interactor.ReviewReputationMergeUseCase.RequestParamFactory.KEY_SHOP_INFO_CONTAINER_PARAM;

/**
 * @author normansyahputa on 3/17/17.
 */

public class ReviewReputationMergeUseCase extends UseCase<List<Object>> {
    private final ReviewReputationUseCase reviewReputationUseCase;
    private final ShopInfoUseCase shopInfoUseCase;


    public ReviewReputationMergeUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ReviewReputationUseCase reviewReputationUseCase,
            ShopInfoUseCase shopInfoUseCase) {
        super(threadExecutor, postExecutionThread);
        this.reviewReputationUseCase = reviewReputationUseCase;
        this.shopInfoUseCase = shopInfoUseCase;
    }


    public Observable<List<Object>> createObservable(
            String userid, String deviceId,
            ShopNetworkController.ShopInfoParam shopInfoParam,
            String shopId, Map<String, String> param) {
        return Observable.concat(
                shopInfoUseCase.createObservable(userid, deviceId, shopInfoParam),
                reviewReputationUseCase.createObservable(shopId, param)
        ).toList();
    }

    public void execute(String userid, String deviceId,
                        ShopNetworkController.ShopInfoParam shopInfoParam,
                        String shopId, Map<String, String> param, Subscriber<List<Object>> subscriber) {
        createObservable(userid, deviceId, shopInfoParam, shopId, param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }


    @Override
    public Observable<List<Object>> createObservable(RequestParams requestParams) {
        return Observable.concat(
                shopInfoUseCase.createObservable((RequestParams) requestParams.getObject(KEY_SHOP_INFO_CONTAINER_PARAM)),
                reviewReputationUseCase.createObservable((RequestParams) requestParams.getObject(KEY_REVIEW_REPUTATION_CONTAINER_PARAM))
        ).toList();
    }

    public static class RequestParamFactory {
        public static final String KEY_REVIEW_REPUTATION_CONTAINER_PARAM = "KEY_REVIEW_REPUTATION_CONTAINER_PARAM";
        public static final String KEY_SHOP_INFO_CONTAINER_PARAM = "KEY_SHOP_INFO_CONTAINER_PARAM";

        public static RequestParams generateRequestParam(RequestParams reviewParam, RequestParams shopInfoParam) {
            RequestParams requestParams = RequestParams.create();
            requestParams.putObject(KEY_REVIEW_REPUTATION_CONTAINER_PARAM, reviewParam);
            requestParams.putObject(KEY_SHOP_INFO_CONTAINER_PARAM, shopInfoParam);
            return requestParams;
        }
    }
}
