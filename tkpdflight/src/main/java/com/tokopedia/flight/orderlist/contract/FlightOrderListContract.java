package com.tokopedia.flight.orderlist.contract;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.design.quickfilter.QuickFilterItem;

import java.util.List;

/**
 * Created by alvarisi on 12/6/17.
 */

public interface FlightOrderListContract {

    interface View extends BaseListViewListener<Visitable> {

        void renderOrderStatus(List<QuickFilterItem> filterItems);

        String getString(int resId);

        String getSelectedFilter();

        Context getActivity();

        void navigateToOpenBrowser(String urlPdf);
    }

    interface Presenter extends CustomerPresenter<View> {

        void loadData(String selectedFilter, int page, int perPage);

        void onDestroyView();

        void onDownloadEticket(String invoiceId, String filename);
    }
}
