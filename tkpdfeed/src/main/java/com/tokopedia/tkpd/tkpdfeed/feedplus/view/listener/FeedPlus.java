package com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.recentview.RecentViewProductDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore.OfficialStoreViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.ProductFeedViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 5/15/17.
 */

public interface FeedPlus{

    interface View extends CustomerView {
        void onShareButtonClicked( String shareUrl,
                                   String title,
                                   String imgUrl,
                                   String contentMessage);

        void onGoToProductDetail(String productId);

        void onGoToProductDetailFromRecentView(String productID);

        void onGoToProductDetailFromInspiration(String productId);

        void onGoToFeedDetail(String feedId);

        void onGoToShopDetail(Integer shopId, String url);

        void onCopyClicked(String s, String name);

        void onGoToBlogWebView(String url);

        void onOpenVideo(String videoUrl, String subtitle);

        void onGoToBuyProduct(ProductFeedViewModel productFeedViewModel);

        void onInfoClicked();

        void onSuccessGetFeedFirstPage(ArrayList<Visitable> listFeed);

        void onErrorGetFeedFirstPage(String errorMessage);

        void onSearchShopButtonClicked();

        void onFavoritedClicked(int adapterPosition);

        void showSnackbar(String s);

        Context getActivity();

        void updateFavorite(int adapterPosition);

        void onViewMorePromoClicked();

        void showRefresh();

        void finishLoading();

        void updateCursor(String currentCursor);

        void onSuccessGetFeed(ArrayList<Visitable> visitables);

        void onSuccessGetFeedFirstPageWithAddFeed(ArrayList<Visitable> listFeedView);

        void onSeePromo(String link, String name);

        void onRetryClicked();

        void onShowRetryGetFeed();

        void onShowAddFeedMore();

        void shouldLoadTopAds(boolean loadTopAds);

        void hideTopAdsAdapterLoading();

        String getString(int msg_network_error);

        int getColor(int black);

        Resources getResources();

        void onSeeAllRecentView();

        void onShowEmptyWithRecentView(ArrayList<Visitable> recentProduct, boolean canShowTopads);

        void onShowEmpty(boolean canShowTopads);

        void clearData();

        void unsetEndlessScroll();

        void onShowNewFeed();

        void onGoToPromoPageFromHeader();

        void onHideNewFeed();

        boolean hasFeed();

        void updateFavoriteFromEmpty();

        void showTopAds(boolean isTopAdsShown);

        void onEmptyOfficialStoreClicked();

        void onBrandClicked(OfficialStoreViewModel officialStoreViewModel);

        void onSeeAllOfficialStoresFromCampaign(String redirectUrl);

        void onGoToCampaign(String redirectUrl, String title);

        void onSeeAllOfficialStoresFromBrands();

        void onGoToProductDetailFromCampaign(String productId);

        void onGoToShopDetailFromCampaign(String shopUrl);
    }

    interface Presenter extends CustomerPresenter<View>{

        void fetchFirstPage();

        void fetchNextPage();

        void refreshPage();

        void checkNewFeed(String cursor);

    }
}
