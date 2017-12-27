package com.tokopedia.seller.reputation.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.reputation.domain.ReputationReviewRepository;
import com.tokopedia.seller.reputation.domain.model.SellerReputationDomain;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.tokopedia.seller.util.ShopNetworkController.RequestParamFactory.KEY_SHOP_ID;

/**
 * @author normansyahputa on 3/16/17.
 */
public class ReviewReputationUseCase extends UseCase<SellerReputationDomain> {

    private ReputationReviewRepository reputationReviewRepository;

    public ReviewReputationUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ReputationReviewRepository reputationReviewRepository) {
        super(threadExecutor, postExecutionThread);
        this.reputationReviewRepository = reputationReviewRepository;
    }

    @Override
    public Observable<SellerReputationDomain> createObservable(RequestParams requestParams) {
        return reputationReviewRepository.getReputationHistory(requestParams);
    }

    public Observable<SellerReputationDomain> createObservable(String shopId, Map<String, String> param) {
        return reputationReviewRepository.getReputationHistory(shopId, param);
    }

    public void execute(String shopId, Map<String, String> param, Subscriber<SellerReputationDomain> subscriber) {
        this.subscription = createObservable(shopId, param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public static class RequestParamFactory {
        public static final String KEY_REVIEW_REPUTATION_PARAM = "review_reputation_param";

        public static RequestParams generateRequestParam(String shopId,
                                                         Map<String, String> param) {
            RequestParams requestParams = RequestParams.create();
            requestParams.putString(KEY_SHOP_ID, shopId);
            requestParams.putObject(KEY_REVIEW_REPUTATION_PARAM, param);
            return requestParams;
        }
    }
}
