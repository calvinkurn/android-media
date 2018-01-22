package com.tokopedia.tkpd.tkpdreputation.productreview.view.presenter;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewstarcount.DataResponseReviewStarCount;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.adapter.ProductReviewModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public interface ProductReviewContract {
    interface Presenter extends CustomerPresenter<View> {

    }

    interface View extends BaseListViewListener<ProductReviewModel>{

        void onGetListReviewProduct(List<ProductReviewModel> map, boolean isHasNextPage);

        void onErrorGetListReviewProduct(Throwable e);

        void onGetListReviewHelpful(List<ProductReviewModel> map);

        void onErrorGetListReviewHelpful(Throwable e);

        void onGetRatingReview(DataResponseReviewStarCount dataResponseReviewStarCount);

        void onErrorGetRatingView(Throwable e);

        void onSuccessPostLikeDislike(LikeDislikeDomain likeDislikeDomain, String reviewId);

        void onErrorPostLikeDislike(Throwable e);

        void onSuccessDeleteReview(DeleteReviewResponseDomain deleteReviewResponseDomain, String reviewId);

        void onErrorDeleteReview(Throwable e);

        void showProgressLoading();

        void hideProgressLoading();
    }
}
