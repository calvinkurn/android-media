package com.tokopedia.seller.shop.open.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.shop.open.domain.interactor.CheckDomainNameUseCase;
import com.tokopedia.seller.shop.open.domain.interactor.CheckShopNameUseCase;
import com.tokopedia.seller.shop.open.domain.interactor.ReserveShopNameDomainUseCase;
import com.tokopedia.seller.shop.open.view.listener.ShopOpenDomainView;
import com.tokopedia.seller.shop.open.data.model.response.ResponseReserveDomain;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class ShopOpenDomainPresenterImpl extends BaseDaggerPresenter<ShopOpenDomainView>
        implements ShopOpenDomainPresenter {

    private static final int DELAY_DEBOUNCE = 700; // ms
    public static final String SUCCESS = "1";

    private final CheckDomainNameUseCase checkDomainNameUseCase;
    private final CheckShopNameUseCase checkShopNameUseCase;
    private final ReserveShopNameDomainUseCase reserveShopNameDomainUseCase;
    private Subscription domainDebounceSubscription;
    private Subscription shopDebounceSubscription;

    private ShopOpenDomainPresenterImpl.QueryListener domainListener;
    private ShopOpenDomainPresenterImpl.QueryListener shopListener;

    @Inject
    public ShopOpenDomainPresenterImpl(CheckDomainNameUseCase checkDomainNameUseCase,
                                       CheckShopNameUseCase checkShopNameUseCase,
                                       ReserveShopNameDomainUseCase reserveShopNameDomainUseCase) {
        this.checkDomainNameUseCase = checkDomainNameUseCase;
        this.checkShopNameUseCase = checkShopNameUseCase;
        this.reserveShopNameDomainUseCase = reserveShopNameDomainUseCase;

        domainDebounceSubscription = Observable.unsafeCreate(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(final Subscriber<? super String> subscriber) {
                        domainListener = new ShopOpenDomainPresenterImpl.QueryListener() {
                            @Override
                            public void query(String string) {
                                subscriber.onNext(string);
                            }
                        };
                    }
                })
                .debounce(DELAY_DEBOUNCE, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        checkDomainWS(s);
                    }
                });
        shopDebounceSubscription = Observable.unsafeCreate(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(final Subscriber<? super String> subscriber) {
                        shopListener = new ShopOpenDomainPresenterImpl.QueryListener() {
                            @Override
                            public void query(String string) {
                                subscriber.onNext(string);
                            }
                        };
                    }
                })
                .debounce(DELAY_DEBOUNCE, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        checkShopWS(s);
                    }
                });
    }

    private Subscriber<ResponseReserveDomain> getReserveShopSubscriber() {
        return new Subscriber<ResponseReserveDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorReserveShop(e);
                }
            }

            @Override
            public void onNext(ResponseReserveDomain responseReserveDomain) {
                if (SUCCESS.equals( responseReserveDomain.getShopDomainStatus()) &&
                        SUCCESS.equals( responseReserveDomain.getShopNameStatus())) {
                    getView().onSuccessReserveShop(responseReserveDomain.getShopName());
                } else {
                    getView().onErrorReserveShop(null);
                }
            }
        };
    }

    private interface QueryListener {
        void query(String string);
    }

    @Override
    public void checkShop(String shopName) {
        if (shopListener != null) {
            shopListener.query(shopName);
        }
    }

    public void submitReserveNameAndDomainShop(String shopName, String shopDomain) {
        reserveShopNameDomainUseCase.execute(
                ReserveShopNameDomainUseCase.createRequestParams(shopName, shopDomain), getReserveShopSubscriber());
    }

    private void checkShopWS(String shopName) {
        checkShopNameUseCase.unsubscribe();
        if (!getView().isShopNameInValidRange()){
            return;
        }
        checkShopNameUseCase.execute(CheckShopNameUseCase.createRequestParams(shopName), new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorCheckShopName(e);
                }
            }

            @Override
            public void onNext(Boolean existed) {
                if (getView().isShopNameInValidRange()) {
                    getView().onSuccessCheckShopName(existed);
                }
            }
        });
    }

    private void checkDomainWS(String domainName) {
        checkDomainNameUseCase.unsubscribe();
        if (!getView().isShopDomainInValidRange()){
            return;
        }
        checkDomainNameUseCase.execute(CheckDomainNameUseCase.createRequestParams(domainName), new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorCheckShopDomain(e);
                }
            }

            @Override
            public void onNext(Boolean existed) {
                if (getView().isShopDomainInValidRange()) {
                    getView().onSuccessCheckShopDomain(existed);
                }
            }
        });
    }

    @Override
    public void checkDomain(String domainName) {
        if (domainListener != null) {
            domainListener.query(domainName);
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        checkDomainNameUseCase.unsubscribe();
        checkShopNameUseCase.unsubscribe();
        reserveShopNameDomainUseCase.unsubscribe();
        domainDebounceSubscription.unsubscribe();
        shopDebounceSubscription.unsubscribe();
    }
}
