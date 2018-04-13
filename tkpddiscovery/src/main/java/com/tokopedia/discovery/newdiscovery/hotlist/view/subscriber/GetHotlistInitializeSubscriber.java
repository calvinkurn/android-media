package com.tokopedia.discovery.newdiscovery.hotlist.view.subscriber;

import com.google.android.gms.tagmanager.DataLayer;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;
import com.tokopedia.discovery.newdiscovery.domain.model.BadgeModel;
import com.tokopedia.discovery.newdiscovery.domain.model.LabelModel;
import com.tokopedia.discovery.newdiscovery.domain.model.ProductModel;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistBannerModel;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistHashtagModel;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistModel;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistPromoInfo;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistPromo;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.SearchEmptyViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistHashTagViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistHeaderViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistProductViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.presenter.HotlistFragmentContract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by hangnadi on 10/8/17.
 */

public class GetHotlistInitializeSubscriber extends rx.Subscriber<HotlistModel> {

    private final HotlistFragmentContract.View view;
    private final int page;

    public GetHotlistInitializeSubscriber(HotlistFragmentContract.View view, int page) {
        this.view = view;
        this.page = page;
    }

    @Override
    public void onStart() {
        view.setTopAdsEndlessListener();
        view.showRefresh();
    }

    @Override
    public void onCompleted() {
        view.hideRefresh();
        view.getDynamicFilter();
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
        view.hideRefresh();
    }

    @Override
    public void onNext(HotlistModel hotlistModel) {
        view.resetData();
        view.storeTotalData(hotlistModel.getTotalData());
        view.setQueryModel(hotlistModel.getBanner().getHotlistQueryModel());
        view.setDisableTopads(hotlistModel.getBanner().isDisableTopads());
        view.setShareUrl(hotlistModel.getShareURL());

        HotlistHeaderViewModel header = mappingHotlistHeader(hotlistModel.getBanner(), hotlistModel.getAttribute().getHastTags());

        view.clearLastProductTracker(page == 1);

        if (hotlistModel.getProductList() != null && !hotlistModel.getProductList().isEmpty()) {
            view.initTopAdsParams();
            List<HotlistProductViewModel> list = mappingHotlistProduct(hotlistModel.getProductList());
            view.trackImpressionProduct(createDataLayer(list));
            view.renderListView(
                    mappingHotlist(header, list)
            );
        } else {
            view.renderEmptyHotlist(
                    mappingEmptyHotlist(header, getEmptyHotlist())
            );
        }
        if (view.getStartFrom() > hotlistModel.getTotalData()){
            view.unSetTopAdsEndlessListener();
        }
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

    private List<Visitable> mappingEmptyHotlist(HotlistHeaderViewModel header, SearchEmptyViewModel emptyHotlist) {
        List<Visitable> list = new ArrayList<>();
        list.add(header);
        list.add(emptyHotlist);
        return list;
    }

    private SearchEmptyViewModel getEmptyHotlist() {
        return new SearchEmptyViewModel();
    }

    private List<Visitable> mappingHotlist(HotlistHeaderViewModel header, List<HotlistProductViewModel> product) {
        List<Visitable> list = new ArrayList<>();
        list.add(header);
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

    private HotlistHeaderViewModel mappingHotlistHeader(HotlistBannerModel banner, List<HotlistHashtagModel> hashTags) {
        HotlistHeaderViewModel headerViewModel = new HotlistHeaderViewModel();
        headerViewModel.setImageUrl(banner.getBannerImage());
        headerViewModel.setHotlistTitle(banner.getHotlistTitle());
        headerViewModel.setDesc(banner.getBannerDesc());
        headerViewModel.setHashTags(mappingHashtags(hashTags));
        if (banner.getHotlistPromoInfo() != null) {
            HotlistPromoInfo info = banner.getHotlistPromoInfo();
            headerViewModel.setHotlistPromo(mappingHotlistPromo(info));
            TrackingUtils.impressionHotlistPromo(banner.getHotlistTitle(), info.getTitle(), info.getVoucherCode());
        }
        return headerViewModel;
    }

    private HotlistPromo mappingHotlistPromo(HotlistPromoInfo info) {
        HotlistPromo hotlistPromo = new HotlistPromo();
        hotlistPromo.setTitle(info.getTitle());
        hotlistPromo.setVoucherCode(info.getVoucherCode());
        hotlistPromo.setPromoPeriod(info.getValidDate());
        hotlistPromo.setMinimunTransaction(info.getMinimunTransaction());
        hotlistPromo.setApplinkTermCondition(info.getApplinkTermCondition());
        hotlistPromo.setUrlTermCondition(info.getUrlTermCondition());
        return hotlistPromo;
    }

    private List<HotlistHashTagViewModel> mappingHashtags(List<HotlistHashtagModel> hashTags) {
        List<HotlistHashTagViewModel> list = new ArrayList<>();
        for (HotlistHashtagModel domain : hashTags) {
            HotlistHashTagViewModel viewModel = new HotlistHashTagViewModel();
            viewModel.setDepartmentID(domain.getDepartmentID());
            viewModel.setName(domain.getName());
            viewModel.setURL(domain.getURL());
            list.add(viewModel);
        }
        return list;
    }
}
