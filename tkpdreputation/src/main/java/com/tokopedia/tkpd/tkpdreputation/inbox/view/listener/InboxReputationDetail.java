package com.tokopedia.tkpd.tkpdreputation.inbox.view.listener;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationItemViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailHeaderViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailItemViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.RevieweeBadgeCustomerViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.RevieweeBadgeSellerViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 8/19/17.
 */

public interface InboxReputationDetail {

    interface View extends CustomerView {

        void showLoading();

        void onErrorGetInboxDetail(String errorMessage);

        void onSuccessGetInboxDetail(InboxReputationItemViewModel inboxReputationItemViewModel,
                                     List<Visitable> visitables);

        void finishLoading();

        void onEditReview(InboxReputationDetailItemViewModel element);

        void onGoToGiveReview(String reviewId, String productId, int shopId, boolean reviewIsSkippable);

        void onErrorSendSmiley(String errorMessage);

        void showLoadingDialog();

        void finishLoadingDialog();

        void showRefresh();

        void onErrorRefreshInboxDetail(String errorMessage);

        void onSuccessRefreshGetInboxDetail(InboxReputationItemViewModel inboxReputationViewModel,
                                            List<Visitable> visitables);

        void finishRefresh();

        void goToPreviewImage(int position, ArrayList<ImageUpload> list);

        int getTab();

        void onGoToReportReview(int shopId, String reviewId);

        void onSuccessSendSmiley(int score);

        void onErrorCheckShopIsFavorited();

        void onSuccessCheckShopIsFavorited(int shopFavorited);

        void onErrorFavoriteShop(String errorMessage);

        void onSuccessFavoriteShop();

        void onDeleteReviewResponse(InboxReputationDetailItemViewModel element);

        void onErrorDeleteReviewResponse(String errorMessage);

        void onSuccessDeleteReviewResponse();
    }

    interface Presenter extends CustomerPresenter<View> {

        void getInboxDetail(String id, int anInt);

        void sendSmiley(String reputationId, String score, int role);

        void checkShopFavorited(int shopId);

        void onFavoriteShopClicked(int shopId);

        void deleteReviewResponse(String reviewId, String productId, String shopId, String
                reputationId);
    }
}
