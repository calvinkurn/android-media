package com.tokopedia.tkpd.tkpdreputation.inbox.view.listener;

import android.app.Activity;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageUpload;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.sendreview.SendReviewPass;

import java.util.ArrayList;

/**
 * @author by nisie on 8/20/17.
 */

public interface InboxReputationForm {
    interface View extends CustomerView {

        void showLoadingProgress();

        void onErrorSendReview(String errorMessage);

        void onSuccessSendReview();

        void finishLoadingProgress();

        Activity getActivity();

        void setFormFromCache(SendReviewPass sendReviewPass);
    }

    interface Presenter extends CustomerPresenter<View> {

        void sendReview(String reviewId, String reputationId, String productId, String shopId,
                        String review, float rating, ArrayList<ImageUpload> list,
                        boolean shareFb, boolean anonymous);

        void openCamera();

        void openImageGallery();

        void setFormToCache(int position, SendReviewPass sendReviewPass);

        String getFileLocFromCamera();

        void restoreFormFromCache();

    }
}
