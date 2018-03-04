package com.tokopedia.shop.product.view.mapper;

import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;
import com.tokopedia.shop.common.util.TextApiUtils;
import com.tokopedia.shop.common.util.WishListUtils;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductBadge;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductSort;
import com.tokopedia.shop.product.view.model.ShopProductSortModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 2/25/18.
 */

public class ShopProductMapper {

    private static final String BADGE_FREE_RETURN = "Free Return";

    public List<ShopProductViewModel> convertFromShopProduct(List<ShopProduct> shopProductList, List<String> productWishList) {
        List<ShopProductViewModel> shopProductViewModelList = new ArrayList<>();
        for (ShopProduct shopProduct: shopProductList) {
            ShopProductViewModel shopProductViewModel = convertFromShopProduct(shopProduct);
            shopProductViewModel.setWishList(WishListUtils.isWishList(shopProduct.getProductId(), productWishList));
            shopProductViewModelList.add(shopProductViewModel);
        }
        return shopProductViewModelList;
    }

    private ShopProductViewModel convertFromShopProduct(ShopProduct shopProduct) {
        ShopProductViewModel shopProductViewModel = new ShopProductViewModel();

        shopProductViewModel.setId(shopProduct.getProductId());
        shopProductViewModel.setName(shopProduct.getProductName());
        shopProductViewModel.setPrice(shopProduct.getProductPrice());
        shopProductViewModel.setImageUrl(shopProduct.getProductImage());
//        shopProductViewModel.setRating(); Api not support
//        shopProductViewModel.setCashback(shopProduct.get); Api not support
        shopProductViewModel.setPo(TextApiUtils.isValueTrue(shopProduct.getProductPreorder()));
        shopProductViewModel.setTotalReview(Integer.valueOf(shopProduct.getProductReviewCount()));
        shopProductViewModel.setWholesale(TextApiUtils.isValueTrue(shopProduct.getProductWholesale()));
        if (shopProduct.getBadges() != null && shopProduct.getBadges().size() > 0) {
            for (ShopProductBadge badge : shopProduct.getBadges()) {
                if (badge.getTitle().equalsIgnoreCase(BADGE_FREE_RETURN)){
                    shopProductViewModel.setFreeReturn(true);
                    break;
                }
            }
        }
        return shopProductViewModel;
    }


    public List<ShopProductViewModel> convertFromProductFeatured(List<GMFeaturedProduct> gmFeaturedProductList, List<String> productWishList) {
        List<ShopProductViewModel> shopProductViewModelList = new ArrayList<>();
        for (GMFeaturedProduct shopProduct: gmFeaturedProductList) {
            ShopProductViewModel shopProductViewModel = convertFromProductFeatured(shopProduct);
            shopProductViewModel.setWishList(WishListUtils.isWishList(shopProduct.getProductId(), productWishList));
            shopProductViewModelList.add(shopProductViewModel);
        }
        return shopProductViewModelList;
    }

    private ShopProductViewModel convertFromProductFeatured(GMFeaturedProduct gmFeaturedProduct) {
        ShopProductViewModel shopProductViewModel = new ShopProductViewModel();

        shopProductViewModel.setId(gmFeaturedProduct.getProductId());
        shopProductViewModel.setName(gmFeaturedProduct.getName());
        shopProductViewModel.setPrice(gmFeaturedProduct.getPrice());
        shopProductViewModel.setImageUrl(gmFeaturedProduct.getImageUri());

        shopProductViewModel.setTotalReview(gmFeaturedProduct.getTotalReview());
        shopProductViewModel.setRating(gmFeaturedProduct.getRating());
        if (gmFeaturedProduct.getCashbackDetail() != null) {
            shopProductViewModel.setCashback(gmFeaturedProduct.getCashbackDetail().getCashbackPercent());
        }
        shopProductViewModel.setWholesale(gmFeaturedProduct.isWholesale());
        shopProductViewModel.setPo(gmFeaturedProduct.isPreorder());
        shopProductViewModel.setFreeReturn(gmFeaturedProduct.isReturnable());
        return shopProductViewModel;
    }

    public List<ShopProductSortModel> convertSort(List<ShopProductSort> shopProductSortList) {
        List<ShopProductSortModel> result = new ArrayList<>();
        for (ShopProductSort data : shopProductSortList) {
            ShopProductSortModel shopProductFilterModel = new ShopProductSortModel();
            shopProductFilterModel.setInputType(data.getInputType());
            shopProductFilterModel.setKey(data.getKey());
            shopProductFilterModel.setName(data.getName());
            shopProductFilterModel.setValue(data.getValue());
            result.add(shopProductFilterModel);
        }
        return result;
    }
}
