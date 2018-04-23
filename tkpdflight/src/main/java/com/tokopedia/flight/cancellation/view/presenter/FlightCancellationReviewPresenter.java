package com.tokopedia.flight.cancellation.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.data.cloud.entity.CancellationRequestEntity;
import com.tokopedia.flight.cancellation.domain.FlightCancellationRequestUseCase;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationReviewContract;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by furqan on 11/04/18.
 */

public class FlightCancellationReviewPresenter extends BaseDaggerPresenter<FlightCancellationReviewContract.View>
        implements FlightCancellationReviewContract.Presenter {

    private FlightCancellationRequestUseCase flightCancellationRequestUseCase;

    @Inject
    public FlightCancellationReviewPresenter(FlightCancellationRequestUseCase flightCancellationRequestUseCase) {
        this.flightCancellationRequestUseCase = flightCancellationRequestUseCase;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void requestCancellation() {
        FlightCancellationWrapperViewModel viewModel = getView().getCancellationWrapperViewModel();

        String reason = (viewModel.getCancellationReasonAndAttachment() != null) ?
                viewModel.getCancellationReasonAndAttachment().getReason() : null;
        List<FlightCancellationAttachmentViewModel> attachmentViewModelList = (viewModel.getCancellationReasonAndAttachment() != null) ?
                viewModel.getCancellationReasonAndAttachment().getAttachments() : null;
        Long estimatedRefund = (viewModel.getCancellationReasonAndAttachment() != null) ?
                viewModel.getCancellationReasonAndAttachment().getEstimateRefund() : null;


        flightCancellationRequestUseCase.execute(
                flightCancellationRequestUseCase.createRequest(
                        getView().getInvoiceId(),
                        reason,
                        attachmentViewModelList,
                        estimatedRefund,
                        viewModel.getGetCancellations()
                ),
                new Subscriber<CancellationRequestEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onNext(CancellationRequestEntity cancellationRequestEntity) {
                        if (isRefundable()) {
                            getView().showSuccessDialog(R.string.flight_cancellation_review_dialog_refundable_success_description);
                        } else {
                            getView().showSuccessDialog(R.string.flight_cancellation_review_dialog_non_refundable_success_description);
                        }
                    }
                }
        );
    }

    private boolean isRefundable() {
        boolean isRefundable = false;

        for (FlightCancellationViewModel item : getView().getCancellationWrapperViewModel().getGetCancellations()) {
            if (item.getFlightCancellationJourney().isRefundable()) {
                isRefundable = true;
                break;
            }
        }

        return isRefundable;
    }
}
