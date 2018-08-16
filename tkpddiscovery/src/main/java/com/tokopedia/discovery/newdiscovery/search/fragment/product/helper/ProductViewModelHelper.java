package com.tokopedia.discovery.newdiscovery.search.fragment.product.helper;

import com.google.gson.Gson;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.discovery.newdiscovery.domain.gql.SearchProductGqlResponse;
import com.tokopedia.discovery.newdiscovery.domain.model.BadgeModel;
import com.tokopedia.discovery.newdiscovery.domain.model.LabelModel;
import com.tokopedia.discovery.newdiscovery.domain.model.ProductModel;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.BadgeItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.LabelItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.search.model.SuggestionModel;
import com.tokopedia.topads.sdk.domain.model.CpmModel;

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
        productViewModel.setCpmModel(gqlResponse.getCpmModel());
        productViewModel.setProductList(convertToProductItemListGql(searchProductResponse.getProducts()));
        productViewModel.setAdsModel(gqlResponse.getTopAdsModel());
        productViewModel.setQuery(searchProductResponse.getQuery());
        productViewModel.setShareUrl(searchProductResponse.getShareUrl());
        productViewModel.setHasCatalog(ListHelper.isContainItems(searchProductResponse.getCatalogs()));
        productViewModel.setSuggestionModel(createSuggestionModel(searchProductResponse));
        productViewModel.setTotalData(searchProductResponse.getCount());
        if (gqlResponse.getDynamicFilterModel() != null) {
            productViewModel.setDynamicFilterModel(gqlResponse.getDynamicFilterModel());
        }
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
        String suggestedQuery
                = NetworkParamHelper.getQueryValue(gqlSuggestionResponse.getQuery());
        model.setSuggestedQuery(suggestedQuery);
        model.setSuggestionCurrentKeyword(gqlSuggestionResponse.getCurrentKeyword());
        //model.setFormattedResultCount(Long.toString(searchProductResponse.getCount()));
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
        //productItem.setPriceRange(productModel.getPriceRange());
        productItem.setShopID(productModel.getShop().getId());
        productItem.setShopName(productModel.getShop().getName());
        productItem.setShopCity(productModel.getShop().getCity());
        productItem.setGoldMerchant(productModel.getShop().isGoldmerchant());
        productItem.setOfficial(productModel.getShop().isOfficial());
        //productItem.setWishlisted(productModel.isWishlist());
        productItem.setBadgesList(convertToBadgesItemListGql(productModel.getBadges()));
        productItem.setLabelList(convertToLabelsItemListGql(productModel.getLabels()));
        productItem.setPosition(position);
        //productItem.setTopLabel(productModel.getTopLabel());
        //productItem.setBottomLabel(productModel.getBottomLabel());
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
        //badgeItem.setShown(badgeModel.isShown());
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

    public static CpmModel getCpmDummy() {
        return new Gson().fromJson("{\n" +
                "\t\"status\": {\n" +
                "\t\t\"error_code\": 0,\n" +
                "\t\t\"message\": \"OK\"\n" +
                "\t},\n" +
                "\t\"header\": {\n" +
                "\t\t\"total_data\": 1,\n" +
                "\t\t\"process_time\": 0.008716949,\n" +
                "\t\t\"meta\": {\n" +
                "\t\t\t\"ab_test\": \"N\",\n" +
                "\t\t\t\"display\": \"cpm\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"data\": [{\n" +
                "\t\t\"id\": \"20083201\",\n" +
                "\t\t\"ad_ref_key\": \"\",\n" +
                "\t\t\"redirect\": \"https://www.tokopedia.com/anker-indonesia\",\n" +
                "\t\t\"ad_click_url\": \"https://ta.tokopedia.com/promo/v1/clicks/HAtaHAndHpUFH_tp6sJEH_1poAeFHp1hHAtF6sHaHsU7H_jEHmdFHAKNHAJEg9BGqMzUZMggQj2fgAo6QJBkQfBoepzR_1zgH1YJ__uoqMra_92C81O1__-6zcW2_JoGqMzUZMgsHBgtyfO6Q7BkQfBoe7BpZ3O7QcuygIgsQu-Myp-6PMoWu3Bvq1BR_c2zHJOJ__z6qBja_1zCH7Y1z9x681BpZ37OPM-W_Mh-qMY2_M2o8jO9u_-vuOu7_uzs8BBNHACoHjgW_Vztq1O1_9zoz7jF3BP6Q1O1gpCvzcxDZ3BRq3UpZSCqHMhO3Aom83Ua1sVgHO-MyuPzq1Y2Z9P-q9P2ysoGrVtaQIuyH7N5ysomgMV913Bvq1BRZ3BRq3oyuMhsQMhMgJPcQMoNZ_g-q1BpZ3N6qMUpZMhyHj2Nysoj8B2_Z_g-qjVO_1zC81NEz9268B1O_7zC81NJu_o6qjuE_jo-r7BX_M2iH72D3Ao6QVByZM2xe7jfZ32CP1OJe_zvuJVR_7zsH7O1e_VozcDp_Bz0q3gzv_7?alg=stm\\u0026ab_test=N\\u0026keywords=samsung\\u0026cpm_template_req=3%2C4%2C5\\u0026src=search\\u0026is_search=1\\u0026uid=0\\u0026r=https%3A%2F%2Fwww.tokopedia.com%2Fanker-indonesia\\u0026t=android\\u0026sid=fa125e42-dcce-4a2c-872e-f4b06ee5f301\\u0026number_of_ads=1\\u0026page=1\\u0026number_ads_req=1\\u0026n_candidate_ads=1\\u0026template_id_used=3\",\n" +
                "\t\t\"headline\": {\n" +
                "\t\t\t\"template_id\": \"3\",\n" +
                "\t\t\t\"name\": \"Anker Indonesia\",\n" +
                "\t\t\t\"image\": {\n" +
                "\t\t\t\t\"full_url\": \"https://ecs7.tokopedia.net/img/cache/100-square/shops-1/2018/7/17/1645551/1645551_ed1734d3-ff1a-4476-91a1-341fdc3639d6.jpg\",\n" +
                "\t\t\t\t\"full_ecs\": \"https://ecs7.tokopedia.net/img/cache/100-square/shops-1/2018/7/17/1645551/1645551_ed1734d3-ff1a-4476-91a1-341fdc3639d6.jpg\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"shop\": {\n" +
                "\t\t\t\t\"id\": \"1645551\",\n" +
                "\t\t\t\t\"name\": \"Anker Indonesia\",\n" +
                "\t\t\t\t\"domain\": \"anker-indonesia\",\n" +
                "\t\t\t\t\"tagline\": \"Fast Charge, Quick Charge, Premium Product\",\n" +
                "\t\t\t\t\"slogan\": \"Powerbank Terbaik Indonesia\",\n" +
                "\t\t\t\t\"location\": \"\",\n" +
                "\t\t\t\t\"city\": \"\",\n" +
                "\t\t\t\t\"gold_shop\": true,\n" +
                "\t\t\t\t\"gold_shop_badge\": true,\n" +
                "\t\t\t\t\"shop_is_official\": true,\n" +
                "\t\t\t\t\"product\": [{\n" +
                "\t\t\t\t\t\"id\": \"101093856\",\n" +
                "\t\t\t\t\t\"name\": \"Anker PowerCore+ 13400 mAh Premium - Black [A1315H11]\",\n" +
                "\t\t\t\t\t\"price_format\": \"Rp 875.700\",\n" +
                "\t\t\t\t\t\"applinks\": \"tokopedia://product/101093856\",\n" +
                "\t\t\t\t\t\"image_product\": {\n" +
                "\t\t\t\t\t\t\"product_id\": \"101093856\",\n" +
                "\t\t\t\t\t\t\"product_name\": \"Anker PowerCore+ 13400 mAh Premium - Black [A1315H11]\",\n" +
                "\t\t\t\t\t\t\"image_url\": \"https://ecs7.tokopedia.net/img/cache/200-square/product-1/2017/9/13/101093856/101093856_114a7a5a-8463-45f1-822d-7b5c2608b391_1774_1773.jpg\",\n" +
                "\t\t\t\t\t\t\"image_click_url\": \"https://ta.tokopedia.com/promo/v1/clicks/HAtaHAndHpUFH_tp6sJEH_1poAeFHp1hHAtF6sHaHsU7H_jEHmdFHAKNHAJEg9BGqMzUZMggQj2fgAo6QJBkQfBoepzR_1zgH1YJ__uoqMra_92C81O1__-6zcW2_JoGqMzUZMgsHBgtyfO6Q7BkQfBoe7BpZ3O7QcuygIgsQu-Myp-6PMoWu3Bvq1BR_c2zHJOJ__z6qBja_1zCH7Y1z9x681BpZ37OPM-W_Mh-qMY2_M2o8jO9u_-vuOu7_uzs8BBNHACoHjgW_Vztq1O1_9zoz7jF3BP6Q1O1gpCvzcxDZ3BRq3UpZSCqHMhO3Aom83Ua1sVgHO-MyuPzq1Y2Z9P-q9P2ysoGrVtaQIuyH7N5ysomgMV913Bvq1BRZ3BRq3oyuMhsQMhMgJPcQMoNZ_g-q1BpZ3N6qMUpZMhyHj2Nysoj8B2_Z_g-qjVO_1zC81NEz9268B1O_7zC81NJu_o6qjuE_jo-r7BX_M2iH72D3Ao6QVByZM2xe7jfZ32CP1OJe_zvuJVR_7zsH7O1e_VozcDp_Bz0q3gzv_7?src=search\\u0026template_id_used=3\\u0026sid=fa125e42-dcce-4a2c-872e-f4b06ee5f301\\u0026ab_test=N\\u0026number_ads_req=1\\u0026keywords=samsung\\u0026cpm_template_req=3%2C4%2C5\\u0026t=android\\u0026uid=0\\u0026is_search=1\\u0026page=1\\u0026r=https%3A%2F%2Fwww.tokopedia.com%2Fanker-indonesia%2Fanker-powercore-13400-mah-premium-black-a1315h11\\u0026alg=stm\\u0026n_candidate_ads=1\\u0026number_of_ads=1\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"id\": \"181707633\",\n" +
                "\t\t\t\t\t\"name\": \"Anker PowerCore+ 26800  And PowerPort+ 1 -Black [B1374L11]\",\n" +
                "\t\t\t\t\t\"price_format\": \"Rp 1.668.600\",\n" +
                "\t\t\t\t\t\"applinks\": \"tokopedia://product/181707633\",\n" +
                "\t\t\t\t\t\"image_product\": {\n" +
                "\t\t\t\t\t\t\"product_id\": \"181707633\",\n" +
                "\t\t\t\t\t\t\"product_name\": \"Anker PowerCore+ 26800  And PowerPort+ 1 -Black [B1374L11]\",\n" +
                "\t\t\t\t\t\t\"image_url\": \"https://ecs7.tokopedia.net/img/cache/200-square/product-1/2017/10/5/181707633/181707633_78465782-69f7-4f0a-90a0-26166d22fcca_700_700.jpg\",\n" +
                "\t\t\t\t\t\t\"image_click_url\": \"https://ta.tokopedia.com/promo/v1/clicks/HAtaHAndHpUFH_tp6sJEH_1poAeFHp1hHAtF6sHaHsU7H_jEHmdFHAKNHAJEg9BGqMzUZMggQj2fgAo6QJBkQfBoepzR_1zgH1YJ__uoqMra_92C81O1__-6zcW2_JoGqMzUZMgsHBgtyfO6Q7BkQfBoe7BpZ3O7QcuygIgsQu-Myp-6PMoWu3Bvq1BR_c2zHJOJ__z6qBja_1zCH7Y1z9x681BpZ37OPM-W_Mh-qMY2_M2o8jO9u_-vuOu7_uzs8BBNHACoHjgW_Vztq1O1_9zoz7jF3BP6Q1O1gpCvzcxDZ3BRq3UpZSCqHMhO3Aom83Ua1sVgHO-MyuPzq1Y2Z9P-q9P2ysoGrVtaQIuyH7N5ysomgMV913Bvq1BRZ3BRq3oyuMhsQMhMgJPcQMoNZ_g-q1BpZ3N6qMUpZMhyHj2Nysoj8B2_Z_g-qjVO_1zC81NEz9268B1O_7zC81NJu_o6qjuE_jo-r7BX_M2iH72D3Ao6QVByZM2xe7jfZ32CP1OJe_zvuJVR_7zsH7O1e_VozcDp_Bz0q3gzv_7?t=android\\u0026is_search=1\\u0026alg=stm\\u0026page=1\\u0026number_ads_req=1\\u0026sid=fa125e42-dcce-4a2c-872e-f4b06ee5f301\\u0026r=https%3A%2F%2Fwww.tokopedia.com%2Fanker-indonesia%2Fanker-powercore-26800-and-powerport-1-black-b1374l11\\u0026ab_test=N\\u0026keywords=samsung\\u0026n_candidate_ads=1\\u0026cpm_template_req=3%2C4%2C5\\u0026uid=0\\u0026src=search\\u0026number_of_ads=1\\u0026template_id_used=3\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"id\": \"318826238\",\n" +
                "\t\t\t\t\t\"name\": \"Quick Charge 3.0 Anker Powerbank PowerCore II 20000mAh - A1260H21\",\n" +
                "\t\t\t\t\t\"price_format\": \"Rp 1.421.400\",\n" +
                "\t\t\t\t\t\"applinks\": \"tokopedia://product/318826238\",\n" +
                "\t\t\t\t\t\"image_product\": {\n" +
                "\t\t\t\t\t\t\"product_id\": \"318826238\",\n" +
                "\t\t\t\t\t\t\"product_name\": \"Quick Charge 3.0 Anker Powerbank PowerCore II 20000mAh - A1260H21\",\n" +
                "\t\t\t\t\t\t\"image_url\": \"https://ecs7.tokopedia.net/img/cache/200-square/product-1/2018/8/15/37586796/37586796_d67098ca-3acf-4f39-b12f-06bc0c441af2_448_448.png\",\n" +
                "\t\t\t\t\t\t\"image_click_url\": \"https://ta.tokopedia.com/promo/v1/clicks/HAtaHAndHpUFH_tp6sJEH_1poAeFHp1hHAtF6sHaHsU7H_jEHmdFHAKNHAJEg9BGqMzUZMggQj2fgAo6QJBkQfBoepzR_1zgH1YJ__uoqMra_92C81O1__-6zcW2_JoGqMzUZMgsHBgtyfO6Q7BkQfBoe7BpZ3O7QcuygIgsQu-Myp-6PMoWu3Bvq1BR_c2zHJOJ__z6qBja_1zCH7Y1z9x681BpZ37OPM-W_Mh-qMY2_M2o8jO9u_-vuOu7_uzs8BBNHACoHjgW_Vztq1O1_9zoz7jF3BP6Q1O1gpCvzcxDZ3BRq3UpZSCqHMhO3Aom83Ua1sVgHO-MyuPzq1Y2Z9P-q9P2ysoGrVtaQIuyH7N5ysomgMV913Bvq1BRZ3BRq3oyuMhsQMhMgJPcQMoNZ_g-q1BpZ3N6qMUpZMhyHj2Nysoj8B2_Z_g-qjVO_1zC81NEz9268B1O_7zC81NJu_o6qjuE_jo-r7BX_M2iH72D3Ao6QVByZM2xe7jfZ32CP1OJe_zvuJVR_7zsH7O1e_VozcDp_Bz0q3gzv_7?ab_test=N\\u0026number_of_ads=1\\u0026number_ads_req=1\\u0026uid=0\\u0026page=1\\u0026cpm_template_req=3%2C4%2C5\\u0026sid=fa125e42-dcce-4a2c-872e-f4b06ee5f301\\u0026r=https%3A%2F%2Fwww.tokopedia.com%2Fanker-indonesia%2Fquick-charge-3-0-anker-powerbank-powercore-ii-20000mah-a1260h21\\u0026alg=stm\\u0026template_id_used=3\\u0026t=android\\u0026src=search\\u0026is_search=1\\u0026keywords=samsung\\u0026n_candidate_ads=1\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t}],\n" +
                "\t\t\t\t\"image_shop\": {\n" +
                "\t\t\t\t\t\"cover\": \"https://ecs7.tokopedia.net/img/cache/750/shops-1/2018/8/2/22126717/22126717_4c5d863e-bbce-4d56-ba3d-0b1b53ec6818.png\",\n" +
                "\t\t\t\t\t\"s_url\": \"https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2018/7/17/1645551/1645551_ed1734d3-ff1a-4476-91a1-341fdc3639d6.jpg\",\n" +
                "\t\t\t\t\t\"xs_url\": \"https://ecs7.tokopedia.net/img/cache/100-square/shops-1/2018/7/17/1645551/1645551_ed1734d3-ff1a-4476-91a1-341fdc3639d6.jpg\",\n" +
                "\t\t\t\t\t\"cover_ecs\": \"https://ecs7.tokopedia.net/img/cache/750/shops-1/2018/8/2/22126717/22126717_4c5d863e-bbce-4d56-ba3d-0b1b53ec6818.png\",\n" +
                "\t\t\t\t\t\"s_ecs\": \"https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2018/7/17/1645551/1645551_ed1734d3-ff1a-4476-91a1-341fdc3639d6.jpg\",\n" +
                "\t\t\t\t\t\"xs_ecs\": \"https://ecs7.tokopedia.net/img/cache/100-square/shops-1/2018/7/17/1645551/1645551_ed1734d3-ff1a-4476-91a1-341fdc3639d6.jpg\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t},\n" +
                "\t\t\t\"badges\": [{\n" +
                "\t\t\t\t\"title\": \"Official Store\",\n" +
                "\t\t\t\t\"image_url\": \"https://ecs7.tokopedia.net/img/ta/display/of/ofstore.png\",\n" +
                "\t\t\t\t\"show\": true\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"button_text\": \"Kunjungi Toko\",\n" +
                "\t\t\t\"promoted_text\": \"Promoted by\",\n" +
                "\t\t\t\"description\": \"Fast Charge, Quick Charge, Premium Product\",\n" +
                "\t\t\t\"uri\": \"https://www.tokopedia.com/anker-indonesia\"\n" +
                "\t\t},\n" +
                "\t\t\"applinks\": \"tokopedia://shop/1645551\"\n" +
                "\t}]\n" +
                "}", CpmModel.class);
    }
}
