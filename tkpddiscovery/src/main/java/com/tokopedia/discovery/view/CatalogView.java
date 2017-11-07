package com.tokopedia.discovery.view;

import com.tokopedia.core.discovery.model.DataValue;
import com.tokopedia.core.network.entity.discovery.BrowseCatalogModel;
import com.tokopedia.core.network.entity.discovery.CatalogModel;
import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.util.PagingHandler;

import java.util.List;

/**
 * Created by Erry on 6/30/2016.
 */
public interface CatalogView extends BaseView {
    void setupRecyclerView();
    void initAdapter();

    void notifyChangeData(List<CatalogModel> model, PagingHandler.PagingHandlerModel pagingHandlerModel);
    boolean isLoading();
    int getStartIndexForQuery(String TAG);
    int getPage(String TAG);
    int getDataSize();
    void onCallNetwork();
    BrowseCatalogModel getDataModel();
    void setLoading(boolean isLoading);

    void displayEmptyResult();
    void setDynamicFilterAtrribute(DataValue filterAtrribute, int activeTab);
}
