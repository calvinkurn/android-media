package com.tokopedia.seller.topads.view.presenter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.shopinfo.facades.authservices.ShopService;
import com.tokopedia.core.shopinfo.models.etalasemodel.EtalaseModel;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.domain.interactor.DashboardTopadsInteractor;
import com.tokopedia.seller.topads.domain.interactor.DashboardTopadsInteractorImpl;
import com.tokopedia.seller.topads.domain.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.domain.interactor.TopAdsEtalaseListUseCase;
import com.tokopedia.seller.topads.domain.model.data.DataCredit;
import com.tokopedia.seller.topads.domain.model.data.Etalase;
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

public class TopAdsEtalaseListPresenterImpl extends BaseDaggerPresenter<TopAdsEtalaseListView>
        implements TopAdsEtalaseListPresenter {

    private TopAdsEtalaseListUseCase topAdsEtalaseListUseCase;
    public TopAdsEtalaseListPresenterImpl(TopAdsEtalaseListUseCase topAdsEtalaseListUseCase) {
        this.topAdsEtalaseListUseCase = topAdsEtalaseListUseCase;
    }

    @Override
    public void populateEtalaseList(String shopId) {
        if (TextUtils.isEmpty(shopId))
            return;
        getView().showLoad(true);

        topAdsEtalaseListUseCase.execute(
                TopAdsEtalaseListUseCase.createRequestParams(shopId),
                getShopEtalaseSubscriber()
        );
    }

    private Subscriber<List<Etalase>> getShopEtalaseSubscriber() {
        return new Subscriber<List<Etalase>>() {
            @Override
            public void onCompleted() {
                getView().showLoad(false);
            }

            @Override
            public void onError(Throwable e) {
                getView().showLoad(false);
                if (e instanceof UnknownHostException||
                        e instanceof ConnectException) {
                    getView().onLoadConnectionError();
                } else if (e instanceof SocketTimeoutException) {
                    getView().onLoadConnectionError();
                } else {
                    getView().onLoadError("Terjadi Kesalahan, " +
                            "Mohon ulangi beberapa saat lagi");
                }
            }

            @Override
            public void onNext(List<Etalase> etalaseList) {
                if (etalaseList != null && etalaseList.size() > 0) {
                    getView().onLoadSuccess(etalaseList);

                }
                else {
                    getView().onLoadSuccessEtalaseEmpty();
                }
            }
        };
    }
}