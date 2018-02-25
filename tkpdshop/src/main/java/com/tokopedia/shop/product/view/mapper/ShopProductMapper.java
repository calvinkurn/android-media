package com.tokopedia.shop.product.view.mapper;

import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.shop.common.util.TextApiUtils;
import com.tokopedia.shop.common.util.WishListUtils;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductBadge;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 2/25/18.
 */

public class ShopProductMapper {

    private static final String BADGGE_FREE_RETURN = "Free Return";

    public List<ShopProductViewModel> convert(List<ShopProduct> shopProductList, List<String> productWishList) {
        List<ShopProductViewModel> shopProductViewModelList = new ArrayList<>();
        for (ShopProduct shopProduct: shopProductList) {
            ShopProductViewModel shopProductViewModel = convert(shopProduct);
            shopProductViewModel.setWishList(WishListUtils.isWishList(shopProduct.getProductId(), productWishList));
            shopProductViewModelList.add(shopProductViewModel);
        }
        return shopProductViewModelList;
    }

    private ShopProductViewModel convert(ShopProduct shopProduct) {
        ShopProductViewModel shopProductViewModel = new ShopProductViewModel();

        shopProductViewModel.setId(shopProduct.getProductId());
        shopProductViewModel.setName(shopProduct.getProductName());
        shopProductViewModel.setPrice(shopProduct.getProductPrice());
        shopProductViewModel.setImageUrl(shopProduct.getProductImage());

        shopProductViewModel.setWholesale(TextApiUtils.isValueTrue(shopProduct.getProductWholesale()));
        shopProductViewModel.setPo(TextApiUtils.isValueTrue(shopProduct.getProductPreorder()));
        if (shopProduct.getBadges() != null && shopProduct.getBadges().size() > 0) {
            for (ShopProductBadge badge : shopProduct.getBadges()) {
                if (badge.getTitle().equalsIgnoreCase(BADGGE_FREE_RETURN)){
                    shopProductViewModel.setFreeReturn(true);
                    break;
                }
            }
        }
        return shopProductViewModel;
    }
}
