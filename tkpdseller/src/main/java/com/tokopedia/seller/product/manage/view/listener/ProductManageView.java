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

    void onSuccessLoadGoldMerchantFlag(boolean goldMerchant);

    void onErrorEditPrice(Throwable t, String productId, String price, String currencyId, String currencyText);

    void onSuccessEditPrice(String productId, String price, String currencyId, String currencyText);

    void onSuccessDeleteProduct();

    void onErrorDeleteProduct();

    void showLoadingProgress();

    void hideLoadingProgress();

    void onGetFeaturedProductList(List<String> data);

    void onErrorSetCashback();

    void onSuccessSetCashback();

    void onErrorMultipleDeleteProduct(Throwable e);

    void onSuccessMultipleDeleteProduct(int countOfSuccess, int countOfError);

    void onErrorMultipleDeleteProduct(int countOfSuccess, int countOfError);
}
