package com.tokopedia.flight.cancellation.view.presenter;

import android.net.Uri;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.domain.FlightCancellationUploadImageUseCase;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationReasonAndProofContract;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationPassengerViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonAndAttachmentViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author by alvarisi on 3/26/18.
 */

public class FlightCancellationReasonAndProofPresenter extends BaseDaggerPresenter<FlightCancellationReasonAndProofContract.View> implements FlightCancellationReasonAndProofContract.Presenter {

    private FlightCancellationUploadImageUseCase flightCancellationUploadImageUseCase;
    private CompositeSubscription compositeSubscription;

    @Inject
    public FlightCancellationReasonAndProofPresenter(FlightCancellationUploadImageUseCase flightCancellationUploadImageUseCase) {
        this.flightCancellationUploadImageUseCase = flightCancellationUploadImageUseCase;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void initialize() {
        getView().showUploadAttachmentView();
    }

    @Override
    public void onSuccessGetImage(String filepath) {
        FlightCancellationAttachmentViewModel viewModel = new FlightCancellationAttachmentViewModel();
        viewModel.setFilepath(filepath);
        viewModel.setFilename(Uri.parse(filepath).getLastPathSegment());
        getView().hideUploadAttachmentView();
        getView().addAttachment(viewModel);
        getView().showUploadAttachmentView();
    }

    @Override
    public void onNextButtonClicked() {
        if (validateFields()) {
            getView().hideFullPageContainer();
            getView().showLoading();
            List<FlightCancellationAttachmentViewModel> attachments = getView().getAttachments();
            compositeSubscription.add(Observable.from(attachments)
                    .flatMap(new Func1<FlightCancellationAttachmentViewModel, Observable<FlightCancellationAttachmentViewModel>>() {
                        @Override
                        public Observable<FlightCancellationAttachmentViewModel> call(FlightCancellationAttachmentViewModel attachmentViewModel) {
                            return Observable.zip(Observable.just(attachmentViewModel),
                                    flightCancellationUploadImageUseCase.createObservable(
                                            flightCancellationUploadImageUseCase.createParam(attachmentViewModel.getFilepath())
                                    ), new Func2<FlightCancellationAttachmentViewModel, String, FlightCancellationAttachmentViewModel>() {
                                        @Override
                                        public FlightCancellationAttachmentViewModel call(FlightCancellationAttachmentViewModel attachmentViewModel, String s) {
                                            attachmentViewModel.setImageurl(s);
                                            return attachmentViewModel;
                                        }
                                    });
                        }
                    })
                    .toList()
                    .map(new Func1<List<FlightCancellationAttachmentViewModel>, FlightCancellationWrapperViewModel>() {
                        @Override
                        public FlightCancellationWrapperViewModel call(List<FlightCancellationAttachmentViewModel> flightCancellationAttachmentViewModels) {
                            FlightCancellationWrapperViewModel viewModel = getView().getCancellationViewModel();
                            FlightCancellationReasonAndAttachmentViewModel reasonAndAttachmentViewModel = new FlightCancellationReasonAndAttachmentViewModel();
                            reasonAndAttachmentViewModel.setReason(getView().getReason());
                            reasonAndAttachmentViewModel.setAttachments(flightCancellationAttachmentViewModels);
                            viewModel.setCancellationReasonAndAttachment(reasonAndAttachmentViewModel);
                            return viewModel;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<FlightCancellationWrapperViewModel>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            if (isViewAttached() && !isUnsubscribed()) {
                                getView().showFullPageContainer();
                                getView().hideLoading();
                                getView().showFailedToNextStepErrorMessage(ErrorHandler.getErrorMessage(getView().getActivity(), e));
                            }
                        }

                        @Override
                        public void onNext(FlightCancellationWrapperViewModel viewModel) {
                            getView().navigateToNextStep(viewModel);
                        }
                    })
            );
        }
    }

    @Override
    public void onDestroy() {
        if (compositeSubscription.hasSubscriptions()) compositeSubscription.unsubscribe();
    }

    private boolean validateFields() {
        boolean isValid = true;
        List<FlightCancellationAttachmentViewModel> attachments = getView().getAttachments();
        int totalPassenger = calculateTotalPassenger(getView().getCancellationViewModel());
        if (attachments.size() == 0) {
            isValid = false;
            getView().showRequiredMinimalOneAttachmentErrorMessage(R.string.flight_cancellation_attachment_required_error_message);
        } else if (attachments.size() > totalPassenger + 1) {
            isValid = false;
            getView().showAttachmentGreaterThanPassengersTotalAndRequiredAttachmentErrorMessage(
                    String.format(
                            getView().getString(R.string.flight_cancellation_attachment_more_than_max_error_message), totalPassenger + 1)
            );
        }
        return isValid;
    }

    private int calculateTotalPassenger(FlightCancellationWrapperViewModel cancellationViewModel) {
        List<String> uniquePassengers = new ArrayList<>();
        for (FlightCancellationViewModel viewModel : cancellationViewModel.getViewModels()) {
            for (FlightCancellationPassengerViewModel passengerViewModel : viewModel.getPassengerViewModelList()) {
                if (!uniquePassengers.contains(passengerViewModel.getPassengerId())) {
                    uniquePassengers.add(passengerViewModel.getPassengerId());
                }
            }
        }
        return uniquePassengers.size();
    }
}
