package com.tokopedia.transaction.purchase.detail.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.purchase.detail.domain.OrderDetailRepository;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.EmptyVarianProductEditable;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.WrongProductPriceWeightEditable;

import java.util.List;

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
    public void processOrder(Subscriber<String> subscriber, TKPDMapParam<String, String> params) {
        compositeSubscription.add(orderDetailRepository.processOrder(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void rejectEmptyOrderVarian(
            Subscriber<String> subscriber,
            List<EmptyVarianProductEditable> emptyVarianProductEditables,
            TKPDMapParam<String, String> productParam,
            TKPDMapParam<String, String> rejectParam
    ) {
        compositeSubscription.add(orderDetailRepository
                .rejectOrderChangeProductVarian(
                        emptyVarianProductEditables,
                        productParam,
                        rejectParam
                )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void rejectChangeWeightPrice(
            Subscriber<String> subscriber,
            List<WrongProductPriceWeightEditable> editables,
            TKPDMapParam<String, String> productParam,
            TKPDMapParam<String, String> rejectParam) {
        compositeSubscription.add(orderDetailRepository
                .rejectOrderWeightPrice(
                        editables,
                        productParam,
                        rejectParam
                )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void confirmAwb(Subscriber<String> subscriber, TKPDMapParam<String, String> params) {
        compositeSubscription.add(orderDetailRepository.changeAwb(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void confirmShipping(Subscriber<String> subscriber, TKPDMapParam<String, String> params) {
        compositeSubscription.add(orderDetailRepository.processShipping(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void cancelShipping(Subscriber<String> subscriber, TKPDMapParam<String, String> params) {
        compositeSubscription.add(orderDetailRepository.processShipping(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void retryPickup(Subscriber<String> subscriber, TKPDMapParam<String, String> params) {
        compositeSubscription.add(orderDetailRepository.retryPickup(params)
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
