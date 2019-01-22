package com.tokopedia.seller.reputation.view;

import com.tokopedia.base.list.seller.common.util.ItemType;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.reputation.view.adapter.SellerReputationAdapter;
import com.tokopedia.seller.reputation.view.model.SetDateHeaderModel;

import java.util.List;

/**
 * Created by normansyahputa on 3/15/17.
 */

public interface SellerReputationView extends CustomerView {
    void dismissSnackbar();

    void setRetry(String error);

    void showEmptyState(String error);

    void setRetry();

    void showEmptyState();

    void showRefreshing();

    void refresh();

    boolean isRefreshing();

    void setActionsEnabled(Boolean isEnabled);

    void removeError();

    void showErrorMessage(String errorMessage);

    void setLoading();

    SellerReputationAdapter getAdapter();

    void finishLoading();

    String getEndDate();

    void setEndDate(String date);

    String getStartDate();

    void setStartDate(String date);

    /**
     * @param loadmoreflag true means there is no load more, false mean have load more.
     */
    void setLoadMoreFlag(boolean loadmoreflag);

    void loadData(List<ItemType> datas);

    void loadMore(List<ItemType> datas);

    void loadShopInfo(ShopModel shopModel);

    SetDateHeaderModel getHeaderModel();
}
