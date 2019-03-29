package com.tokopedia.discovery.newdiscovery.category.presentation.product.helper;

import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.BadgeItem;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.LabelItem;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.domain.model.BadgeModel;
import com.tokopedia.discovery.newdiscovery.domain.model.LabelModel;
import com.tokopedia.discovery.newdiscovery.domain.model.ProductModel;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 10/9/17.
 */

public class CategoryModelHelper {

    public static ProductViewModel convertToProductViewModel(SearchResultModel searchResultModel, CategoryHeaderModel categoryHeaderModel) {
        ProductViewModel productViewModel = new ProductViewModel();
        productViewModel.setProductList(convertToProductItemList(searchResultModel.getProductList()));
        productViewModel.setShareUrl(searchResultModel.getShareUrl());
        productViewModel.setHasCatalog(searchResultModel.isHasCatalog());
        productViewModel.setCategoryHeaderModel(categoryHeaderModel);
        productViewModel.getCategoryHeaderModel().setTotalData(searchResultModel.getTotalData());
        return productViewModel;
    }

    public static ProductViewModel convertToProductViewModel(SearchResultModel searchResultModel) {
        ProductViewModel productViewModel = new ProductViewModel();
        productViewModel.setProductList(convertToProductItemList(searchResultModel.getProductList()));
        productViewModel.setShareUrl(searchResultModel.getShareUrl());
        productViewModel.setHasCatalog(searchResultModel.isHasCatalog());
        return productViewModel;
    }

    private static List<ProductItem> convertToProductItemList(List<ProductModel> productModels) {
        List<ProductItem> productItemList = new ArrayList<>();

        for (ProductModel productModel : productModels) {
            productItemList.add(convertToProductItem(productModel));
        }
        return productItemList;
    }

    private static ProductItem convertToProductItem(ProductModel productModel) {
        ProductItem productItem = new ProductItem();
        productItem.setProductID(productModel.getProductID());
        productItem.setProductName(productModel.getProductName());
        productItem.setImageUrl(productModel.getImageUrl());
        productItem.setImageUrl700(productModel.getImageUrl700());
        productItem.setRating(productModel.getRating());
        productItem.setCountReview(productModel.getCountReview());
        productItem.setCountCourier(productModel.getCountCourier());
        productItem.setOriginalPrice(productModel.getOriginalPrice());
        productItem.setDiscountPercentage(productModel.getDiscountPercentage());
        productItem.setPrice(productModel.getPrice());
        productItem.setPriceRange(productModel.getPriceRange());
        productItem.setShopID(productModel.getShopID());
        productItem.setShopName(productModel.getShopName());
        productItem.setShopCity(productModel.getShopCity());
        productItem.setGoldMerchant(productModel.isGoldMerchant());
        productItem.setOfficial(productModel.isOfficial());
        productItem.setWishlisted(productModel.isWishlisted());
        productItem.setBadgesList(convertToBadgesItemList(productModel.getBadgesList()));
        productItem.setLabelList(convertToLabelsItemList(productModel.getLabelList()));
        productItem.setTopLabel(productModel.getTopLabel());
        productItem.setBottomLabel(productModel.getBottomLabel());
        productItem.setCategoryBreadcrumb(productModel.getCategoryBreadcrumb());
        return productItem;
    }

    private static List<BadgeItem> convertToBadgesItemList(List<BadgeModel> badgesList) {
        List<BadgeItem> badgeItemList = new ArrayList<>();

        for (BadgeModel badgeModel : badgesList) {
            badgeItemList.add(convertToBadgeItem(badgeModel));
        }
        return badgeItemList;
    }

    private static BadgeItem convertToBadgeItem(BadgeModel badgeModel) {
        BadgeItem badgeItem = new BadgeItem();
        badgeItem.setImageUrl(badgeModel.getImageUrl());
        badgeItem.setTitle(badgeModel.getTitle());
        badgeItem.setShown(badgeModel.isShown());
        return badgeItem;
    }

    private static List<LabelItem> convertToLabelsItemList(List<LabelModel> labelList) {
        List<LabelItem> labelItemList = new ArrayList<>();

        for (LabelModel labelModel : labelList) {
            labelItemList.add(convertToLabelItem(labelModel));
        }
        return labelItemList;
    }

    private static LabelItem convertToLabelItem(LabelModel labelModel) {
        LabelItem labelItem = new LabelItem();
        labelItem.setTitle(labelModel.getTitle());
        labelItem.setColor(labelModel.getColor());
        return labelItem;
    }
}
