package com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener;

import android.app.Activity;
import android.content.res.Resources;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.feeddetail.SingleFeedDetailViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.feeddetail.FeedDetailHeaderViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 5/18/17.
 */

public interface FeedPlusDetail {

    interface View extends CustomerView{

        void onWishlistClicked(int adapterPosition, Integer productId, boolean wishlist);

        void onGoToShopDetail(Integer shopId);

        void onErrorGetFeedDetail(String errorMessage);

        void onSuccessGetFeedDetail(FeedDetailHeaderViewModel header,
                                    ArrayList<Visitable> listDetail,
                                    boolean hasNextPage);

        void showLoading();

        void showLoadingProgress();

        Activity getActivity();

        String getString(int resId);

        void onGoToProductDetail(String productId, boolean wishlist, int adapterPosition);

        int getColor(int resId);

        Resources getResources();

        void onEmptyFeedDetail();

        void onBackPressed();

        void onGoToBuyProduct(String productId, String price, String imageSource);

        void onSuccessGetSingleFeedDetail(FeedDetailHeaderViewModel headerViewModel,
                                          SingleFeedDetailViewModel feedDetailViewModel);
    }

    interface Presenter extends CustomerPresenter<View> {
        void addToWishlist(int adapterPosition, String productId);

        void removeFromWishlist(int adapterPosition, String productId);
    }
}
