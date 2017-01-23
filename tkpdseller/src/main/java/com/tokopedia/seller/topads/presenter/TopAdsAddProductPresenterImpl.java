package com.tokopedia.seller.topads.presenter;

import android.content.Context;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractor;
import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractorImpl;
import com.tokopedia.seller.topads.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.model.data.Product;
import com.tokopedia.seller.topads.model.request.SearchProductRequest;
import com.tokopedia.seller.topads.view.listener.TopAdsAddProductFragmentListener;

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