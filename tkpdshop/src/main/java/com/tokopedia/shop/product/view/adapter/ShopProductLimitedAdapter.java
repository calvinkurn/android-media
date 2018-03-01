package com.tokopedia.shop.product.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.shop.product.view.model.ShopProductBaseViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedProductViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

/**
 * Created by zulfikarrahman on 3/1/18.
 */

public class ShopProductLimitedAdapter extends BaseListAdapter<ShopProductBaseViewModel, ShopProductLimitedAdapterTypeFactory> {
    public ShopProductLimitedAdapter(ShopProductLimitedAdapterTypeFactory adapterTypeFactory) {
        super(adapterTypeFactory);
    }

    public void updateWishListStatus(String productId, boolean wishList) {
        for (int i = 0; i < getData().size(); i++) {
            ShopProductBaseViewModel shopProductViewModel = getData().get(i);
            if (shopProductViewModel instanceof ShopProductLimitedFeaturedViewModel) {
                for (ShopProductViewModel shopProductViewModelContent : ((ShopProductLimitedFeaturedViewModel) shopProductViewModel).getShopProductViewModelList()) {
                    if (shopProductViewModelContent.getId().equalsIgnoreCase(productId)) {
                        shopProductViewModelContent.setWishList(wishList);
                        break;
                    }
                }
                notifyItemChanged(i);
                return;
            }

            if (shopProductViewModel instanceof ShopProductLimitedProductViewModel) {
                for (ShopProductViewModel shopProductViewModelContent : ((ShopProductLimitedProductViewModel) shopProductViewModel).getShopProductViewModelList()) {
                    if (shopProductViewModelContent.getId().equalsIgnoreCase(productId)) {
                        shopProductViewModelContent.setWishList(wishList);
                        break;
                    }
                }
                notifyItemChanged(i);
                return;
            }
        }
    }

}
