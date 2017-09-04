package com.tokopedia.tkpd.tkpdreputation.inbox.view.listener;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailHeaderViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.RevieweeBadgeCustomerViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.RevieweeBadgeSellerViewModel;

import java.util.List;

/**
 * @author by nisie on 8/19/17.
 */

public interface InboxReputationDetail {

    interface View extends CustomerView {

        void showLoading();

        void onErrorGetInboxDetail(String errorMessage);

        void onSuccessGetInboxDetail(RevieweeBadgeCustomerViewModel revieweeBadgeCustomerViewModel,
                                     RevieweeBadgeSellerViewModel revieweeBadgeSellerViewModel,
                                     List<Visitable> list);

        void finishLoading();

        void onEditReview();

        void onSkipReview(String reviewId);

        void onGoToGiveReview(String reviewId, String productId, int shopId);

        void onErrorSendSmiley(String errorMessage);

        void onSuccessSendSmiley();

        void showLoadingDialog();

        void finishLoadingDialog();

        void showRefresh();

        void onErrorRefreshInboxDetail(String errorMessage);

        void onSuccessRefreshGetInboxDetail(RevieweeBadgeCustomerViewModel revieweeBadgeCustomerViewModel,
                                            RevieweeBadgeSellerViewModel revieweeBadgeSellerViewModel,
                                            List<Visitable> visitables);

        void finishRefresh();

    }

    interface Presenter extends CustomerPresenter<View> {

        void getInboxDetail(String id, int anInt);

        void getNextPage(int lastItemPosition, int visibleItem);

        void sendSmiley(String reputationId, String value);
    }
}
