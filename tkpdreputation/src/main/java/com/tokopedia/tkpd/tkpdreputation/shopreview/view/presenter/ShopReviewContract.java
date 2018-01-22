package com.tokopedia.tkpd.tkpdreputation.shopreview.view.presenter;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.shopreview.view.adapter.ShopReviewModelContent;

/**
 * Created by zulfikarrahman on 1/19/18.
 */

public interface ShopReviewContract {
    interface Presenter extends CustomerPresenter<View> {

    }

    interface View extends BaseListViewListener<ShopReviewModelContent> {

        void onErrorDeleteReview(Throwable e);

        void onSuccessDeleteReview(DeleteReviewResponseDomain deleteReviewResponseDomain, String reviewId);

        void onErrorPostLikeDislike(Throwable e);

        void onSuccessPostLikeDislike(LikeDislikeDomain likeDislikeDomain, String reviewId);

        void hideProgressLoading();

        void showProgressLoading();
    }
}
