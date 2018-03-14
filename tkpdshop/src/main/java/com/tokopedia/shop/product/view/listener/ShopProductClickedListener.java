package com.tokopedia.shop.product.view.listener;

import com.tokopedia.shop.product.view.model.ShopProductViewModel;

/**
 * Created by normansyahputa on 2/24/18.
 */

public interface ShopProductClickedListener {

    void onWishListClicked(ShopProductViewModel shopProductViewModel);

    void onProductClicked(ShopProductViewModel shopProductViewModel, int adapterPosition);

    void onLastItemVisible();
}
