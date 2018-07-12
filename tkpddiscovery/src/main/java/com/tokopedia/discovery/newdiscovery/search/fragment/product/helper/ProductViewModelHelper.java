package com.tokopedia.discovery.newdiscovery.search.fragment.product.helper;

import android.content.Context;
import android.text.TextUtils;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.entity.discovery.gql.SearchProductGqlResponse;
import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.discovery.newdiscovery.domain.model.BadgeModel;
import com.tokopedia.discovery.newdiscovery.domain.model.LabelModel;
import com.tokopedia.discovery.newdiscovery.domain.model.ProductModel;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.BadgeItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.LabelItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.search.model.SuggestionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 10/9/17.
 */

public class ProductViewModelHelper {

    private static final String SEARCH_RESULT_ENHANCE_ANALYTIC = "SEARCH_RESULT_ENHANCE_ANALYTIC";
    private static final String LAST_POSITION_ENHANCE_PRODUCT = "LAST_POSITION_ENHANCE_PRODUCT";
    private static LocalCacheHandler cache;

    public static ProductViewModel convertToProductViewModelFirstPageGql(SearchProductGqlResponse gqlResponse) {
        clearPositionCache();
        return convertToProductViewModel(gqlResponse);
    }

    public static ProductViewModel convertToProductViewModelFirstPage(SearchResultModel searchResultModel) {
        clearPositionCache();
        return convertToProductViewModel(searchResultModel);
    }

    private static void clearPositionCache() {
        LocalCacheHandler.clearCache(MainApplication.getAppContext(), SEARCH_RESULT_ENHANCE_ANALYTIC);
    }

    public static ProductViewModel convertToProductViewModel(SearchProductGqlResponse gqlResponse) {
        SearchProductGqlResponse.SearchProduct searchProductResponse
                = gqlResponse.getSearchProduct();
        ProductViewModel productViewModel = new ProductViewModel();
        productViewModel.setProductList(convertToProductItemListGql(searchProductResponse.getProducts()));
        productViewModel.setQuery(searchProductResponse.getQuery());
        productViewModel.setShareUrl(searchProductResponse.getShareUrl());
        productViewModel.setHasCatalog(ListHelper.isContainItems(searchProductResponse.getCatalogs()));
        productViewModel.setSuggestionModel(createSuggestionModel(searchProductResponse));
        productViewModel.setTotalData(searchProductResponse.getCount());
        productViewModel.setFilter(gqlResponse.getDynamicAttribute().getData().getFilter());
        productViewModel.setSort(gqlResponse.getDynamicAttribute().getData().getSort());
        return productViewModel;
    }


    public static ProductViewModel convertToProductViewModel(SearchResultModel searchResultModel) {
        ProductViewModel productViewModel = new ProductViewModel();
        productViewModel.setProductList(convertToProductItemList(searchResultModel.getProductList()));
        productViewModel.setQuery(searchResultModel.getQuery());
        productViewModel.setShareUrl(searchResultModel.getShareUrl());
        productViewModel.setHasCatalog(searchResultModel.isHasCatalog());
        productViewModel.setSuggestionModel(createSuggestionModel(searchResultModel));
        productViewModel.setTotalData(searchResultModel.getTotalData());
        if (searchResultModel.getOfficialStoreBannerModel() != null) {
            productViewModel.setOfficialStoreBannerModel(searchResultModel.getOfficialStoreBannerModel());
        }
        productViewModel.setAdditionalParams(searchResultModel.getAdditionalParams());
        return productViewModel;
    }

    private static SuggestionModel createSuggestionModel(SearchProductGqlResponse.SearchProduct searchProductResponse) {
        SearchProductGqlResponse.Suggestion gqlSuggestionResponse = searchProductResponse.getSuggestion();
        SuggestionModel model = new SuggestionModel();
        model.setSuggestionText(gqlSuggestionResponse.getText());
        model.setSuggestedQuery(gqlSuggestionResponse.getQuery());
        model.setSuggestionCurrentKeyword(gqlSuggestionResponse.getCurrentKeyword());
        model.setFormattedResultCount(Long.toString(searchProductResponse.getCount()));
        return model;
    }

    private static SuggestionModel createSuggestionModel(SearchResultModel searchResultModel) {
        SuggestionModel model = new SuggestionModel();
        model.setSuggestionText(searchResultModel.getSuggestionText());
        model.setSuggestedQuery(searchResultModel.getSuggestedQuery());
        model.setSuggestionCurrentKeyword(searchResultModel.getSuggestionCurrentKeyword());
        model.setFormattedResultCount(searchResultModel.getTotalDataText());
        return model;
    }

    private static List<ProductItem> convertToProductItemListGql(List<SearchProductGqlResponse.Product> productModels) {
        List<ProductItem> productItemList = new ArrayList<>();

        int position = getLastPositionFromCache();

        for (SearchProductGqlResponse.Product productModel : productModels) {
            position++;
            productItemList.add(convertToProductItem(productModel, position));
        }

        saveLastPositionToCache(position);

        return productItemList;
    }

    private static List<ProductItem> convertToProductItemList(List<ProductModel> productModels) {
        List<ProductItem> productItemList = new ArrayList<>();

        int position = getLastPositionFromCache();

        for (ProductModel productModel : productModels) {
            position++;
            productItemList.add(convertToProductItem(productModel, position));
        }

        saveLastPositionToCache(position);

        return productItemList;
    }

    private static int getLastPositionFromCache() {
        if (cache == null) {
            cache = new LocalCacheHandler(MainApplication.getAppContext(), SEARCH_RESULT_ENHANCE_ANALYTIC);
        }
        return cache.getInt(LAST_POSITION_ENHANCE_PRODUCT, 0);
    }

    private static void saveLastPositionToCache(int position) {
        if (cache == null) {
            cache = new LocalCacheHandler(MainApplication.getAppContext(), SEARCH_RESULT_ENHANCE_ANALYTIC);
        }
        cache.putInt(LAST_POSITION_ENHANCE_PRODUCT, position);
        cache.applyEditor();
    }

    private static ProductItem convertToProductItem(SearchProductGqlResponse.Product productModel, int position) {
        ProductItem productItem = new ProductItem();
        productItem.setProductID(productModel.getId());
        productItem.setProductName(productModel.getName());
        productItem.setImageUrl(productModel.getImageUrl());
        productItem.setImageUrl700(productModel.getImageUrlLarge());
        productItem.setRating(productModel.getRating());
        productItem.setCountReview(productModel.getCountReview());
        //productItem.setCountCourier(productModel.getCountCourier());
        //productItem.setDiscountPercentage(productModel.getDiscountPercentage());
        //productItem.setOriginalPrice(productModel.getOriginalPrice());
        productItem.setPrice(productModel.getPrice());
        productItem.setShopID(productModel.getShop().getId());
        productItem.setShopName(productModel.getShop().getName());
        productItem.setShopCity(productModel.getShop().getCity());
        productItem.setGoldMerchant(productModel.getShop().isGoldmerchant());
        productItem.setOfficial(productModel.getShop().isOfficial());
        productItem.setWishlisted(productModel.isWishlist());
        productItem.setBadgesList(new ArrayList<>());
        productItem.setLabelList(new ArrayList<>());
        //productItem.setBadgesList(convertToBadgesItemList(productModel.getBadgesList()));
        //productItem.setLabelList(convertToLabelsItemList(productModel.getLabelList()));
        productItem.setPosition(position);
        return productItem;
    }

    private static ProductItem convertToProductItem(ProductModel productModel, int position) {
        ProductItem productItem = new ProductItem();
        productItem.setProductID(productModel.getProductID());
        productItem.setProductName(productModel.getProductName());
        productItem.setImageUrl(productModel.getImageUrl());
        productItem.setImageUrl700(productModel.getImageUrl700());
        productItem.setRating(productModel.getRating());
        productItem.setCountReview(productModel.getCountReview());
        productItem.setCountCourier(productModel.getCountCourier());
        productItem.setDiscountPercentage(productModel.getDiscountPercentage());
        productItem.setOriginalPrice(productModel.getOriginalPrice());
        productItem.setPrice(productModel.getPrice());
        productItem.setShopID(productModel.getShopID());
        productItem.setShopName(productModel.getShopName());
        productItem.setShopCity(productModel.getShopCity());
        productItem.setGoldMerchant(productModel.isGoldMerchant());
        productItem.setOfficial(productModel.isOfficial());
        productItem.setOfficial(productModel.isOfficial());
        productItem.setWishlisted(productModel.isWishlisted());
        productItem.setBadgesList(convertToBadgesItemList(productModel.getBadgesList()));
        productItem.setLabelList(convertToLabelsItemList(productModel.getLabelList()));
        productItem.setPosition(position);
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
