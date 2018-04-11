package com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore.OfficialStoreViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.ProductFeedViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 5/15/17.
 */

public interface FeedPlus {

    interface View extends CustomerView {

        void showLoadingProgress();

        void finishLoadingProgress();

        interface Kol {
            void onGoToKolProfile(int page, int rowNumber, String url);

            void onOpenKolTooltip(int page, int rowNumber, String url);

            void onFollowKolClicked(int page, int rowNumber, int id);

            void onUnfollowKolClicked(int page, int rowNumber, int id);

            void onLikeKolClicked(int page, int rowNumber, int id);

            void onUnlikeKolClicked(int page, int adapterPosition, int id);

            void onGoToKolComment(int page, int rowNumber, KolViewModel kolViewModel);

            void onGoToListKolRecommendation(int page, int rowNumber, String url);

            void onErrorFollowKol(String errorMessage, int id, int status, int rowNumber);

            void onSuccessFollowUnfollowKol(int rowNumber);

            void onErrorLikeDislikeKolPost(String errorMessage);

            void onSuccessLikeDislikeKolPost(int rowNumber);

            void onFollowKolFromRecommendationClicked(int page, int rowNumber, int id, int position);

            void onUnfollowKolFromRecommendationClicked(int page, int rowNumber, int id, int position);

            void onSuccessFollowKolFromRecommendation(int rowNumber, int position);

            void onSuccessUnfollowKolFromRecommendation(int rowNumber, int position);
        }

        void setFirstCursor(String firstCursor);

        interface Toppicks {
            void onToppicksClicked(int page, int rowNumber, String name, String url, int itemPosition);

            void onSeeAllToppicks(int page, int rowNumber);
        }

        void onShareButtonClicked(String shareUrl,
                                  String title,
                                  String imgUrl,
                                  String contentMessage,
                                  String pageRowNumber);

        void onGoToProductDetail(int rowNumber, int page, String id, String imageSourceSingle, String name, String productId);

        void onGoToProductDetailFromProductUpload(
                int rowNumber,
                int positionFeedCard,
                int page,
                int itemPosition,
                String productId,
                String imageSourceSingle,
                String name,
                String price,
                String priceInt,
                String productUrl,
                String eventLabel
        );

        void onGoToProductDetailFromRecentView(String productID, String imgUri, String name, String price);

        void onGoToProductDetailFromInspiration(int page, int rowNumber, String productId,
                                                String imageSource, String name, String price, String priceInt,
                                                String productUrl, String source, int positionFeedCard, int itemPosition, String eventLabel);

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

        void onShowEmptyWithRecentView(ArrayList<Visitable> recentProduct);

        void onShowEmpty();

        void clearData();

        void unsetEndlessScroll();

        void onShowNewFeed(String totalData);

        void onGoToPromoPageFromHeader(int page, int rowNumber);

        void onHideNewFeed();

        boolean hasFeed();

        void updateFavoriteFromEmpty(String shopId);

        void onEmptyOfficialStoreClicked();

        void onBrandClicked(int page, int rowNumber, OfficialStoreViewModel officialStoreViewModel);

        void onSeeAllOfficialStoresFromCampaign(int page, int rowNumber, String redirectUrl);

        void onGoToCampaign(int page, int rowNumber, String redirectUrl, String title);

        void onSeeAllOfficialStoresFromBrands(int page, int rowNumber);

        void onGoToProductDetailFromCampaign(int page, int rowNumber, String productId,
                                             String imageSourceSingle, String name, String price);

        void onGoToShopDetailFromCampaign(int page, int rowNumber, String shopUrl);

        void onContentProductLinkClicked(String url);

        void onUserNotLogin();

        void onGoToLogin();

        int getAdapterListSize();
    }

    interface Presenter extends CustomerPresenter<View> {

        void fetchFirstPage();

        void fetchNextPage();

        void refreshPage();

        void checkNewFeed(String cursor);

        void followKol(int id, int rowNumber, View.Kol kolListener);

        void unfollowKol(int id, int rowNumber, View.Kol kolListener);

        void likeKol(int id, int rowNumber, View.Kol kolListener);

        void unlikeKol(int id, int rowNumber, View.Kol kolListener);


        void followKolFromRecommendation(int id, int rowNumber, int position, View.Kol
                kolListener);

        void unfollowKolFromRecommendation(int id, int rowNumber, int position, View.Kol
                kolListener);

    }
}
