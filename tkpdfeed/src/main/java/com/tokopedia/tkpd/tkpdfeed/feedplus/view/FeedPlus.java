package com.tokopedia.tkpd.tkpdfeed.feedplus.view;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.ProductFeedViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 5/15/17.
 */

public interface FeedPlus{

    public interface View extends CustomerView {
        void onShareButtonClicked( String shareUrl,
                                   String title,
                                   String imgUrl,
                                   String contentMessage);

        void onGoToProductDetail(String productId);

        void onGoToFeedDetail(String feedId);

        void onGoToShopDetail(Integer shopId, String url);

        void onCopyClicked(String s);

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

        void updateCursor(String currentCursor);

        void onSuccessGetFeed(ArrayList<Visitable> visitables);

        void onSeePromo(String link);

        void onErrorGetFeed();

        void onRetryClicked();

        void onShowRetryGetFeed();

        void onShowAddFeedMore();

        void shouldLoadTopAds(boolean loadTopAds);

        void hideTopAdsAdapterLoading();

        String getString(int msg_network_error);

        int getColor(int black);

        Resources getResources();

        void onSeeAllRecentView();
    }

    public interface Presenter extends CustomerPresenter<View>{

        void fetchFirstPage();

        void fetchNextPage();

        void refreshPage();

    }
}
