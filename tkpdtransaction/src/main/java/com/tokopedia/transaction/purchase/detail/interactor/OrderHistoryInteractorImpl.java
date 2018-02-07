package com.tokopedia.transaction.purchase.detail.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.purchase.detail.domain.OrderDetailRepository;
import com.tokopedia.transaction.purchase.detail.domain.OrderHistoryRepository;
import com.tokopedia.transaction.purchase.detail.model.history.viewmodel.OrderHistoryData;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 11/17/17. Tokopedia
 */

public class OrderHistoryInteractorImpl implements OrderHistoryInteractor {

    private CompositeSubscription compositeSubscription;

    private OrderHistoryRepository repository;

    public OrderHistoryInteractorImpl(OrderHistoryRepository repository,
                                      CompositeSubscription compositeSubscription) {
        this.repository = repository;
        this.compositeSubscription = compositeSubscription;
    }

    @Override
    public void requestOrderHistoryData(Subscriber<OrderHistoryData> subscriber,
                                        TKPDMapParam<String, Object> params) {
        compositeSubscription.add(repository.requestOrderHistoryData(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void onViewDestroyed() {
        compositeSubscription.unsubscribe();
    }
}
