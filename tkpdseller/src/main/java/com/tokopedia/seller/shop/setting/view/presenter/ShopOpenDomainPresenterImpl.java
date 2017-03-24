package com.tokopedia.seller.shop.setting.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.shop.setting.domain.interactor.CheckDomainNameUseCase;
import com.tokopedia.seller.shop.setting.domain.interactor.CheckShopNameUseCase;

import java.util.concurrent.TimeUnit;

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
        if (shopListener!= null) {
            shopListener.query(shopName);
        }
    }

    private void checkShopWS (String shopName) {
        checkShopNameUseCase.execute(
                CheckShopNameUseCase.createRequestParams(shopName),
                getCheckShopSubscriber());
    }

    private void checkDomainWS (String domainName) {
        checkDomainNameUseCase.execute(
                CheckShopNameUseCase.createRequestParams(domainName),
                getCheckDomainSubscriber());
    }

    private Subscriber<Boolean> getCheckShopSubscriber(){
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                // TODO error network?
            }

            @Override
            public void onNext(Boolean existed) {
                getView().setShopCheckResult(existed);
            }
        };
    }


    private Subscriber<Boolean> getCheckDomainSubscriber(){
         return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                // TODO error network?
            }

            @Override
            public void onNext(Boolean existed) {
                getView().setDomainCheckResult(existed);
            }
        };
    }

    @Override
    public void checkDomain(String domainName) {
        if (domainListener!= null) {
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
