package com.tokopedia.tkpd.tkpdfeed.feedplus.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.PromotedShopViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ActivityCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ProductFeedViewModel;

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

        void onGoToProductDetail(Integer productId);

        void onGoToFeedDetail(ActivityCardViewModel activityCardViewModel);

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

        void showLoading();

        void onSuccessGetFeed(ArrayList<Visitable> visitables);
    }

    public interface Presenter extends CustomerPresenter<View>{

        void fetchFirstPage(boolean isRefresh);

        void fetchNextPage();

    }
}
