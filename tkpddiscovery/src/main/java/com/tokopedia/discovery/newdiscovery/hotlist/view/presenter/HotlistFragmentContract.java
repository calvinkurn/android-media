package com.tokopedia.discovery.newdiscovery.hotlist.view.presenter;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistQueryModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistHeaderViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.BrowseSectionFragmentPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.BrowseSectionFragmentView;
import com.tokopedia.discovery.newdiscovery.util.HotlistParameter;

import java.util.List;
import java.util.Map;

/**
 * Created by hangnadi on 10/6/17.
 */

public interface HotlistFragmentContract {
    interface View extends BrowseSectionFragmentView {

        void renderErrorView(String message);

        void renderRetryInit();

        void renderRetryRefresh();

        void setTopAdsEndlessListener();

        void unSetTopAdsEndlessListener();

        void renderListView(List<Visitable> visitables);

        void renderNextListView(List<Visitable> visitables);

        void renderEmptyHotlist(List<Visitable> visitables);

        void resetData();

        void initTopAdsParams();

        String getHotlistAlias();

        String getShareUrl();

        void setShareUrl(String shareUrl);

        void loadImageHeader(String bannerImageUrl);

        void setTitleHeader(String title);

        void setDescription(String description);

        void storeTotalData(int totalData);

        void setQueryModel(HotlistQueryModel queryModel);

        HotlistQueryModel getQueryModel();

        int getStartFrom();

        void showRefresh();

        void hideRefresh();

        void onEditWishlistError(String errorMessage, String productID);

        void onEditWishlistSuccess(String successMessage, String productID);

        void setDisableTopads(boolean disableTopads);

        boolean isDisableTopads();

        String getHomeAttribution();

        void setLastPositionProductTracker(int lastPositionProductTracker);

        int getLastPositionProductTracker();

        void clearLastProductTracker(boolean clear);

        void trackImpressionProduct(Map<String, Object> dataLayer);

        String getString(int resId);
    }

    interface Presenter extends BrowseSectionFragmentPresenter<View> {

        void requestDataForTheFirstTime(HotlistParameter parameter);

        void requestLoadMore();

        void refreshSort(HotlistHeaderViewModel headerViewModel);

        void addWishlist(String productID);

        void removeWishlist(String productID);
    }
}
