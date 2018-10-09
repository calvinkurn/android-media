package com.tokopedia.transaction.purchase.detail.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.purchase.detail.domain.OrderCourierRepository;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.ListCourierViewModel;

import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 1/3/18. Tokopedia
 */

public class OrderCourierInteractorImpl implements OrderCourierInteractor{

    private CompositeSubscription compositeSubscription;
    private OrderCourierRepository repository;

    public OrderCourierInteractorImpl(CompositeSubscription compositeSubscription,
                                      OrderCourierRepository repository) {
        this.compositeSubscription = compositeSubscription;
        this.repository = repository;
    }

    @Override
    public void onGetCourierList(String selectedCourierId,
                                 TKPDMapParam<String, String> params,
                                 Subscriber<ListCourierViewModel> subscriber) {
        compositeSubscription.add(repository.onOrderCourierRepository(selectedCourierId, params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void confirmShipping(TKPDMapParam<String, String> params,
                                Subscriber<String> subscriber) {
        compositeSubscription.add(repository.processShipping(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void changeCourier(Map<String, String> params,
                              Subscriber<String> subscriber) {
        compositeSubscription.add(repository.changeCourier(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }
}
