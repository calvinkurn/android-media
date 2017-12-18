package com.tokopedia.transaction.purchase.detail.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.purchase.detail.domain.OrderDetailRepository;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 11/13/17. Tokopedia
 */

public class OrderDetailInteractorImpl implements OrderDetailInteractor {

    private CompositeSubscription compositeSubscription;

    private OrderDetailRepository orderDetailRepository;

    public OrderDetailInteractorImpl(CompositeSubscription compositeSubscription,
                                     OrderDetailRepository orderDetailRepository) {
        this.compositeSubscription = compositeSubscription;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public void requestDetailData(Subscriber<OrderDetailData> subscriber,
                                  TKPDMapParam<String, Object> params) {
        compositeSubscription.add(orderDetailRepository.requestOrderDetailData(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void confirmFinishConfirm(Subscriber<String> subscriber,
                                     TKPDMapParam<String, String> params) {
        compositeSubscription.add(orderDetailRepository.
                confirmFinishDeliver(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void confirmDeliveryConfirm(Subscriber<String> subscriber, TKPDMapParam<String, String> params) {
        compositeSubscription.add(orderDetailRepository.confirmDelivery(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void cancelOrder(Subscriber<String> subscriber, TKPDMapParam<String, String> params) {
        compositeSubscription.add(orderDetailRepository.requestCancelOrder(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void cancelReplacement(Subscriber<String> subscriber, TKPDMapParam<String, Object> params) {
        compositeSubscription.add(orderDetailRepository.cancelReplacement(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }


    @Override
    public void onActivityClosed() {
        compositeSubscription.unsubscribe();
    }
}
