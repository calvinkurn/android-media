package com.tokopedia.seller.topads.presenter;

import android.content.Context;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractor;
import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractorImpl;
import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.model.exchange.ProductResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public class TopAdsListPresenterImpl implements TopAdsListPresenter {

    private final TopAdsListView topAdsListView;
    private final Context context;
    private final DashboardTopadsInteractor dashboardTopadsInteractor;
    private List<Ad> topAdsListItem;

    public TopAdsListPresenterImpl(Context context,TopAdsListView topAdsListView) {
        this.topAdsListView = topAdsListView;
        topAdsListItem = new ArrayList<>();
        this.context = context;
        this.dashboardTopadsInteractor = new DashboardTopadsInteractorImpl(context);
    }

    @Override
    public List<Ad> getListTopAds() {
        return topAdsListItem;
    }

    @Override
    public void getListTopAdsFromNet() {
        HashMap<String, String> params = new HashMap<>();
        params.put("shop_id", SessionHandler.getShopID(context));
        params.put("start_date", "");
        params.put("end_date", "");
        dashboardTopadsInteractor.getDashboardProduct(params, new DashboardTopadsInteractor.Listener<ProductResponse>(){

            @Override
            public void onSuccess(ProductResponse productResponse) {
                if(productResponse != null) {
                    topAdsListItem.addAll(productResponse.getData());
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }
}
