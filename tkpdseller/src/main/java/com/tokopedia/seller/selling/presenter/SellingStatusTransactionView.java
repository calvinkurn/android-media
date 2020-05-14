package com.tokopedia.seller.selling.presenter;

import android.view.View;

import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.seller.selling.model.SellingStatusTxModel;

import java.util.List;

/**
 * Created by Toped10 on 7/15/2016.
 */
public interface SellingStatusTransactionView extends BaseView {

    void initHandlerAndAdapter();

    boolean getUserVisible();

    void setRefreshPullEnable(boolean b);

    String getQuery();

    void notifyDataSetChanged(List<SellingStatusTxModel> listDatas);

    void finishRefresh();

    void removeRetry();

    void removeLoading();

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

    void addEmptyView();

    void removeEmpty();

    String getFilter();

    String getStartDate();

    String getEndDate();

    void showFab();

    void hideFab();
}
