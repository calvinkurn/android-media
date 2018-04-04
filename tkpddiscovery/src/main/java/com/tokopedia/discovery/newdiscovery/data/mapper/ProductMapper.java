package com.tokopedia.discovery.newdiscovery.data.mapper;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.entity.discovery.SearchProductResponse;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;
import com.tokopedia.discovery.newdiscovery.domain.model.BadgeModel;
import com.tokopedia.discovery.newdiscovery.domain.model.LabelModel;
import com.tokopedia.discovery.newdiscovery.domain.model.ProductModel;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.helper.NetworkParamHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 10/5/17.
 */

public class ProductMapper implements Func1<Response<String>, SearchResultModel> {

    private final Gson gson;

    public ProductMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public SearchResultModel call(Response<String> response) {
        if (response.isSuccessful()) {
            SearchProductResponse searchProductResponse = gson.fromJson(response.body(), SearchProductResponse.class);
            if (searchProductResponse != null) {
                return mappingPojoIntoDomain(searchProductResponse);
            } else {
                throw new MessageErrorException(response.errorBody().toString());
            }
        } else {
            throw new RuntimeHttpErrorException(response.code());
        }
    }

    private SearchResultModel mappingPojoIntoDomain(SearchProductResponse searchProductResponse) {
        SearchResultModel model = new SearchResultModel();
        model.setTotalData(searchProductResponse.getHeader().getTotalData());
        model.setTotalDataText(searchProductResponse.getHeader().getTotalDataText());
        model.setAdditionalParams(searchProductResponse.getHeader().getAdditionalParams());
        model.setRedirectUrl(searchProductResponse.getData().getRedirection().getRedirectUrl());

        if (!TextUtils.isEmpty(searchProductResponse.getData().getRedirection().getDepartmentId())) {
            model.setDepartmentId(searchProductResponse.getData().getRedirection().getDepartmentId());
        }

        model.setHasCatalog(searchProductResponse.getData().getCatalogs().size() > 0);
        model.setProductList(mappingProduct(searchProductResponse.getData().getProducts()));
        model.setQuery(searchProductResponse.getData().getQuery());
        model.setSource(searchProductResponse.getData().getSource());
        model.setShareUrl(searchProductResponse.getData().getShareUrl());

        if (searchProductResponse.getData().getSuggestionText() != null) {
            model.setSuggestionText(searchProductResponse.getData().getSuggestionText().getText());
            String suggestedQuery
                    = NetworkParamHelper.getQueryValue(searchProductResponse.getData().getSuggestionText().getQuery());
            model.setSuggestedQuery(suggestedQuery);
        }
        if (searchProductResponse.getData().getSuggestionsInstead() != null) {
            model.setSuggestionCurrentKeyword(searchProductResponse.getData().getSuggestionsInstead().getCurrentKeyword());
        }

        return model;
    }

    private List<ProductModel> mappingProduct(List<SearchProductResponse.Data.Products> products) {
        List<ProductModel> list = new ArrayList<>();
        for (SearchProductResponse.Data.Products data : products) {
            ProductModel model = new ProductModel();
            model.setProductID(data.getId());
            model.setProductName(data.getName());
            model.setImageUrl(data.getImageUrl());
            model.setImageUrl700(data.getImageUrl700());
            model.setRating(data.getRating());
            model.setCountReview(data.getCountReview());
            model.setPrice(data.getPrice());
            model.setShopID(data.getShop().getId());
            model.setShopName(data.getShop().getName());
            model.setShopCity(data.getShop().getCity());
            model.setGoldMerchant(data.getShop().isIsGold());
            model.setLabelList(mappingLabels(data.getLabels()));
            model.setBadgesList(mappingBadges(data.getBadges()));
            model.setFeatured(data.getIsFeatured() == 1);
            list.add(model);
        }
        return list;
    }

    private List<LabelModel> mappingLabels(List<SearchProductResponse.Data.Products.Labels> labels) {
        List<LabelModel> list = new ArrayList<>();
        for (SearchProductResponse.Data.Products.Labels data : labels) {
            LabelModel model = new LabelModel();
            model.setTitle(data.getTitle());
            model.setColor(data.getColor());
            list.add(model);
        }
        return list;
    }

    private List<BadgeModel> mappingBadges(List<SearchProductResponse.Data.Products.Badges> badges) {
        List<BadgeModel> list = new ArrayList<>();
        for (SearchProductResponse.Data.Products.Badges data : badges) {
            BadgeModel model = new BadgeModel();
            model.setTitle(data.getTitle());
            model.setImageUrl(data.getImageUrl());
            list.add(model);
        }
        return list;
    }
}
