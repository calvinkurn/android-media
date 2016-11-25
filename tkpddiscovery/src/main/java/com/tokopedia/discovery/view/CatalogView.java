package com.tokopedia.discovery.view;

import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.discovery.adapter.browseparent.BrowseCatalogAdapter;
import com.tokopedia.discovery.dynamicfilter.model.DynamicFilterModel;
import com.tokopedia.discovery.model.BrowseCatalogModel;

import java.util.List;

/**
 * Created by Erry on 6/30/2016.
 */
public interface CatalogView extends BaseView {
    void setupRecyclerView();
    void initAdapter();
    void notifyChangeData(List<BrowseCatalogAdapter.CatalogModel> model, PagingHandler.PagingHandlerModel pagingHandlerModel);
    boolean isLoading();
    int getStartIndexForQuery(String TAG);
    int getPage(String TAG);
    int getDataSize();
    void onCallNetwork();
    BrowseCatalogModel getDataModel();
    void setDynamicFilterAtrribute(DynamicFilterModel.Data filterAtrribute, int activeTab);
}
