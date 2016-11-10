package com.tokopedia.core.selling.presenter;

import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.selling.model.SellingStatusTxModel;

import java.util.List;

/**
 * Created by Toped10 on 7/15/2016.
 */
public interface SellingStatusTransactionView extends BaseView {
    void setupRecyclerView();

    void initAdapter();

    boolean isLoading();

    int getDataSize();

    void onCallStatusLoadMore(List<SellingStatusTxModel> data);

    void onCallNetwork();

    void displayLoadMore(boolean isLoadMore);

    void setPullEnabled(boolean isPullEnabled);

    void onNoResult();
}
