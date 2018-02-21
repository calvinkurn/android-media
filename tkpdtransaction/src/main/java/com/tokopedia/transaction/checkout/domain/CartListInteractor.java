package com.tokopedia.transaction.checkout.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.checkout.domain.response.cartlist.CartDataListResponse;
import com.tokopedia.transaction.checkout.domain.response.deletecart.DeleteCartDataResponse;
import com.tokopedia.transaction.checkout.domain.response.updatecart.UpdateCartDataResponse;
import com.tokopedia.transaction.checkout.view.data.CartListData;
import com.tokopedia.transaction.checkout.view.data.DeleteCartData;
import com.tokopedia.transaction.checkout.view.data.DeleteUpdateCartData;
import com.tokopedia.transaction.checkout.view.data.UpdateCartData;
import com.tokopedia.transaction.checkout.view.data.UpdateCartListData;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public class CartListInteractor implements ICartListInteractor {

    private final CompositeSubscription compositeSubscription;
    private final ICartRepository cartRepository;
    private final ICartMapper cartMapper;
    private final IShipmentMapper shipmentMapper;

    @Inject
    public CartListInteractor(CompositeSubscription compositeSubscription,
                              ICartRepository cartRepository,
                              ICartMapper cartMapper,
                              IShipmentMapper shipmentMapper) {
        this.compositeSubscription = compositeSubscription;
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.shipmentMapper = shipmentMapper;
    }


    @Override
    public void getCartList(Subscriber<CartListData> subscriber, TKPDMapParam<String, String> param) {
        compositeSubscription.add(
                cartRepository.getCartList(param)
                        .map(new Func1<CartDataListResponse, CartListData>() {
                            @Override
                            public CartListData call(CartDataListResponse cartDataListResponse) {
                                return cartMapper.convertToCartItemDataList(cartDataListResponse);
                            }
                        })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void deleteCart(Subscriber<DeleteCartData> subscriber, TKPDMapParam<String, String> param) {
        compositeSubscription.add(
                cartRepository.deleteCartData(param)
                        .map(new Func1<DeleteCartDataResponse, DeleteCartData>() {
                            @Override
                            public DeleteCartData call(DeleteCartDataResponse deleteCartDataResponse) {
                                return cartMapper.convertToDeleteCartData(deleteCartDataResponse);
                            }
                        })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void deleteAndUpdateCart(Subscriber<DeleteUpdateCartData> subscriber,
                                    TKPDMapParam<String, String> paramDelete,
                                    final TKPDMapParam<String, String> paramUpdate) {
        compositeSubscription.add(
                cartRepository.deleteCartData(paramDelete)
                        .map(new Func1<DeleteCartDataResponse, DeleteCartData>() {
                            @Override
                            public DeleteCartData call(DeleteCartDataResponse deleteCartDataResponse) {
                                return cartMapper.convertToDeleteCartData(deleteCartDataResponse);
                            }
                        })
                        .flatMap(new Func1<DeleteCartData, Observable<DeleteUpdateCartData>>() {
                            @Override
                            public Observable<DeleteUpdateCartData> call(final DeleteCartData deleteCartData) {
                                if (deleteCartData.isSuccess()) {
                                    return cartRepository.updateCartData(paramUpdate)
                                            .map(new Func1<UpdateCartDataResponse, UpdateCartData>() {
                                                @Override
                                                public UpdateCartData call(UpdateCartDataResponse updateCartDataResponse) {
                                                    return cartMapper.convertToUpdateCartData(updateCartDataResponse);
                                                }
                                            }).map(new Func1<UpdateCartData, DeleteUpdateCartData>() {
                                                @Override
                                                public DeleteUpdateCartData call(UpdateCartData updateCartData) {
                                                    DeleteUpdateCartData deleteUpdateCartData = new DeleteUpdateCartData();
                                                    if (!updateCartData.isSuccess()) {
                                                        deleteUpdateCartData.setMessage(updateCartData.getMessage());
                                                        deleteUpdateCartData.setSuccess(updateCartData.isSuccess());
                                                    } else {
                                                        deleteUpdateCartData.setMessage(deleteCartData.getMessage());
                                                        deleteUpdateCartData.setSuccess(deleteCartData.isSuccess());
                                                    }
                                                    return deleteUpdateCartData;
                                                }
                                            });
                                } else {
                                    DeleteUpdateCartData deleteUpdateCartData = new DeleteUpdateCartData();
                                    deleteUpdateCartData.setMessage(deleteCartData.getMessage());
                                    deleteUpdateCartData.setSuccess(deleteCartData.isSuccess());
                                    return Observable.just(deleteUpdateCartData);
                                }

                            }
                        })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void updateCart(Subscriber<UpdateCartData> subscriber, TKPDMapParam<String, String> param) {
        compositeSubscription.add(
                cartRepository.updateCartData(param)
                        .map(new Func1<UpdateCartDataResponse, UpdateCartData>() {
                            @Override
                            public UpdateCartData call(UpdateCartDataResponse updateCartDataResponse) {
                                return cartMapper.convertToUpdateCartData(updateCartDataResponse);
                            }
                        }).subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void updateAndRefreshCartList(Subscriber<UpdateCartListData> subscriber,
                                         final TKPDMapParam<String, String> paramUpdate,
                                         final TKPDMapParam<String, String> paramGetList,
                                         final TKPDMapParam<String, String> paramGetShipmentForm) {
        compositeSubscription.add(
                Observable.just(new UpdateCartListData())
                        .flatMap(new Func1<UpdateCartListData, Observable<UpdateCartListData>>() {
                            @Override
                            public Observable<UpdateCartListData> call(final UpdateCartListData updateCartListData) {
                                return cartRepository.updateCartData(paramUpdate)
                                        .map(new Func1<UpdateCartDataResponse, UpdateCartListData>() {
                                            @Override
                                            public UpdateCartListData call(UpdateCartDataResponse updateCartDataResponse) {
                                                updateCartListData.setUpdateCartData(
                                                        cartMapper.convertToUpdateCartData(updateCartDataResponse)
                                                );
                                                return updateCartListData;
                                            }
                                        });
                            }
                        })
                        .flatMap(new Func1<UpdateCartListData, Observable<UpdateCartListData>>() {
                            @Override
                            public Observable<UpdateCartListData> call(final UpdateCartListData updateCartListData) {
                                return cartRepository.getCartList(paramGetList)
                                        .map(new Func1<CartDataListResponse, UpdateCartListData>() {
                                            @Override
                                            public UpdateCartListData call(CartDataListResponse cartDataListResponse) {
                                                updateCartListData.setCartListData(
                                                        cartMapper.convertToCartItemDataList(cartDataListResponse)
                                                );
                                                return updateCartListData;
                                            }
                                        });
                            }
                        })
                        .flatMap(new Func1<UpdateCartListData, Observable<UpdateCartListData>>() {
                            @Override
                            public Observable<UpdateCartListData> call(final UpdateCartListData updateCartListData) {
                                return cartRepository.getShipmentAddressForm(paramGetShipmentForm)
                                        .map(new Func1<String, UpdateCartListData>() {
                                            @Override
                                            public UpdateCartListData call(String s) {
                                                updateCartListData.setShipmentAddressFormData(
                                                        shipmentMapper.convertToShipmentAddressFormData()
                                                );
                                                return updateCartListData;
                                            }
                                        });
                            }
                        })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }
}
