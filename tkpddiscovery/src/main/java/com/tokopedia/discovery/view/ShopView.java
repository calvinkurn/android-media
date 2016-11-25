package com.tokopedia.discovery.view;

import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.network.entity.discovery.ShopModel;
import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.util.PagingHandler;

import java.util.List;

/**
 * Created by Erry on 6/30/2016.
 */
public interface ShopView extends BaseView {
    void setupRecyclerView();
    void initAdapter();

    void onCallProductServiceLoadMore(List<ShopModel> model, PagingHandler.PagingHandlerModel pagingHandlerModel);
    boolean isLoading();
    int getStartIndexForQuery(String TAG);
    int getPage(String TAG);
    int getDataSize();
    void setLoading(boolean isLoading);
    void setDynamicFilterAtrribute(DynamicFilterModel.Data filterAtrribute, int activeTab);
}
