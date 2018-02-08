package com.tokopedia.seller.product.manage.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.seller.base.view.listener.BaseListViewListener;
import com.tokopedia.seller.product.manage.view.model.ProductManageViewModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public interface ProductManageView extends BaseListViewListener<ProductManageViewModel> {

    void onSearchLoaded(@NonNull List<ProductManageViewModel> list, int totalItem, boolean hasNextPage);

    void onSuccessGetShopInfo(boolean goldMerchant, boolean officialStore);

    void onSuccessGetFeaturedProductList(List<String> data);

    void onErrorEditPrice(Throwable t, String productId, String price, String currencyId, String currencyText);

    void onSuccessEditPrice(String productId, String price, String currencyId, String currencyText);

    void onErrorSetCashback(Throwable t, String productId, int cashback);

    void onSuccessSetCashback(String productId, int cashback);

    void onErrorMultipleDeleteProduct(Throwable e, List<String> productIdDeletedList, List<String> productIdFailToDeleteList);

    void onSuccessMultipleDeleteProduct();

    void showLoadingProgress();

    void hideLoadingProgress();
}
