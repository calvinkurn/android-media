package com.tokopedia.seller.topads.interactor;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.topads.model.data.GroupAdBulkAction;
import com.tokopedia.seller.topads.model.data.ProductAd;
import com.tokopedia.seller.topads.model.data.ProductAdBulkAction;
import com.tokopedia.seller.topads.model.data.DataCredit;
import com.tokopedia.seller.topads.model.data.DataDeposit;
import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.model.data.Product;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.model.data.TotalAd;
import com.tokopedia.seller.topads.model.request.DataRequest;
import com.tokopedia.seller.topads.model.request.SearchAdRequest;
import com.tokopedia.seller.topads.model.request.SearchProductRequest;
import com.tokopedia.seller.topads.model.request.ShopRequest;
import com.tokopedia.seller.topads.model.request.StatisticRequest;
import com.tokopedia.seller.topads.model.response.DataResponse;
import com.tokopedia.seller.topads.model.response.PageDataResponse;

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

    void getListProductAds(HashMap<String, String> params, ListenerInteractor<PageDataResponse<List<ProductAd>>> listener);

    void getListGroupAds(SearchAdRequest searchAdRequest, ListenerInteractor<List<GroupAd>> listener);

    void getDashboardShop(HashMap<String, String> params, ListenerInteractor<DataResponse<ProductAd>> listener);

    void getDashboardResponse(HashMap<String, String> params, ListenerInteractor<DataResponse<DataDeposit>> listener);

    void actionSingleAds(DataRequest<ProductAdBulkAction> dataRequest, ListenerInteractor<ProductAdBulkAction> listenerInteractor);

    void actionGroupAds(DataRequest<GroupAdBulkAction> dataRequest, ListenerInteractor<GroupAdBulkAction> listenerInteractor);
}