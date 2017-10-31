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

        interface Kol {
            void onGoToKolProfile(int page, int rowNumber, String url);

            void onGoToProductPageFromKol(int page, int rowNumber, String productId, String productImage);

            void onFollowKolClicked(int page, int rowNumber, String id);

            void onUnfollowKolClicked(int page, int rowNumber, String id);

            void onUnlikeKol(int page, int rowNumber, String id);

            void onLikeKol(int page, int rowNumber, String id);

            void onGoToKolComment(int page, int rowNumber, String id);

            void onGoToListKolRecommendation(int page, int rowNumber, String url);
        }

        void setFirstCursor(String firstCursor);

        interface Toppicks {
            void onToppicksClicked(int page, int rowNumber, String name, String url);

            void onSeeAllToppicks(int page, int rowNumber);
        }

        void onShareButtonClicked(String shareUrl,
                                  String title,
                                  String imgUrl,
                                  String contentMessage,
                                  String pageRowNumber);

        void onGoToProductDetail(int rowNumber, int page, String id, String imageSourceSingle, String name, String productId);

        void onGoToProductDetailFromRecentView(String productID, String imgUri, String name, String price);

        void onGoToProductDetailFromInspiration(int page, int rowNumber, String productId, String imageSource, String name, String price);

        void onGoToFeedDetail(int page, int rowNumber, String feedId);

        void onGoToShopDetail(int page, int rowNumber, Integer shopId, String url);

        void onCopyClicked(int page, int rowNumber, String id, String s, String name);

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

        void onViewMorePromoClicked(int page, int rowNumber);

        void showRefresh();

        void finishLoading();

        void updateCursor(String currentCursor);

        void onSuccessGetFeed(ArrayList<Visitable> visitables);

        void onSuccessGetFeedFirstPageWithAddFeed(ArrayList<Visitable> listFeedView);

        void onSeePromo(int page, int rowNumber, String id, String link, String name);

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

        void onShowNewFeed(String totalData);

        void onGoToPromoPageFromHeader(int page, int rowNumber);

        void onHideNewFeed();

        boolean hasFeed();

        void updateFavoriteFromEmpty(String shopId);

        void showTopAds(boolean isTopAdsShown);

        void onEmptyOfficialStoreClicked();

        void onBrandClicked(int page, int rowNumber, OfficialStoreViewModel officialStoreViewModel);

        void onSeeAllOfficialStoresFromCampaign(int page, int rowNumber, String redirectUrl);

        void onGoToCampaign(int page, int rowNumber, String redirectUrl, String title);

        void onSeeAllOfficialStoresFromBrands(int page, int rowNumber);

        void onGoToProductDetailFromCampaign(int page, int rowNumber, String productId,
                                             String imageSourceSingle, String name, String price);

        void onGoToShopDetailFromCampaign(int page, int rowNumber, String shopUrl);
    }

    interface Presenter extends CustomerPresenter<View>{

        void fetchFirstPage();

        void fetchNextPage();

        void refreshPage();

        void checkNewFeed(String cursor);

        void followKol(String id);

        void unfollowKol(String id);

        void likeKol(String id);

        void unlikeKol(String id);
    }
}
