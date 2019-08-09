package com.tokopedia.transaction.orders.orderlist.view.presenter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.transaction.orders.orderlist.data.Order;

import java.util.List;

/**
 * Created by baghira on 07/05/18.
 */

public class OrderListContract {
    public interface View extends CustomerView {
        void showProcessGetData();

        void renderDataList(List<Order> orderDataList);

        void showFailedResetData(String message);

        void showNoConnectionResetData(String message);

        void showEmptyData(int typeRequest);

        void removeProgressBarView();

        void unregisterScrollListener();

        void showErrorNetwork(String errorMessage);

        void renderEmptyList(int typeRequest);

        Context getAppContext();

        void setLastOrderId(int orderid);

        String getSelectedFilter();

        void renderOrderStatus(List<QuickFilterItem> filterItems, int selctedIndex);

        void showSurveyButton(boolean isEligible);

        String getString(int resId);

        String getSearchedString();

        String getStartDate();

        String getEndDate();

        void showSuccessMessage(String message);

        void showFailureMessage(String message);

        void addRecommendationData(List<Visitable> data);

        void displayLoadMore(boolean isLoadMore);

        void initAdapterWithData(List<Visitable> data);
    }

    public interface Presenter extends CustomerPresenter<View> {
        void getAllOrderData(Context context, String orderCategory, int typeRequest, int page, int orerId);

        void processGetRecommendationData(int page, boolean isFirstTime);

        void onRefresh();

        void initDataInstance();
    }
}
