package com.tokopedia.ride.history.view;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.bookingride.domain.model.Paging;

import java.util.ArrayList;

/**
 * Created by alvarisi on 4/19/17.
 */

public interface RideHistoryContract {
    interface View extends CustomerView{

        RequestParams getHistoriesParam();

        void showFailedGetHistoriesMessage();

        void renderHistoryLists(ArrayList<Visitable> histories);

        void renderUpdatedHistoryRow(int position, Visitable history);

        void enableRefreshLayout();

        void setRefreshLayoutToFalse();

        void disableRefreshLayout();

        void showEmptyResultLayout();

        void showRetryLayout();

        String getMapKey();

        String getMapImageSize();

        void showMainLoading();

        void hideMainLoading();

        void showMainLayout();

        void hideMainLayout();

        void setPaging(Paging paging);

        void showRetryLoadMoreLayout();

        void renderHistoryLoadMoreLists(ArrayList<Visitable> histories);

        void showLoadMoreLoading();

        void hideLoadMoreLoading();

        RequestParams getHistoriesLoadMoreParam();

        Activity getActivity();
    }

    interface Presenter extends CustomerPresenter<View>{

        void initialize();

        void actionRefreshHistoriesData();

        void actionLoadMore();
    }
}
