package com.tokopedia.seller.topads.interactor;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.topads.model.data.DataCredit;
import com.tokopedia.seller.topads.model.data.DataDeposit;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.model.data.TotalAd;
import com.tokopedia.seller.topads.model.exchange.CreditResponse;
import com.tokopedia.seller.topads.model.exchange.ShopRequest;
import com.tokopedia.seller.topads.model.exchange.DepositResponse;
import com.tokopedia.seller.topads.model.exchange.ProductResponse;
import com.tokopedia.seller.topads.model.exchange.ShopResponse;
import com.tokopedia.seller.topads.model.exchange.StatisticRequest;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public interface DashboardTopadsInteractor {

    interface Listener<T> {
        void onSuccess(T t);

        void onError(Throwable throwable);
    }

    void getDashboardSummary(StatisticRequest statisticRequest, final Listener<Summary> listener);

    void getDeposit(ShopRequest shopRequest, final Listener<DataDeposit> listener);

    void getShopInfo(ShopRequest shopRequest, final Listener<ShopModel> listener);

    void getTotalAd(ShopRequest shopRequest, final Listener<TotalAd> listener);

    void getCreditList(final Listener<List<DataCredit>> listener);

    void unSubscribe();

    void getDashboardProduct(HashMap<String, String> params, Listener<ProductResponse> listener);

    void getDashboardShop(HashMap<String, String> params, Listener<ShopResponse> listener);

    void getDashboardResponse(HashMap<String, String> params, Listener<DepositResponse> listener);
}