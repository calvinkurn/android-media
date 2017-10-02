package com.tokopedia.seller.product.manage.view.presenter;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.seller.product.manage.view.listener.ProductManageView;

import java.util.List;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public interface ProductManagePresenter extends CustomerPresenter<ProductManageView> {
    void editPrice(String productId, String price, String priceCurrency);
    void deleteProduct(String productId);
    void getListProduct(int page, String keywordFilter);
    void getListFeaturedProduct();
    void deleteListProduct(List<String> productIds);
    void setCashback(String productId, String cashback);
}
