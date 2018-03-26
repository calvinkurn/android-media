package com.tokopedia.discovery.newdiscovery.hotlist.view.subscriber;

import com.google.android.gms.tagmanager.DataLayer;
import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;
import com.tokopedia.discovery.newdiscovery.domain.model.BadgeModel;
import com.tokopedia.discovery.newdiscovery.domain.model.LabelModel;
import com.tokopedia.discovery.newdiscovery.domain.model.ProductModel;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistProductViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.presenter.HotlistFragmentContract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by hangnadi on 10/10/17.
 */

public class GetHotlistLoadMoreSubscriber extends rx.Subscriber<SearchResultModel> {

    private final HotlistFragmentContract.View view;
    private final int page;

    public GetHotlistLoadMoreSubscriber(HotlistFragmentContract.View view,
                                        int page) {
        this.view = view;
        this.page = page;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof MessageErrorException) {
            view.renderErrorView(e.getMessage());
        } else if (e instanceof RuntimeHttpErrorException) {
            view.renderErrorView(e.getMessage());
        } else if (e instanceof IOException) {
            view.renderRetryInit();
        } else {
            view.renderErrorView(null);
            e.printStackTrace();
        }
    }

    @Override
    public void onNext(SearchResultModel searchResultModel) {
        view.clearLastProductTracker(page == 1);
        List<HotlistProductViewModel> list = mappingHotlistProduct(searchResultModel);
        view.trackImpressionProduct(createDataLayer(list));
        List<Visitable> visitables = new ArrayList<>();
        visitables.addAll(list);
        view.renderNextListView(visitables);
        if(view.getStartFrom() > searchResultModel.getTotalData()){
            view.unSetTopAdsEndlessListener();
        }
    }

    private List<HotlistProductViewModel> mappingHotlistProduct(SearchResultModel searchResultModel) {
        List<HotlistProductViewModel> list = new ArrayList<>();
        int lastPositionProduct = view.getLastPositionProductTracker();
        for (ProductModel domain : searchResultModel.getProductList()) {
            lastPositionProduct++;
            HotlistProductViewModel model = new HotlistProductViewModel();
            model.setBadgesList(mappingBadges(domain.getBadgesList()));
            model.setLabelList(mappingLabels(domain.getLabelList()));
            model.setCountReview(domain.getCountReview());
            model.setGoldMerchant(domain.isGoldMerchant());
            model.setImageUrl(domain.getImageUrl());
            model.setImageUrl700(domain.getImageUrl700());
            model.setPrice(domain.getPrice());
            model.setProductID(domain.getProductID());
            model.setProductName(domain.getProductName());
            model.setRating(domain.getRating());
            model.setShopCity(domain.getShopCity());
            model.setShopID(domain.getShopID());
            model.setShopName(domain.getShopName());
            model.setWishlist(domain.isWishlisted());
            model.setWishlistButtonEnabled(true);
            model.setFeatured(domain.isFeatured());
            model.setTrackerName(String.format(Locale.getDefault(), "/hot/%s - product %d", view.getHotlistAlias(), page));
            model.setTrackerPosition(String.valueOf(lastPositionProduct));
            model.setHomeAttribution(view.getHomeAttribution());
            list.add(model);
        }
        view.setLastPositionProductTracker(lastPositionProduct);
        return list;
    }

    private List<HotlistProductViewModel.BadgeModel> mappingBadges(List<BadgeModel> badgesList) {
        List<HotlistProductViewModel.BadgeModel> list = new ArrayList<>();
        for (BadgeModel domain : badgesList) {
            HotlistProductViewModel.BadgeModel viewModel = new HotlistProductViewModel.BadgeModel();
            viewModel.setImageUrl(domain.getImageUrl());
            viewModel.setTitle(domain.getTitle());
            list.add(viewModel);
        }
        return list;
    }

    private List<HotlistProductViewModel.LabelModel> mappingLabels(List<LabelModel> labelList) {
        List<HotlistProductViewModel.LabelModel> list = new ArrayList<>();
        for (LabelModel domain : labelList) {
            HotlistProductViewModel.LabelModel viewModel = new HotlistProductViewModel.LabelModel();
            viewModel.setTitle(domain.getTitle());
            viewModel.setColor(domain.getColor());
            list.add(viewModel);
        }
        return list;
    }

    private Map<String, Object> createDataLayer(List<HotlistProductViewModel> list) {
        List<Map<String, Object>> productListDataLayer = new ArrayList<>();
        for (HotlistProductViewModel model : list) {
            productListDataLayer.add(model.generateImpressionDataLayer());
        }
        return DataLayer.mapOf(
                "event", "productView",
                "eventCategory", "hotlist page",
                "eventAction", "product list impression",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(
                                productListDataLayer.toArray(new Object[productListDataLayer.size()])

                        ))
        );
    }

}
