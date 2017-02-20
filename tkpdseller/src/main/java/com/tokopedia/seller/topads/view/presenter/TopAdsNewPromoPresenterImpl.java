package com.tokopedia.seller.topads.view.presenter;

import android.content.Context;

import com.tokopedia.seller.topads.domain.interactor.DashboardTopadsInteractor;
import com.tokopedia.seller.topads.domain.interactor.DashboardTopadsInteractorImpl;
import com.tokopedia.seller.topads.domain.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.model.data.DataCredit;
import com.tokopedia.seller.topads.view.listener.TopAdsNewPromoFragmentListener;

import java.util.List;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsNewPromoPresenterImpl implements TopAdsNewPromoPresenter {

    private DashboardTopadsInteractor dashboardTopadsInteractor;
    private TopAdsNewPromoFragmentListener listener;
    private Context context;

    public TopAdsNewPromoPresenterImpl(Context context, TopAdsNewPromoFragmentListener listener) {
        this.context = context;
        this.listener = listener;
        dashboardTopadsInteractor = new DashboardTopadsInteractorImpl(context);
    }

    @Override
    public void populateGroupNameList() {
        dashboardTopadsInteractor.getCreditList(new ListenerInteractor<List<DataCredit>>() {
            @Override
            public void onSuccess(List<DataCredit> creditList) {
                listener.onGroupNameListLoaded(creditList);
            }

            @Override
            public void onError(Throwable throwable) {
                listener.onLoadGroupNameListError();
            }
        });
    }
}