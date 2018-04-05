package com.tokopedia.home.beranda.presentation.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.constants.DrawerActivityBroadcastReceiverConstant;
import com.tokopedia.core.constants.TokoPointDrawerBroadcastReceiverConstant;
import com.tokopedia.core.drawer2.data.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.core.drawer2.data.viewmodel.TokoPointDrawerData;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.shopinfo.facades.GetShopInfoRetrofit;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.tokocash.model.CashBackData;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.domain.interactor.GetHomeDataUseCase;
import com.tokopedia.home.beranda.domain.interactor.GetLocalHomeDataUseCase;
import com.tokopedia.home.beranda.listener.HomeFeedListener;
import com.tokopedia.home.beranda.presentation.view.HomeContract;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.home.beranda.presentation.view.subscriber.GetHomeFeedsSubscriber;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetHomeFeedsUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomePresenter extends BaseDaggerPresenter<HomeContract.View> implements HomeContract.Presenter {

    private static final String TAG = HomePresenter.class.getSimpleName();
    private static final String CURSOR_NO_NEXT_PAGE_FEED = "CURSOR_NO_NEXT_PAGE_FEED";
    private final Context context;
    protected CompositeSubscription compositeSubscription;
    protected Subscription subscription;
    @Inject
    GetLocalHomeDataUseCase localHomeDataUseCase;
    @Inject
    GetHomeDataUseCase getHomeDataUseCase;
    @Inject
    GetHomeFeedsUseCase getHomeFeedsUseCase;
    private SessionHandler sessionHandler;
    private GetShopInfoRetrofit getShopInfoRetrofit;
    private String currentCursor = "";
    private PagingHandler pagingHandler;
    private HomeFeedListener feedListener;
    private HeaderViewModel headerViewModel;
    private boolean fetchFirstData;
    private long REQUEST_DELAY = 15000;// 15s
    private long lastRequestTime;

    public HomePresenter(Context context) {
        this.context = context;
        compositeSubscription = new CompositeSubscription();
        subscription = Subscriptions.empty();
        this.pagingHandler = new PagingHandler();
        resetPageFeed();
        sessionHandler = new SessionHandler(context);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @NonNull
    private Observable<List<Visitable>> getDataFromNetwork() {
        return getHomeDataUseCase.getExecuteObservable(RequestParams.EMPTY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void onFirstLaunch() {
        this.fetchFirstData = true;
    }

    @Override
    public void onResume() {
        boolean needRefresh = (lastRequestTime + REQUEST_DELAY > System.currentTimeMillis());
        if (isViewAttached() && !this.fetchFirstData && needRefresh) {
            subscription = getHomeDataUseCase.getExecuteObservable(RequestParams.EMPTY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Visitable>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(List<Visitable> visitables) {
                            if (isViewAttached() && isDataValid(visitables)) {
                                getView().updateListOnResume(visitables);
                                lastRequestTime = System.currentTimeMillis();
                            }
                        }
                    });
            compositeSubscription.add(subscription);
        }
    }

    @Override
    public void getHomeData() {
        if (lastRequestTime + REQUEST_DELAY > System.currentTimeMillis()){
            if(isViewAttached()){
                getView().hideLoading();
            }
        } else {
            initHeaderViewModelData();
            subscription = localHomeDataUseCase.getExecuteObservable(RequestParams.EMPTY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(refreshData())
                    .onErrorResumeNext(getDataFromNetwork())
                    .subscribe(new HomeDataSubscriber());
            compositeSubscription.add(subscription);
        }
    }

    private void initHeaderViewModelData() {
        if (SessionHandler.isV4Login(context)) {
            if (headerViewModel == null) {
                headerViewModel = new HeaderViewModel();
            }
            headerViewModel.setPendingTokocashChecked(false);
        }
    }

    @NonNull
    private Action1<List<Visitable>> refreshData() {
        return new Action1<List<Visitable>>() {
            @Override
            public void call(List<Visitable> visitables) {
                compositeSubscription.add(getDataFromNetwork().subscribe(new HomeDataSubscriber()));
            }
        };
    }

    @Override
    public void updateHeaderTokoCashData(HomeHeaderWalletAction homeHeaderWalletAction) {
        if (headerViewModel == null) {
            headerViewModel = new HeaderViewModel();
        }
        headerViewModel.setWalletDataSuccess();
        headerViewModel.setHomeHeaderWalletActionData(homeHeaderWalletAction);
        getView().updateHeaderItem(headerViewModel);
    }

    @Override
    public void onHeaderTokocashErrorFromBroadcast() {
        if (headerViewModel == null) {
            headerViewModel = new HeaderViewModel();
        }

        headerViewModel.setWalletDataError();
        headerViewModel.setHomeHeaderWalletActionData(null);
        getView().updateHeaderItem(headerViewModel);
    }

    @Override
    public void updateHeaderTokoCashPendingData(CashBackData cashBackData) {
        if (headerViewModel == null) {
            headerViewModel = new HeaderViewModel();
        }
        headerViewModel.setWalletDataSuccess();
        headerViewModel.setCashBackData(cashBackData);
        headerViewModel.setPendingTokocashChecked(true);
        getView().updateHeaderItem(headerViewModel);
    }

    @Override
    public void updateHeaderTokoPointData(TokoPointDrawerData tokoPointDrawerData) {
        if (headerViewModel == null) {
            headerViewModel = new HeaderViewModel();
        }
        headerViewModel.setTokoPointDataSuccess();
        headerViewModel.setTokoPointDrawerData(tokoPointDrawerData);
        getView().updateHeaderItem(headerViewModel);
    }

    @Override
    public void onHeaderTokopointErrorFromBroadcast() {
        if (headerViewModel == null) {
            headerViewModel = new HeaderViewModel();
        }
        headerViewModel.setTokoPointDataError();
        headerViewModel.setTokoPointDrawerData(null);
        getView().updateHeaderItem(headerViewModel);
    }

    @Override
    public void onRefreshTokoPoint() {
        Intent intentGetTokoPoint = new Intent(TokoPointDrawerBroadcastReceiverConstant.INTENT_ACTION_MAIN_APP);
        if (headerViewModel == null) {
            headerViewModel = new HeaderViewModel();
        }
        headerViewModel.setTokoPointDataSuccess();
        headerViewModel.setTokoPointDrawerData(null);
        getView().updateHeaderItem(headerViewModel);
        context.sendBroadcast(intentGetTokoPoint);
    }

    @Override
    public void onRefreshTokoCash() {
        if (!SessionHandler.isV4Login(context)) return;
        Intent intentGetTokocash = new Intent(DrawerActivityBroadcastReceiverConstant.INTENT_ACTION_MAIN_APP);
        intentGetTokocash.putExtra(DrawerActivityBroadcastReceiverConstant.EXTRA_ACTION_RECEIVER,
                DrawerActivityBroadcastReceiverConstant.ACTION_RECEIVER_GET_TOKOCASH_DATA);

        if (headerViewModel == null) {
            headerViewModel = new HeaderViewModel();
        }
        headerViewModel.setWalletDataSuccess();
        headerViewModel.setHomeHeaderWalletActionData(null);
        getView().updateHeaderItem(headerViewModel);
        context.sendBroadcast(intentGetTokocash);
    }

    @Override
    public void getShopInfo(final String url, String shopDomain) {
        getShopInfoRetrofit = new GetShopInfoRetrofit(context, "", shopDomain);
        getShopInfoRetrofit.setGetShopInfoListener(new GetShopInfoRetrofit.OnGetShopInfoListener() {
            @Override
            public void onSuccess(String result) {
                if (isViewAttached()) {
                    try {
                        ShopModel shopModel = new Gson().fromJson(result, ShopModel.class);
                        if (shopModel.info != null) {
                            Bundle bundle = ShopInfoActivity.createBundle(
                                    shopModel.getInfo().getShopId(), shopModel.getInfo().getShopDomain());
                            Intent intent = new Intent(context, ShopInfoActivity.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        } else {
                            getView().openWebViewURL(url, context);
                        }
                    } catch (Exception e) {
                        getView().openWebViewURL(url, context);
                    }
                }
            }

            @Override
            public void onError(String message) {
                if (isViewAttached()) {
                    getView().openWebViewURL(url, context);
                }
            }

            @Override
            public void onFailure() {
                if (isViewAttached()) {
                    getView().openWebViewURL(url, context);
                }
            }
        });
        getShopInfoRetrofit.getShopInfo();
    }

    @Override
    public void openProductPageIfValid(final String url, String shopDomain) {
        getShopInfoRetrofit = new GetShopInfoRetrofit(context, "", shopDomain);
        getShopInfoRetrofit.setGetShopInfoListener(new GetShopInfoRetrofit.OnGetShopInfoListener() {
            @Override
            public void onSuccess(String result) {
                if (isViewAttached()) {
                    try {
                        ShopModel shopModel = new Gson().fromJson(result,
                                ShopModel.class);
                        if (shopModel.info != null) {
                            DeepLinkChecker.openProduct(url, context);
                        } else {
                            getView().openWebViewURL(url, context);
                        }
                    } catch (Exception e) {
                        getView().openWebViewURL(url, context);
                    }
                }
            }

            @Override
            public void onError(String message) {
                if (isViewAttached()) {
                    getView().openWebViewURL(url, context);
                }
            }

            @Override
            public void onFailure() {
                if (isViewAttached()) {
                    getView().openWebViewURL(url, context);
                }
            }
        });
        getShopInfoRetrofit.getShopInfo();
    }

    public void getHeaderData(boolean initialStart) {
        if (!SessionHandler.isV4Login(context)) return;
        Intent intentGetTokocash = new Intent(DrawerActivityBroadcastReceiverConstant.INTENT_ACTION_MAIN_APP);
        intentGetTokocash.putExtra(DrawerActivityBroadcastReceiverConstant.EXTRA_ACTION_RECEIVER,
                DrawerActivityBroadcastReceiverConstant.ACTION_RECEIVER_GET_TOKOCASH_DATA);

        Intent intentGetTokoPoint = new Intent(TokoPointDrawerBroadcastReceiverConstant.INTENT_ACTION_MAIN_APP);

        if (initialStart && headerViewModel != null) {
            if (headerViewModel.getHomeHeaderWalletActionData() == null)
                context.sendBroadcast(intentGetTokocash);
            if (headerViewModel.getTokoPointDrawerData() == null)
                context.sendBroadcast(intentGetTokoPoint);
        } else {
            context.sendBroadcast(intentGetTokocash);
            context.sendBroadcast(intentGetTokoPoint);
        }
    }

    public void setFeedListener(HomeFeedListener feedListener) {
        this.feedListener = feedListener;
    }

    public void resetPageFeed() {
        currentCursor = "";
        pagingHandler.setPage(0);
        if (getHomeFeedsUseCase != null) {
            getHomeFeedsUseCase.unsubscribe();
        }
    }

    public void fetchNextPageFeed() {
        pagingHandler.nextPage();
        fetchCurrentPageFeed();
    }

    public void fetchCurrentPageFeed() {
        if (currentCursor == null)
            return;
        getHomeFeedsUseCase.execute(
                getHomeFeedsUseCase.getFeedPlusParam(
                        pagingHandler.getPage(),
                        sessionHandler,
                        currentCursor),
                new GetHomeFeedsSubscriber(feedListener, pagingHandler.getPage()));
    }

    public void setCursor(String currentCursor) {
        this.currentCursor = currentCursor;
    }

    public void setCursorNoNextPageFeed() {
        this.currentCursor = CURSOR_NO_NEXT_PAGE_FEED;
    }

    public boolean hasNextPageFeed() {
        return !CURSOR_NO_NEXT_PAGE_FEED.equals(currentCursor);
    }

    private class HomeDataSubscriber extends Subscriber<List<Visitable>> {

        public HomeDataSubscriber() {
        }

        @Override
        public void onStart() {
            if (isViewAttached()) {
                getView().showLoading();
            }
        }

        @Override
        public void onCompleted() {
            if (isViewAttached()) {
                getView().hideLoading();
                fetchFirstData = false;
            }
        }

        @Override
        public void onError(Throwable e) {
            if (isViewAttached()) {
                getView().showNetworkError(ErrorHandler.getErrorMessage(e));
                onCompleted();
            }
        }

        @Override
        public void onNext(List<Visitable> visitables) {
            if (isViewAttached()) {
                if (SessionHandler.isV4Login(context) && headerViewModel != null) {
                    visitables.add(0, headerViewModel);
                }
                getView().setItems(visitables);
                if (visitables.size() > 0) {
                    getView().showRecomendationButton();
                }
                if (isDataValid(visitables)) {
                    getView().removeNetworkError();
                    lastRequestTime = System.currentTimeMillis();
                } else {
                    getView().showNetworkError(context.getString(R.string.msg_network_error));
                }
            }
        }

    }

    private boolean isDataValid(List<Visitable> visitables) {
        return containsInstance(visitables, BannerViewModel.class);
    }

    public <E> boolean containsInstance(List<E> list, Class<? extends E> clazz) {
        for (E e : list) {
            if (clazz.isInstance(e)) {
                return true;
            }
        }
        return false;
    }
}
