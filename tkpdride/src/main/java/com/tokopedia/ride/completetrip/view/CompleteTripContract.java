package com.tokopedia.ride.completetrip.view;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.completetrip.domain.model.Receipt;

/**
 * Created by alvarisi on 3/31/17.
 */

public interface CompleteTripContract {
    interface View extends CustomerView {
        void showGetReceiptLoading();

        void hideGetReceiptLoading();

        RequestParams getReceiptParam();

        void renderReceipt(Receipt receipt, boolean isPendingPaymentExists);

        void showMessage(String message);

        void showReceiptLayout();

        void showErrorLayout();

        void hideReceiptLayout();

        RequestParams getRatingParam();

        void showRatingSuccessDialog();

        void showRatingErrorLayout();

        void hideRatingLayout();

        void showRatingLayout();

        boolean isCameFromPushNotif();

        RequestParams getRideHistoryParam();

        void clearRideNotificationIfExists();

        void showRatingResultLayout(int star);

        String getRateStars();

        void closePage();
    }

    interface Presenter extends CustomerPresenter<View> {
        void actionGetReceipt();

        void actionSendRating();
    }
}
