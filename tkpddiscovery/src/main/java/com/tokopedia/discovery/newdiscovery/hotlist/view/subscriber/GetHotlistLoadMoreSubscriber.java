package com.tokopedia.discovery.newdiscovery.hotlist.view.subscriber;

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

/**
 * Created by hangnadi on 10/10/17.
 */

public class GetHotlistLoadMoreSubscriber extends rx.Subscriber<SearchResultModel> {

    private final HotlistFragmentContract.View view;

    public GetHotlistLoadMoreSubscriber(HotlistFragmentContract.View view) {
        this.view = view;
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
        view.renderNextListView(mappingHotlistProduct(searchResultModel));
        if(view.getStartFrom() > searchResultModel.getTotalData()){
            view.unSetTopAdsEndlessListener();
        }
    }

    private List<Visitable> mappingHotlistProduct(SearchResultModel searchResultModel) {
        List<Visitable> list = new ArrayList<>();
        for (ProductModel domain : searchResultModel.getProductList()) {
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
            list.add(model);
        }
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
}
