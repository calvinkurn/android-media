package com.tokopedia.seller.topads.interactor;

import android.content.Context;

import com.tokopedia.seller.topads.model.exchange.CreditResponse;
import com.tokopedia.seller.topads.model.exchange.ProductResponse;
import com.tokopedia.seller.topads.model.exchange.ShopResponse;
import com.tokopedia.seller.topads.model.exchange.StatisticResponse;

import java.util.HashMap;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public interface DashboardTopadsInteractor {

    interface ListenerGetDashboardProduct{
        void onSuccess(ProductResponse response);
        void onError(String message);
    }

    interface ListenerGetDashboardShop{
        void onSuccess(ShopResponse response);
        void onError(String message);
    }

    interface ListenerGetDashboardStatistic{
        void onSuccess(StatisticResponse response);
        void onError(String message);
    }

    interface ListenerGetDashboardDeposit{
        void onSuccess(StatisticResponse response);
        void onError(String message);
    }

    interface ListenerGetDashboardCredit{
        void onSuccess(CreditResponse response);
        void onError(String message);
    }

    void unSubscribe();

    void getDashboardProduct(Context context, HashMap<String, String> params, ListenerGetDashboardProduct listenerGetDashboardProduct);

    void getDashboardShop(Context context, HashMap<String, String> params, ListenerGetDashboardShop listenerGetDashboardShop);

    void getDashboardStatistic(Context context, HashMap<String, String> params, ListenerGetDashboardStatistic listenerGetDashboardStatistic);

    void getDashboardResponse(Context context, HashMap<String, String> params, ListenerGetDashboardDeposit listenerGetDashboardDeposit);

    void getDashboardCredit(Context context, HashMap<String, String> params, ListenerGetDashboardCredit listenerGetDashboardCredit);
}
