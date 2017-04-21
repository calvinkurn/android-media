package com.tokopedia.discovery.view;

import android.os.Bundle;

import com.tokopedia.core.network.entity.categoriesHades.Data;
import com.tokopedia.core.network.entity.discovery.BrowseProductModel;
import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.discovery.adapter.ProductAdapter;
import com.tokopedia.discovery.fragment.ProductFragment;

import java.util.List;

/**
 * Created by raditya.gumay on 18/03/2016.
 */
public interface FragmentBrowseProductView extends BaseView {
    /**
     * get data size based on Fragment TAG
     * {@link ProductFragment#TAG}
     * @param TAG
     * @return
     */
    int getDataSize(String TAG);
    void setupAdapter();
    void setupRecyclerView();
    void onCallProductServiceResult2(Long totalProduct, List<ProductItem> model, PagingHandler.PagingHandlerModel pagingHandlerModel);
    void onCallProductServiceLoadMore(List<ProductItem> model, PagingHandler.PagingHandlerModel pagingHandlerModel);
    void setHotlistData(List<ProductItem> model, PagingHandler.PagingHandlerModel pagingHandlerModel);
    boolean isLoading();
    int getStartIndexForQuery(String TAG);
    int getPage(String TAG);
    void savePaging(Bundle savedState);
    void restorePaging(Bundle savedState);
    void addHotListHeader(ProductAdapter.HotListBannerModel hotListBannerModel);
    void addCategoryHeader(Data category);
    BrowseProductModel getDataModel();

}
