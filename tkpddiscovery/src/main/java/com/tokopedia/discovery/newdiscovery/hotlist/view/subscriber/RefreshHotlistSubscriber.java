package com.tokopedia.discovery.newdiscovery.hotlist.view.subscriber;

import com.tokopedia.analytic_constant.DataLayer;
import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;
import com.tokopedia.discovery.newdiscovery.domain.model.BadgeModel;
import com.tokopedia.discovery.newdiscovery.domain.model.LabelModel;
import com.tokopedia.discovery.newdiscovery.domain.model.ProductModel;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistHeaderViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistProductViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.SearchEmptyViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.presenter.HotlistFragmentContract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by hangnadi on 10/23/17.
 */

public class RefreshHotlistSubscriber extends Subscriber<SearchResultModel> {
    private final HotlistFragmentContract.View view;
    private final HotlistHeaderViewModel headerViewModel;
    private final int page;

    public RefreshHotlistSubscriber(HotlistFragmentContract.View view,
                                    HotlistHeaderViewModel headerViewModel,
                                    int page) {
        this.view = view;
        this.headerViewModel = headerViewModel;
        this.page = page;
    }

    @Override
    public void onStart() {
        view.setTopAdsEndlessListener();
        view.initTopAdsParams();
        view.showRefresh();
    }

    @Override
    public void onCompleted() {
        view.getDynamicFilter();
        view.hideRefresh();
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof MessageErrorException) {
            view.renderErrorView(e.getMessage());
        } else if (e instanceof RuntimeHttpErrorException) {
            view.renderErrorView(e.getMessage());
        } else if (e instanceof IOException) {
            view.renderRetryRefresh();
        } else {
            view.renderErrorView(null);
            e.printStackTrace();
        }
        view.hideRefresh();
    }

    @Override
    public void onNext(SearchResultModel searchResultModel) {
        view.resetData();
        view.clearLastProductTracker(page == 1);
        if (searchResultModel.getProductList() != null && !searchResultModel.getProductList().isEmpty()) {
            List<HotlistProductViewModel> list = mappingHotlistProduct(searchResultModel.getProductList());
            view.trackImpressionProduct(createDataLayer(list));
            view.renderListView(
                    mappingHotlist(list)
            );
        } else {
            view.renderEmptyHotlist(
                    mappingEmptyHotlist(getEmptyHotlist())
            );
        }
        if(view.getStartFrom() > searchResultModel.getTotalData()){
            view.unSetTopAdsEndlessListener();
        }
        view.storeTotalData(searchResultModel.getTotalData());
    }

    private SearchEmptyViewModel getEmptyHotlist() {
        return new SearchEmptyViewModel();
    }

    private List<Visitable> mappingEmptyHotlist(SearchEmptyViewModel emptyHotlist) {
        List<Visitable> list = new ArrayList<>();
        list.add(headerViewModel);
        list.add(emptyHotlist);
        return list;
    }

    private List<Visitable> mappingHotlist(List<HotlistProductViewModel> product) {
        List<Visitable> list = new ArrayList<>();
        list.add(headerViewModel);
        list.addAll(product);
        return list;
    }

    private List<HotlistProductViewModel> mappingHotlistProduct(List<ProductModel> productList) {
        List<HotlistProductViewModel> list = new ArrayList<>();
        int lastPositionProduct = view.getLastPositionProductTracker();
        for (ProductModel domain : productList) {
            lastPositionProduct++;
            HotlistProductViewModel model = new HotlistProductViewModel();
            model.setBadgesList(mappingBadges(domain.getBadgesList()));
            model.setLabelList(mappingLabels(domain.getLabelList()));
            model.setCountReview(Integer.toString(domain.getCountReview()));
            model.setGoldMerchant(domain.isGoldMerchant());
            model.setImageUrl(domain.getImageUrl());
            model.setImageUrl700(domain.getImageUrl700());
            model.setPrice(domain.getPrice());
            model.setProductID(domain.getProductID());
            model.setProductName(domain.getProductName());
            model.setRating(Integer.toString(domain.getRating()));
            model.setShopCity(domain.getShopCity());
            model.setShopID(domain.getShopID());
            model.setShopName(domain.getShopName());
            model.setWishlist(domain.isWishlisted());
            model.setWishlistButtonEnabled(true);
            model.setFeatured(domain.isFeatured());
            model.setTrackerName(String.format(Locale.getDefault(), "/hot/%s - product %d", view.getHotlistAlias().toLowerCase(), page));
            model.setTrackerPosition(String.valueOf(lastPositionProduct));
            model.setHomeAttribution(view.getHomeAttribution());
            model.setCountCourier(Integer.toString(domain.getCountCourier()));
            model.setOriginalPrice(domain.getOriginalPrice());
            model.setDiscountPercentage(domain.getDiscountPercentage());
            model.setOfficial(domain.isOfficial());
            model.setPriceRange(domain.getPriceRange());
            model.setTopLabel(domain.getTopLabel());
            model.setBottomLabel(domain.getBottomLabel());

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
            viewModel.setShown(domain.isShown());
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
