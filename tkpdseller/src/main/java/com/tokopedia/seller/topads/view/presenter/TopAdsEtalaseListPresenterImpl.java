package com.tokopedia.seller.topads.view.presenter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.shopinfo.facades.authservices.ShopService;
import com.tokopedia.core.shopinfo.models.etalasemodel.EtalaseModel;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.domain.interactor.DashboardTopadsInteractor;
import com.tokopedia.seller.topads.domain.interactor.DashboardTopadsInteractorImpl;
import com.tokopedia.seller.topads.domain.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.domain.model.data.DataCredit;
import com.tokopedia.seller.topads.view.listener.TopAdsAddCreditFragmentListener;
import com.tokopedia.seller.topads.view.listener.TopAdsEtalaseListView;
import com.tokopedia.seller.topads.view.listener.TopAdsManagePromoProductView;

import org.w3c.dom.Text;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class TopAdsEtalaseListPresenterImpl implements TopAdsEtalaseListPresenter {

    private TopAdsEtalaseListView view;

    // TODO using interactor and clean code architecture
    // TODO refactor shop service

    CompositeSubscription compositeSubscription;
    ShopService shopService = new ShopService();
    private Context context;

    public TopAdsEtalaseListPresenterImpl(Context context, TopAdsEtalaseListView view) {
        this.context = context;
        this.view = view;
        shopService = new ShopService();
        compositeSubscription = new CompositeSubscription();
    }


    @Override
    public void unSubscribe() {
        if (compositeSubscription != null) {
            compositeSubscription.clear();
            compositeSubscription = null;
        }

    }

    @Override
    public void populateEtalaseList() {
        String shopId = new SessionHandler(context).getShopID();
        if (TextUtils.isEmpty(shopId))
            return;
        view.showLoad(true);
        Map<String, String> params = new ArrayMap<>();
        params.put("shop_id", shopId);

        Observable<Response<TkpdResponse>> observable = shopService.getApi().getShopEtalase(
                AuthUtil.generateParams(
                        context, params));
        Subscription subscription = observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getShopEtalaseSubscriber());
        compositeSubscription.add(subscription);
    }

    private Subscriber<Response<TkpdResponse>> getShopEtalaseSubscriber() {
        return new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                view.showLoad(false);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UnknownHostException||
                        e instanceof ConnectException) {
                    view.onLoadConnectionError();
                } else if (e instanceof SocketTimeoutException) {
                    view.onLoadConnectionError();
                } else {
                    view.onLoadError("Terjadi Kesalahan, " +
                            "Mohon ulangi beberapa saat lagi");
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> tkpdResponseResponse) {
                if (tkpdResponseResponse.isSuccessful()) {
                    EtalaseModel etalaseModel =  new Gson().fromJson(
                            tkpdResponseResponse.body().getStringData(), EtalaseModel.class);
                    view.onLoadSuccess(etalaseModel.list);

                }
//                    view.onLoadSuccess(
//                            new Gson().fromJson(tkpdResponseResponse.body().getJsonData(), EtalaseModel.class));
                else {
                    view.onLoadSuccessEtalaseEmpty();
                }
//                    onEtalaseResponseError(tkpdResponseResponse.code());
            }
        };
    }
}