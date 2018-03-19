package com.tokopedia.shop.product.view.mapper;

import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;
import com.tokopedia.shop.common.util.TextApiUtils;
import com.tokopedia.shop.common.util.WishListUtils;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductBadge;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductCampaign;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductLabel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 2/25/18.
 */

public class ShopProductMapper {

    private static final String BADGE_FREE_RETURN = "Free Return";
    private static final String LABEL_CASHBACK = "Cashback";
    private static final String LABEL_PERCENTAGE = "%";

    public List<ShopProductViewModel> convertFromShopProduct(List<ShopProduct> shopProductList) {
        List<ShopProductViewModel> shopProductViewModelList = new ArrayList<>();
        for (ShopProduct shopProduct : shopProductList) {
            ShopProductViewModel shopProductViewModel = convertFromShopProduct(shopProduct);
            shopProductViewModelList.add(shopProductViewModel);
        }
        return shopProductViewModelList;
    }

    private ShopProductViewModel convertFromShopProduct(ShopProduct shopProduct) {
        ShopProductViewModel shopProductViewModel = new ShopProductViewModel();

        shopProductViewModel.setId(shopProduct.getProductId());
        shopProductViewModel.setName(shopProduct.getProductName());
        shopProductViewModel.setDisplayedPrice(shopProduct.getProductPrice());
        shopProductViewModel.setImageUrl(shopProduct.getProductImage());
        shopProductViewModel.setImageUrl300(shopProduct.getProductImage300());
        shopProductViewModel.setImageUrl700(shopProduct.getProductImage700());
        shopProductViewModel.setProductUrl(shopProduct.getProductUrl());
//        shopProductViewModel.setRating(); Api not support
        shopProductViewModel.setPo(TextApiUtils.isValueTrue(shopProduct.getProductPreorder()));
        shopProductViewModel.setTotalReview(Integer.valueOf(shopProduct.getProductReviewCount()));
        shopProductViewModel.setWholesale(TextApiUtils.isValueTrue(shopProduct.getProductWholesale()));
        if (shopProduct.getBadges() != null && shopProduct.getBadges().size() > 0) {
            for (ShopProductBadge badge : shopProduct.getBadges()) {
                if (badge.getTitle().equalsIgnoreCase(BADGE_FREE_RETURN)) {
                    shopProductViewModel.setFreeReturn(true);
                    break;
                }
            }
        }
        if (shopProduct.getLabels() != null && shopProduct.getLabels().size() > 0) {
            for (ShopProductLabel shopProductLabel : shopProduct.getLabels()) {
                if (shopProductLabel.getTitle().startsWith(LABEL_CASHBACK)) {
                    String cashbackText = shopProductLabel.getTitle();
                    cashbackText = cashbackText.replace(LABEL_CASHBACK, "");
                    cashbackText = cashbackText.replace(LABEL_PERCENTAGE, "");
                    double cashbackPercentage = Double.parseDouble(cashbackText.trim());
                    shopProductViewModel.setCashback(cashbackPercentage);
                    break;
                }
            }
        }
        return shopProductViewModel;
    }

    public void mergeShopProductViewModelWithWishList(List<ShopProductViewModel> shopProductViewModelList, List<String> productWishList, boolean showWishlist) {
        for (ShopProductViewModel shopProductViewModel : shopProductViewModelList) {
            shopProductViewModel.setWishList(WishListUtils.isWishList(shopProductViewModel.getId(), productWishList));
            shopProductViewModel.setShowWishList(showWishlist);
        }
    }

    public void mergeShopProductViewModelWithProductCampaigns(List<ShopProductViewModel> shopProductViewModelList, List<ShopProductCampaign> shopProductCampaignList) {
        for (ShopProductViewModel shopProductViewModel : shopProductViewModelList) {
            for (ShopProductCampaign shopProductCampaign : shopProductCampaignList) {
                if (shopProductViewModel.getId().equalsIgnoreCase(shopProductCampaign.getProductId())) {
                    shopProductViewModel.setDisplayedPrice(shopProductCampaign.getDiscountedPriceIdr());
                    shopProductViewModel.setOriginalPrice(shopProductCampaign.getOriginalPriceIdr());
                    shopProductViewModel.setDiscountPercentage(shopProductCampaign.getPercentageAmount());
                }
            }
        }
    }


    public List<ShopProductViewModel> convertFromProductFeatured(List<GMFeaturedProduct> gmFeaturedProductList) {
        List<ShopProductViewModel> shopProductViewModelList = new ArrayList<>();
        for (GMFeaturedProduct shopProduct : gmFeaturedProductList) {
            ShopProductViewModel shopProductViewModel = convertFromProductFeatured(shopProduct);
            shopProductViewModelList.add(shopProductViewModel);
        }
        return shopProductViewModelList;
    }

    private ShopProductViewModel convertFromProductFeatured(GMFeaturedProduct gmFeaturedProduct) {
        ShopProductViewModel shopProductViewModel = new ShopProductViewModel();

        shopProductViewModel.setId(gmFeaturedProduct.getProductId());
        shopProductViewModel.setName(gmFeaturedProduct.getName());
        shopProductViewModel.setDisplayedPrice(gmFeaturedProduct.getPrice());
        shopProductViewModel.setImageUrl(gmFeaturedProduct.getImageUri());
        shopProductViewModel.setProductUrl(gmFeaturedProduct.getUri());

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
}
