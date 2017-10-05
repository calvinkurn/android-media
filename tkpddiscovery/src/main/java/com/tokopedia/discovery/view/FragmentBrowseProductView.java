package com.tokopedia.discovery.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.network.entity.intermediary.Data;
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
     *
     * @param TAG
     * @return
     */
    int getDataSize(String TAG);

    void setupAdapter();

    boolean setupRecyclerView();

    void onCallProductServiceResult(List<ProductItem> model, PagingHandler.PagingHandlerModel pagingHandlerModel);

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

    String getUserId();

    void onWishlistButtonClick(ProductItem data, int position);

    void finishLoadingWishList();

    void loadingWishList();

    void updateWishListStatus(boolean isWishlist, int position);

    void navigateToActivityRequest(Intent intent, int requestCode);

    void navigateToActivity(Intent intent);

    void showToastMessage(String message);

    void showDialog(Dialog dialog);

    void closeView();

    void showWishListRetry(String errorMessage);

    void updateTotalProduct(Long totalProduct);

    void displayEmptyResult();

    void setLoading(boolean isLoading);

    void actionSuccessRemoveFromWishlist(Integer productId);

    void actionSuccessAddToWishlist(Integer productId);
}
