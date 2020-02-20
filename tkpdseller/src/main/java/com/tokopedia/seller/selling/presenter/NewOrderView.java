package com.tokopedia.seller.selling.presenter;

import android.content.Intent;
import android.view.View;

import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.seller.selling.model.orderShipping.OrderShippingList;
import com.tokopedia.core.util.PagingHandler;

import java.util.List;

/**
 * Created by Toped10 on 7/18/2016.
 */
public interface NewOrderView extends BaseView {
    void initHandlerAndAdapter();

    boolean getUserVisible();

    void setRefreshPullEnable(boolean b);

    void disableFilter();

    String getQuery();

    int getDeadlineSelectionPos();

    void notifyDataSetChanged(List<OrderShippingList> listDatas);

    void finishRefresh();

    void removeRetry();

    void removeLoading();

    void enableFilter();

    View getRootView();

    void setRefreshing(boolean b);

    void addLoadingFooter();

    void initListener();

    void hideFilterView();

    boolean getRefreshing();

    void resetPage();

    PagingHandler getPaging();

    void addRetry();

    void addRetryMessage(String message);

    void removeRetryMessage();

    void moveToDetailResult(Intent intent, int code);

    void addEmptyView();

    void removeEmpty();

    void hideFab();

    void showFab();
}
