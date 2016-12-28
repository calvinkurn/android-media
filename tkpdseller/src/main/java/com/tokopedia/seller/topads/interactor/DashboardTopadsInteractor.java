package com.tokopedia.seller.topads.interactor;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.topads.model.data.DataCredit;
import com.tokopedia.seller.topads.model.data.DataDeposit;
import com.tokopedia.seller.topads.model.data.DataRequestGroupAd;
import com.tokopedia.seller.topads.model.data.DataRequestSingleAd;
import com.tokopedia.seller.topads.model.data.DataResponseActionAds;
import com.tokopedia.seller.topads.model.data.Product;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.model.data.TotalAd;
import com.tokopedia.seller.topads.model.request.AdsActionRequest;
import com.tokopedia.seller.topads.model.response.DepositResponse;
import com.tokopedia.seller.topads.model.response.GroupAdResponse;
import com.tokopedia.seller.topads.model.response.ProductResponse;
import com.tokopedia.seller.topads.model.request.SearchProductRequest;
import com.tokopedia.seller.topads.model.request.ShopRequest;
import com.tokopedia.seller.topads.model.response.ShopResponse;
import com.tokopedia.seller.topads.model.request.StatisticRequest;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public interface DashboardTopadsInteractor {

    void getDashboardSummary(StatisticRequest statisticRequest, final ListenerInteractor<Summary> listener);

    void getDeposit(ShopRequest shopRequest, final ListenerInteractor<DataDeposit> listener);

    void getShopInfo(ShopRequest shopRequest, final ListenerInteractor<ShopModel> listener);

    void getTotalAd(ShopRequest shopRequest, final ListenerInteractor<TotalAd> listener);

    void getCreditList(final ListenerInteractor<List<DataCredit>> listener);

    void searchProduct(SearchProductRequest searchProductRequest, final ListenerInteractor<List<Product>> listener);

    void unSubscribe();

    void getListProductAds(HashMap<String, String> params, ListenerInteractor<ProductResponse> listener);

    void getListGroupAds(HashMap<String, String> params, ListenerInteractor<GroupAdResponse> listener);

    void getDashboardShop(HashMap<String, String> params, ListenerInteractor<ShopResponse> listener);

    void getDashboardResponse(HashMap<String, String> params, ListenerInteractor<DepositResponse> listener);

    void actionSingleAds(AdsActionRequest<DataRequestSingleAd> adsActionRequest, ListenerInteractor<DataResponseActionAds> listenerInteractor);

    void actionGroupAds(AdsActionRequest<DataRequestGroupAd> adsActionRequest, ListenerInteractor<DataResponseActionAds> listenerInteractor);
}