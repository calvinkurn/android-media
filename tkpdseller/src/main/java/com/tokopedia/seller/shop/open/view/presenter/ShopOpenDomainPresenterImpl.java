package com.tokopedia.seller.shop.open.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.shop.open.domain.interactor.CheckDomainNameUseCase;
import com.tokopedia.seller.shop.open.domain.interactor.CheckShopNameUseCase;
import com.tokopedia.seller.shop.open.view.listener.ShopOpenDomainView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public class ShopOpenDomainPresenterImpl extends BaseDaggerPresenter<ShopOpenDomainView>
        implements ShopOpenDomainPresenter {

    private static final int DELAY_DEBOUNCE = 700; // ms

    private final CheckDomainNameUseCase checkDomainNameUseCase;
    private final CheckShopNameUseCase checkShopNameUseCase;
    private Subscription domainDebounceSubscription;
    private Subscription shopDebounceSubscription;

    private ShopOpenDomainPresenterImpl.QueryListener domainListener;
    private ShopOpenDomainPresenterImpl.QueryListener shopListener;

    @Inject
    public ShopOpenDomainPresenterImpl(CheckDomainNameUseCase checkDomainNameUseCase,
                                       CheckShopNameUseCase checkShopNameUseCase) {
        this.checkDomainNameUseCase = checkDomainNameUseCase;
        this.checkShopNameUseCase = checkShopNameUseCase;

        domainDebounceSubscription = Observable.create(
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
        shopDebounceSubscription = Observable.create(
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

    private interface QueryListener {
        void query(String string);
    }

    @Override
    public void checkShop(String shopName) {
        if (shopListener != null) {
            shopListener.query(shopName);
        }
    }

    private void checkShopWS(String shopName) {
        checkShopNameUseCase.unsubscribe();
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
                getView().onSuccessCheckShopName(existed);
            }
        });
    }

    private void checkDomainWS(String domainName) {
        checkDomainNameUseCase.unsubscribe();
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
                getView().onSuccessCheckShopDomain(existed);
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
        domainDebounceSubscription.unsubscribe();
        shopDebounceSubscription.unsubscribe();
    }
}
