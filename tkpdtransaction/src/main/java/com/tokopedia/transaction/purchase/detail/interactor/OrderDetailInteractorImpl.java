package com.tokopedia.transaction.purchase.detail.interactor;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.purchase.detail.domain.OrderDetailRepository;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.EmptyVarianProductEditable;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.WrongProductPriceWeightEditable;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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
            final TKPDMapParam<String, String> rejectParam) {
        compositeSubscription.add(Observable
                .concat(changedProductObservable(subscriber, editables, productParam, rejectParam))
                .last()
                .flatMap(new Func1<String, Observable<String>>() {

                    @Override
                    public Observable<String> call(String s) {
                        return orderDetailRepository.processOrder(rejectParam);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    private List<Observable<String>> changedProductObservable(
            Subscriber<String> productSubscriber,
            List<WrongProductPriceWeightEditable> editables,
            TKPDMapParam<String, String> productParam,
            TKPDMapParam<String, String> rejectParam) {
        List<Observable<String>> cartVarianObservableList = new ArrayList<>();
        for (int i =0; i < editables.size(); i++) {
            TKPDMapParam<String, String> params = new TKPDMapParam<>();
            params.putAll(productParam);
            params.put(SHOP_ID_KEY, editables.get(i).getShopId());
            params.put(PRODUCT_ID_KEY, editables.get(i).getProductId());
            params.put(
                    PRODUCT_PRICE_KEY,
                    editables.get(i).getProductPriceUnformatted()
            );
            params.put(
                    PRODUCT_WEIGHT_VALUE_KEY,
                    editables.get(i).getProductWeightUnformatted()
            );
            params.put(
                    PRODUCT_PRICE_CURRENCY_KEY,
                    String.valueOf(editables.get(i).getCurrencyMode())
            );
            params.put(
                    PRODUCT_WEIGHT_UNIT_KEY,
                    String.valueOf(editables.get(i).getWeightMode())
            );
            Observable<String> productObservable = orderDetailRepository.changeProduct(params);
            productObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.newThread());
            cartVarianObservableList.add(productObservable);
        }
        return cartVarianObservableList;
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
