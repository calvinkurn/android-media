package com.tokopedia.transaction.orders.orderlist.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.transaction.orders.orderdetails.data.Status;
import com.tokopedia.transaction.orders.orderlist.data.bomorderfilter.CustomDate;
import com.tokopedia.transaction.orders.orderlist.data.bomorderfilter.DefaultDate;
import java.text.ParseException;
import java.util.List;

/**
 * Created by baghira on 07/05/18.
 */

public class OrderListContract {
    public interface View extends CustomerView {
        void showProcessGetData();

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

        void addData(List<Visitable> data, Boolean isRecommendation);

        void displayLoadMore(boolean isLoadMore);

        void triggerSendEnhancedEcommerceAddToCartSuccess(AddToCartDataModel addToCartDataResponseModel, Object productModel);

        void finishOrderDetail();

        void startSellerAndAddInvoice();

        void requestCancelOrder(Status status);

        void showSuccessMessageWithAction(String message);

        void setFilterRange(DefaultDate defaultDate, CustomDate customDate) throws ParseException;
    }

    public interface Presenter extends CustomerPresenter<View> {
        void getAllOrderData(Context context, String orderCategory, int typeRequest, int page, int orerId);

        void processGetRecommendationData(int page, boolean isFirstTime);

        void onRefresh();
    }
}
