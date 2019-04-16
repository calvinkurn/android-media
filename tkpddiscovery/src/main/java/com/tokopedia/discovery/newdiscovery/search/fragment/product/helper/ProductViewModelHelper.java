package com.tokopedia.discovery.newdiscovery.search.fragment.product.helper;

import android.text.TextUtils;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.GlobalNavViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.RelatedSearchModel;
import com.tokopedia.core.network.entity.discovery.GuidedSearchResponse;
import com.tokopedia.discovery.newdiscovery.domain.gql.SearchProductGqlResponse;
import com.tokopedia.discovery.newdiscovery.domain.model.BadgeModel;
import com.tokopedia.discovery.newdiscovery.domain.model.LabelModel;
import com.tokopedia.discovery.newdiscovery.domain.model.ProductModel;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.BadgeItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.GuidedSearchViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.LabelItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.search.model.SuggestionModel;
import com.tokopedia.topads.sdk.domain.model.Badge;
import com.tokopedia.topads.sdk.domain.model.Data;

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
        productViewModel.setAdsModel(gqlResponse.getTopAdsModel());
        productViewModel.setGlobalNavViewModel(convertToViewModel(gqlResponse.getGlobalNavModel()));
        productViewModel.setCpmModel(gqlResponse.getCpmModel());
        if (searchProductResponse.getRelated() != null &&
                !TextUtils.isEmpty(searchProductResponse.getRelated().getRelatedKeyword())) {
            productViewModel.setRelatedSearchModel(convertToRelatedSearchModel(searchProductResponse.getRelated()));
        } else if (gqlResponse.getGuidedSearchResponse() != null) {
            productViewModel.setGuidedSearchViewModel(convertToGuidedSearchViewModel(gqlResponse.getGuidedSearchResponse()));
        }
        productViewModel.setProductList(convertToProductItemListGql(searchProductResponse.getProducts()));
        productViewModel.setAdsModel(gqlResponse.getTopAdsModel());
        productViewModel.setQuery(searchProductResponse.getQuery());
        productViewModel.setShareUrl(searchProductResponse.getShareUrl());
        productViewModel.setSuggestionModel(createSuggestionModel(searchProductResponse));
        productViewModel.setTotalData(searchProductResponse.getCount());
        if (gqlResponse.getDynamicFilterModel() != null) {
            productViewModel.setDynamicFilterModel(gqlResponse.getDynamicFilterModel());
        }
        if (gqlResponse.getQuickFilterModel() != null) {
            productViewModel.setQuickFilterModel(gqlResponse.getQuickFilterModel());
        }
        productViewModel.setAdditionalParams(gqlResponse.getSearchProduct().getAdditionalParams());
        return productViewModel;
    }

    private static GlobalNavViewModel convertToViewModel(SearchProductGqlResponse.GlobalNavModel globalNavModel) {
        return new GlobalNavViewModel(
                globalNavModel.getData().getTitle(),
                globalNavModel.getData().getSeeAllApplink(),
                globalNavModel.getData().getSeeAllUrl(),
                convertToViewModel(globalNavModel.getData().getGlobalNavItems())
        );
    }

    private static List<GlobalNavViewModel.Item> convertToViewModel(List<SearchProductGqlResponse.GlobalNavItem> globalNavItems) {
        List<GlobalNavViewModel.Item> itemList = new ArrayList<>();

        for (SearchProductGqlResponse.GlobalNavItem item : globalNavItems) {
            itemList.add(new GlobalNavViewModel.Item(
                    item.getName(),
                    item.getInfo(),
                    item.getImageUrl(),
                    item.getApplink(),
                    item.getUrl()
            ));
        }

        return itemList;
    }

    private static RelatedSearchModel convertToRelatedSearchModel(SearchProductGqlResponse.Related related) {
        RelatedSearchModel relatedSearchModel = new RelatedSearchModel();
        relatedSearchModel.setRelatedKeyword(related.getRelatedKeyword());

        List<RelatedSearchModel.OtherRelated> otherRelatedList = new ArrayList<>();
        for (SearchProductGqlResponse.OtherRelated otherRelatedResponse : related.getOtherRelated()) {
            RelatedSearchModel.OtherRelated otherRelatedViewModel = new RelatedSearchModel.OtherRelated();
            otherRelatedViewModel.setKeyword(otherRelatedResponse.getKeyword());
            otherRelatedViewModel.setUrl(otherRelatedResponse.getUrl());
            otherRelatedList.add(otherRelatedViewModel);
        }
        relatedSearchModel.setOtherRelated(otherRelatedList);

        return relatedSearchModel;
    }

    private static GuidedSearchViewModel convertToGuidedSearchViewModel(GuidedSearchResponse guidedSearchResponse) {
        GuidedSearchViewModel model = new GuidedSearchViewModel();
        List<GuidedSearchViewModel.Item> itemList = new ArrayList<>();

        if (guidedSearchResponse.getData() != null) {
            for (GuidedSearchResponse.GuidedSearchItem item : guidedSearchResponse.getData()) {
                itemList.add(mappingGuidedSearchItem(item));
            }
        }
        model.setItemList(itemList);
        return model;
    }

    private static GuidedSearchViewModel.Item mappingGuidedSearchItem(GuidedSearchResponse.GuidedSearchItem networkItem) {
        GuidedSearchViewModel.Item viewModelItem = new GuidedSearchViewModel.Item();
        viewModelItem.setKeyword(networkItem.getKeyword());
        viewModelItem.setUrl(networkItem.getUrl());
        return viewModelItem;
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
        String suggestedQuery
                = NetworkParamHelper.getQueryValue(gqlSuggestionResponse.getQuery());
        model.setSuggestedQuery(suggestedQuery);
        model.setSuggestionCurrentKeyword(gqlSuggestionResponse.getCurrentKeyword());
        model.setFormattedResultCount(searchProductResponse.getCountText());
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
        productItem.setCountCourier(productModel.getCourierCount());
        productItem.setDiscountPercentage(productModel.getDiscountPercentage());
        productItem.setOriginalPrice(productModel.getOriginalPrice());
        productItem.setPrice(productModel.getPrice());
        productItem.setPriceRange(productModel.getPriceRange());
        productItem.setShopID(productModel.getShop().getId());
        productItem.setShopName(productModel.getShop().getName());
        productItem.setShopCity(productModel.getShop().getCity());
        productItem.setGoldMerchant(productModel.getShop().isGoldmerchant());
        productItem.setOfficial(productModel.getShop().isOfficial());
        productItem.setWishlisted(productModel.isWishlist());
        productItem.setBadgesList(convertToBadgesItemListGql(productModel.getBadges()));
        productItem.setLabelList(convertToLabelsItemListGql(productModel.getLabels()));
        productItem.setPosition(position);
        productItem.setTopLabel(isContainItems(productModel.getTopLabel()) ? productModel.getTopLabel().get(0) : "");
        productItem.setBottomLabel(isContainItems(productModel.getBottomLabel()) ? productModel.getBottomLabel().get(0) : "");
        productItem.setCategoryID(productModel.getCategoryId());
        productItem.setCategoryName(productModel.getCategoryName());
        productItem.setCategoryBreadcrumb(productModel.getCategoryBreadcrumb());
        return productItem;
    }

    private static boolean isContainItems(List list) {
        return list != null && !list.isEmpty();
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
        productItem.setPriceRange(productModel.getPriceRange());
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
        productItem.setTopLabel(productModel.getTopLabel());
        productItem.setBottomLabel(productModel.getBottomLabel());
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

    private static List<BadgeItem> convertToBadgesItemListGql(List<SearchProductGqlResponse.Badge> badgesList) {
        List<BadgeItem> badgeItemList = new ArrayList<>();

        for (SearchProductGqlResponse.Badge badgeModel : badgesList) {
            badgeItemList.add(convertToBadgeItem(badgeModel));
        }
        return badgeItemList;
    }

    private static BadgeItem convertToBadgeItem(SearchProductGqlResponse.Badge badgeModel) {
        BadgeItem badgeItem = new BadgeItem();
        badgeItem.setImageUrl(badgeModel.getImageUrl());
        badgeItem.setTitle(badgeModel.getTitle());
        badgeItem.setShown(badgeModel.isShown());
        return badgeItem;
    }

    private static List<LabelItem> convertToLabelsItemListGql(List<SearchProductGqlResponse.Label> labelList) {
        List<LabelItem> labelItemList = new ArrayList<>();
        for (SearchProductGqlResponse.Label labelModel : labelList) {
            labelItemList.add(convertToLabelItem(labelModel));
        }
        return labelItemList;
    }

    private static LabelItem convertToLabelItem(SearchProductGqlResponse.Label labelModel) {
        LabelItem labelItem = new LabelItem();
        labelItem.setTitle(labelModel.getTitle());
        labelItem.setColor(labelModel.getColor());
        return labelItem;
    }

    public static List<Visitable> convertToListOfVisitable(ProductViewModel productViewModel) {
        List<Visitable> list = new ArrayList<>(productViewModel.getProductList());
        int j = 0;
        for (int i = 0; i < productViewModel.getTotalItem(); i++) {
            try {
                if (productViewModel.getAdsModel().getTemplates().get(i).isIsAd()) {
                    Data topAds = productViewModel.getAdsModel().getData().get(j);
                    ProductItem item = new ProductItem();
                    item.setProductID(topAds.getProduct().getId());
                    item.setTopAds(true);
                    item.setTopadsImpressionUrl(topAds.getProduct().getImage().getS_url());
                    item.setTopadsClickUrl(topAds.getProductClickUrl());
                    item.setTopadsWishlistUrl(topAds.getProductWishlistUrl());
                    item.setProductName(topAds.getProduct().getName());
                    if(!topAds.getProduct().getTopLabels().isEmpty()) {
                        item.setTopLabel(topAds.getProduct().getTopLabels().get(0));
                    }
                    if(!topAds.getProduct().getBottomLabels().isEmpty()) {
                        item.setBottomLabel(topAds.getProduct().getBottomLabels().get(0));
                    }
                    item.setPrice(topAds.getProduct().getPriceFormat());
                    item.setShopCity(topAds.getShop().getLocation());
                    item.setImageUrl(topAds.getProduct().getImage().getS_ecs());
                    item.setImageUrl700(topAds.getProduct().getImage().getM_ecs());
                    item.setWishlisted(topAds.getProduct().isWishlist());
                    item.setRating(topAds.getProduct().getProductRating());
                    item.setCountReview(convertCountReviewFormatToInt(topAds.getProduct().getCountReviewFormat()));
                    item.setBadgesList(mapBadges(topAds.getShop().getBadges()));
                    item.setNew(topAds.getProduct().isProductNewLabel());
                    list.add(i, item);
                    j++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private static int convertCountReviewFormatToInt(String countReviewFormat) {
        String countReviewString = countReviewFormat.replaceAll("[^\\d]", "");

        try {
            return Integer.parseInt(countReviewString);
        }
        catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static List<BadgeItem> mapBadges(List<Badge> badges) {
        List<BadgeItem> items = new ArrayList<>();
        for (Badge b:badges) {
            items.add(new BadgeItem(b.getImageUrl(), b.getTitle(), b.isShow()));
        }
        return items;
    }
}
