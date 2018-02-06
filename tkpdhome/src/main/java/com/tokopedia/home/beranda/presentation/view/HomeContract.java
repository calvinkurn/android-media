package com.tokopedia.home.beranda.presentation.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.drawer2.data.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.core.drawer2.data.viewmodel.TokoPointDrawerData;
import com.tokopedia.digital.tokocash.model.CashBackData;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;

import java.util.List;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public interface HomeContract {

    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void setItems(List<Visitable> items);

        void updateHeaderItem(HeaderViewModel headerViewModel);

        void showNetworkError(String message);

        void removeNetworkError();

        String getString(@StringRes int res);

        Context getContext();

        void openWebViewURL(String url, Context context);

        Activity getActivity();

    }

    interface Presenter extends CustomerPresenter<View> {
        void getHomeData();

        void getHeaderData(boolean initialStart);

        void updateHeaderTokoCashData(HomeHeaderWalletAction homeHeaderWalletActionData);

        void updateHeaderTokoCashPendingData(CashBackData cashBackData);

        void updateHeaderTokoPointData(TokoPointDrawerData tokoPointDrawerData);

        void getShopInfo(String url, String shopDomain);

        void openProductPageIfValid(String url, String shopDomain);

    }
}
