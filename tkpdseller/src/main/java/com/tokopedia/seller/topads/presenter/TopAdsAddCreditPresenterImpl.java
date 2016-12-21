package com.tokopedia.seller.topads.presenter;

import android.content.Context;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractor;
import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractorImpl;
import com.tokopedia.seller.topads.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.model.data.DataCredit;
import com.tokopedia.seller.topads.model.data.DataDeposit;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.model.exchange.ShopRequest;
import com.tokopedia.seller.topads.model.exchange.StatisticRequest;
import com.tokopedia.seller.topads.view.listener.TopAdsAddCreditFragmentListener;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardFragmentListener;

import java.util.Date;
import java.util.List;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsAddCreditPresenterImpl implements TopAdsAddCreditPresenter {

    private DashboardTopadsInteractor dashboardTopadsInteractor;
    private TopAdsAddCreditFragmentListener listener;
    private Context context;

    public TopAdsAddCreditPresenterImpl(Context context, TopAdsAddCreditFragmentListener listener) {
        this.context = context;
        this.listener = listener;
        dashboardTopadsInteractor = new DashboardTopadsInteractorImpl(context);
    }

    @Override
    public void populateCreditList() {
        dashboardTopadsInteractor.getCreditList(new ListenerInteractor<List<DataCredit>>() {
            @Override
            public void onSuccess(List<DataCredit> creditList) {
                listener.onCreditListLoaded(creditList);
            }

            @Override
            public void onError(Throwable throwable) {
                listener.onLoadCreditListError();
            }
        });
    }
}