package com.tokopedia.flight.cancellation.view.contract;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperViewModel;

import java.util.List;

/**
 * Created by alvarisi on 3/26/18.
 */

public interface FlightCancellationReasonAndProofContract {
    interface View extends CustomerView{

        void showUploadAttachmentView();

        void hideUploadAttachmentView();

        void addAttachment(FlightCancellationAttachmentViewModel viewModel);

        List<FlightCancellationAttachmentViewModel> getAttachments();

        void showRequiredMinimalOneAttachmentErrorMessage(int resId);

        FlightCancellationWrapperViewModel getCancellationViewModel();

        String getString(int resId);

        void showAttachmentGreaterThanPassengersTotalAndRequiredAttachmentErrorMessage(String errorMessage);

        String getReason();

        Activity getActivity();

        void showFailedToNextStepErrorMessage(String errorMessage);

        void navigateToNextStep(FlightCancellationWrapperViewModel viewModel);

        void hideFullPageContainer();

        void showLoading();

        void showFullPageContainer();

        void hideLoading();

        void showAttachmentMinDimensionErrorMessage(int resId);

        void showAttachmentMaxSizeErrorMessage(int resId);
    }

    interface Presenter extends CustomerPresenter<View>{

        void initialize();

        void onSuccessGetImage(String filepath);

        void onNextButtonClicked();

        void onDestroy();

        void onComeFromEstimateRefundScreen();
    }
}
