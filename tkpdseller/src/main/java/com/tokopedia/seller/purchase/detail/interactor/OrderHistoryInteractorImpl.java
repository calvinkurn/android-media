package com.tokopedia.seller.purchase.detail.interactor;

import com.tokopedia.seller.purchase.detail.domain.OrderHistoryRepository;
import com.tokopedia.seller.purchase.detail.model.history.viewmodel.OrderHistoryData;

import java.util.HashMap;

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
                                        HashMap<String, Object> params) {
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
