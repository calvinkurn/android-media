package com.tokopedia.seller.topads.interactor;

import com.tokopedia.seller.topads.model.data.DataDeposit;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.model.exchange.CreditResponse;
import com.tokopedia.seller.topads.model.exchange.DepositRequest;
import com.tokopedia.seller.topads.model.exchange.DepositResponse;
import com.tokopedia.seller.topads.model.exchange.ProductResponse;
import com.tokopedia.seller.topads.model.exchange.ShopResponse;
import com.tokopedia.seller.topads.model.exchange.StatisticRequest;
import com.tokopedia.seller.topads.model.exchange.StatisticResponse;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public interface DashboardTopadsInteractor {

    interface Listener<T> {
        void onSuccess(T t);

        void onError(Throwable throwable);
    }

    void getDashboardSummary(StatisticRequest statisticRequest, final Listener<Summary> listener);

    void getDeposit(DepositRequest depositRequest, final Listener<DataDeposit> listener);

    void unSubscribe();

    void getDashboardProduct(HashMap<String, String> params, Listener<ProductResponse> listener);

    void getDashboardShop(HashMap<String, String> params, Listener<ShopResponse> listener);

    void getDashboardResponse(HashMap<String, String> params, Listener<DepositResponse> listener);

    void getDashboardCredit(HashMap<String, String> params, Listener<CreditResponse> listener);
}
