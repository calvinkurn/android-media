package com.tokopedia.seller.topads.interactor;

import com.tokopedia.seller.topads.model.exchange.CreditResponse;
import com.tokopedia.seller.topads.model.exchange.DepositResponse;
import com.tokopedia.seller.topads.model.exchange.ProductResponse;
import com.tokopedia.seller.topads.model.exchange.ShopResponse;
import com.tokopedia.seller.topads.model.exchange.StatisticResponse;

import java.util.HashMap;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public interface DashboardTopadsInteractor {

    interface Listener<T> {
        void onSuccess(T t);
        void onError(Throwable throwable);
    }

    void unSubscribe();

    void getDashboardProduct(HashMap<String, String> params, Listener<ProductResponse> listener);

    void getDashboardShop(HashMap<String, String> params, Listener<ShopResponse> listener);

    void getDashboardStatistic(HashMap<String, String> params, Listener<StatisticResponse> listener);

    void getDashboardResponse(HashMap<String, String> params, Listener<DepositResponse> listener);

    void getDashboardCredit(HashMap<String, String> params, Listener<CreditResponse> listener);
}
