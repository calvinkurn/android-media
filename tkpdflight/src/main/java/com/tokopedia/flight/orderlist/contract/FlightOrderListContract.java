package com.tokopedia.flight.orderlist.contract;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.design.quickfilter.QuickFilterItem;

import java.util.List;

/**
 * Created by alvarisi on 12/6/17.
 */

public interface FlightOrderListContract {

    interface View extends CustomerView {

        void showGetInitialOrderDataLoading();

        void hideGetInitialOrderDataLoading();

        void renderOrderStatus(List<QuickFilterItem> filterItems);

        void renderOrders(List<Visitable> visitables);

        String getString(int resId);

        void showLoadMoreLoading();

        void hideLoadMoreLoading();

        void renderAddMoreData(List<Visitable> visitables);

        void setLoadMoreStatusToFalse();

        void showEmptyView();

        String getSelectedFilter();

        void showErrorGetOrderOnFilterChanged(Throwable t);

        void showErrorGetInitialOrders(String errorMessage);

        void disableSwipeRefresh();

        void enableSwipeRefresh();

        Context getActivity();
    }

    interface Presenter extends CustomerPresenter<View> {

        void getInitialOrderData();

        void onFilterSelected();

        void onOrderLoadMore(String selectedFilter, int page);

        void onSwipeRefresh();

        void onDestroyView();
    }
}
