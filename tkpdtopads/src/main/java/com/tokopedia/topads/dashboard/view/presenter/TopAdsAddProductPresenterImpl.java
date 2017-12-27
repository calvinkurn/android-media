package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topads.dashboard.domain.interactor.DashboardTopadsInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.DashboardTopadsInteractorImpl;
import com.tokopedia.topads.dashboard.domain.interactor.ListenerInteractor;
import com.tokopedia.topads.dashboard.data.model.data.Product;
import com.tokopedia.topads.dashboard.data.model.request.SearchProductRequest;
import com.tokopedia.topads.dashboard.view.listener.TopAdsAddProductFragmentListener;

import java.util.List;

/**
 * Created by Nathaniel on 23/01/2017.
 */
public class TopAdsAddProductPresenterImpl implements TopAdsAddProductPresenter {

    private DashboardTopadsInteractor dashboardTopadsInteractor;
    private TopAdsAddProductFragmentListener listener;
    private Context context;

    public TopAdsAddProductPresenterImpl(Context context, TopAdsAddProductFragmentListener listener) {
        this.context = context;
        this.listener = listener;
        dashboardTopadsInteractor = new DashboardTopadsInteractorImpl(context);
    }

    protected String getShopId() {
        SessionHandler session = new SessionHandler(context);
        return session.getShopID();
    }

    @Override
    public void searchProduct(String keyword, int start) {
        SearchProductRequest searchProductRequest = new SearchProductRequest();
        searchProductRequest.setShopId(getShopId());
        searchProductRequest.setKeyword(keyword);
        searchProductRequest.setStart(start);
        dashboardTopadsInteractor.searchProduct(searchProductRequest, new ListenerInteractor<List<Product>>() {
            @Override
            public void onSuccess(List<Product> productList) {
                listener.onProductListLoaded(productList);
            }

            @Override
            public void onError(Throwable throwable) {
                listener.onLoadProductListError();
            }
        });
    }
}