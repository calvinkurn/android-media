package com.tokopedia.ride.completetrip.view;

import android.app.Activity;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.common.ride.domain.model.Receipt;

import java.util.ArrayList;

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

        void showErrorInRating(String message);

        void showErrorInDriverTipping(String message);

        void hideRatingLayout();

        void showRatingLayout();

        boolean isCameFromPushNotif();

        RequestParams getRideHistoryParam();

        void clearRideNotificationIfExists();

        void showRatingResultLayout(int star);

        String getRateStars();

        void closePage();

        void showTipLayout();

        void hideTipLayout();

        void enableRatingSubmitButton();

        void disableRatingSubmitButton();

        Activity getActivity();

        ArrayList<String> getFormmattedTipList();

        RequestParams getTipParam();

        int getTipAmount();

        void openScroogePage(String url, String postData);

        void showProgressbar();

        void showAddShortcutDialog();

        void hideProgressbar();
    }

    interface Presenter extends CustomerPresenter<View> {
        void actionGetReceipt();

        void actionSubmitRatingAndDriverTip();

        void handleRatingStarClick(float v);

        void sendTip(RequestParams tipParams);

        void payPendingFare();

        void showPopupToAddShortcutForFirstTime();

        void setShortcutDialogIsShowninCache();
    }
}
