package com.tokopedia.seller.topads.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.topads.domain.TopAdsSearchProductRepository;
import com.tokopedia.seller.topads.domain.model.ProductListDomain;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author normansyahputa on 2/20/17.
 */
public class TopAdsDefaultParamUseCase extends UseCase<ProductListDomain> {
    private TopAdsSearchProductRepository topAdsSearchProductRepository;

    public TopAdsDefaultParamUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            TopAdsSearchProductRepository topAdsSearchProductRepository) {
        super(threadExecutor, postExecutionThread);
        this.topAdsSearchProductRepository = topAdsSearchProductRepository;
    }

    @Override
    public Observable<ProductListDomain> createObservable(RequestParams requestParams) {
        throw new RuntimeException("this is didn't use in here !!");
    }

    public Observable<ProductListDomain> createObservable(Map<String, String> param) {
        return topAdsSearchProductRepository.searchProduct(param);
    }

    public void execute(Map<String, String> param, Subscriber<ProductListDomain> subscriber) {
        compositeSubscription.add( createObservable(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber) );
    }

    @Override
    public void execute(RequestParams requestParams, Subscriber<ProductListDomain> subscriber) {
        throw new RuntimeException("this is didn't use in here !!");
    }
}
