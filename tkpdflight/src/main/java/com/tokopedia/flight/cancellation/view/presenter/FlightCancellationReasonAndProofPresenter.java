package com.tokopedia.flight.cancellation.view.presenter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airline.domain.FlightAirlineUseCase;
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
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author by alvarisi on 3/26/18.
 */

public class FlightCancellationReasonAndProofPresenter extends BaseDaggerPresenter<FlightCancellationReasonAndProofContract.View> implements FlightCancellationReasonAndProofContract.Presenter {

    private FlightAirlineUseCase flightAirlineUseCase;
    private FlightCancellationUploadImageUseCase flightCancellationUploadImageUseCase;
    private CompositeSubscription compositeSubscription;

    @Inject
    public FlightCancellationReasonAndProofPresenter(FlightAirlineUseCase flightAirlineUseCase,
                                                     FlightCancellationUploadImageUseCase flightCancellationUploadImageUseCase) {
        this.flightAirlineUseCase = flightAirlineUseCase;
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
        List<FlightCancellationAttachmentViewModel> attachments = getView().getAttachments();
        Observable<Boolean> isRequiredAttachment = checkIfAttachmentMandatory();
        Observable<Boolean> isValidRequiredAttachment = Observable.zip(Observable.just(attachments), isRequiredAttachment, new Func2<List<FlightCancellationAttachmentViewModel>, Boolean, Boolean>() {
            @Override
            public Boolean call(List<FlightCancellationAttachmentViewModel> flightCancellationAttachmentViewModels, Boolean aBoolean) {
                return !aBoolean || (aBoolean && flightCancellationAttachmentViewModels.size() > 0);
            }
        });

        isValidRequiredAttachment.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (!aBoolean)
                    getView().showRequiredMinimalOneAttachmentErrorMessage(R.string.flight_cancellation_attachment_required_error_message);
            }
        });

        Observable<Boolean> isValidAttachmentLength = Observable.just(getView().getCancellationViewModel())
                .map(new Func1<FlightCancellationWrapperViewModel, Integer>() {
                    @Override
                    public Integer call(FlightCancellationWrapperViewModel flightCancellationWrapperViewModel) {
                        return calculateTotalPassenger(getView().getCancellationViewModel());
                    }
                }).zipWith(isRequiredAttachment, new Func2<Integer, Boolean, Boolean>() {
                    @Override
                    public Boolean call(Integer integer, Boolean aBoolean) {
                        return !aBoolean || (aBoolean && integer > 0);
                    }
                });

        isValidAttachmentLength.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (!aBoolean) {
                    int totalPassenger = calculateTotalPassenger(getView().getCancellationViewModel());
                    getView().showAttachmentGreaterThanPassengersTotalAndRequiredAttachmentErrorMessage(
                            String.format(
                                    getView().getString(R.string.flight_cancellation_attachment_more_than_max_error_message), totalPassenger + 1)
                    );
                }
            }
        });

        Observable.combineLatest(
                isValidRequiredAttachment, isValidAttachmentLength, new Func2<Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean, Boolean aBoolean2) {
                        return aBoolean && aBoolean2;
                    }
                }
        ).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    actionUploadImageAndBuildModel();
                }
            }
        });

//        checkIfAttachmentMandatory()
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Boolean>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(Boolean aBoolean) {
//                        validateFields(aBoolean);
//                    }
//                });

    }

    @NonNull
    private Observable<Boolean> checkIfAttachmentMandatory() {
        return Observable.defer(new Func0<Observable<List<String>>>() {
            @Override
            public Observable<List<String>> call() {
                final List<String> airlineIds = new ArrayList<>();
                for (FlightCancellationViewModel viewModel : getView().getCancellationViewModel().getGetCancellations()) {
                    for (String airline : viewModel.getFlightCancellationJourney().getAirlineIds()) {
                        if (!airlineIds.contains(airline)) {
                            airlineIds.add(airline);
                        }
                    }
                }
                return Observable.just(airlineIds);
            }
        }).flatMap(new Func1<List<String>, Observable<List<FlightAirlineDB>>>() {
            @Override
            public Observable<List<FlightAirlineDB>> call(List<String> strings) {
                return Observable.from(strings)
                        .flatMap(new Func1<String, Observable<FlightAirlineDB>>() {
                            @Override
                            public Observable<FlightAirlineDB> call(String s) {
                                return Observable.zip(Observable.just(s), flightAirlineUseCase.createObservable(flightAirlineUseCase.createRequestParam(s)), new Func2<String, FlightAirlineDB, FlightAirlineDB>() {
                                    @Override
                                    public FlightAirlineDB call(String s, FlightAirlineDB flightAirlineDB) {
                                        return flightAirlineDB;
                                    }
                                });
                            }
                        }).filter(new Func1<FlightAirlineDB, Boolean>() {
                            @Override
                            public Boolean call(FlightAirlineDB flightAirlineDB) {
                                return flightAirlineDB.getMandatoryRefundAttachment() == 1;
                            }
                        }).toList();
            }
        }).map(new Func1<List<FlightAirlineDB>, Boolean>() {
            @Override
            public Boolean call(List<FlightAirlineDB> flightAirlineDBS) {
                return flightAirlineDBS.size() > 0;
            }
        });
    }

    @Override
    public void onDestroy() {
        if (compositeSubscription.hasSubscriptions()) compositeSubscription.unsubscribe();
    }

    private void validateFields(Boolean requiredAttachment) {
        List<FlightCancellationAttachmentViewModel> attachments = getView().getAttachments();
        Observable<Boolean> isValidRequiredAttachment = Observable.zip(Observable.just(attachments), Observable.just(requiredAttachment), new Func2<List<FlightCancellationAttachmentViewModel>, Boolean, Boolean>() {
            @Override
            public Boolean call(List<FlightCancellationAttachmentViewModel> flightCancellationAttachmentViewModels, Boolean aBoolean) {
                return !aBoolean || (aBoolean && flightCancellationAttachmentViewModels.size() > 0);
            }
        });

        isValidRequiredAttachment.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (!aBoolean)
                    getView().showRequiredMinimalOneAttachmentErrorMessage(R.string.flight_cancellation_attachment_required_error_message);
            }
        });

        Observable<Boolean> isValidAttachmentLength = Observable.just(getView().getCancellationViewModel())
                .map(new Func1<FlightCancellationWrapperViewModel, Integer>() {
                    @Override
                    public Integer call(FlightCancellationWrapperViewModel flightCancellationWrapperViewModel) {
                        return calculateTotalPassenger(getView().getCancellationViewModel());
                    }
                }).zipWith(Observable.just(requiredAttachment), new Func2<Integer, Boolean, Boolean>() {
                    @Override
                    public Boolean call(Integer integer, Boolean aBoolean) {
                        return !aBoolean || (aBoolean && integer > 0);
                    }
                });

        isValidAttachmentLength.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (!aBoolean) {
                    int totalPassenger = calculateTotalPassenger(getView().getCancellationViewModel());
                    getView().showAttachmentGreaterThanPassengersTotalAndRequiredAttachmentErrorMessage(
                            String.format(
                                    getView().getString(R.string.flight_cancellation_attachment_more_than_max_error_message), totalPassenger + 1)
                    );
                }
            }
        });

        Observable.combineLatest(
                isValidRequiredAttachment, isValidAttachmentLength, new Func2<Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean, Boolean aBoolean2) {
                        return aBoolean && aBoolean2;
                    }
                }
        ).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    actionUploadImageAndBuildModel();
                }
            }
        });
    }

    private void actionUploadImageAndBuildModel() {
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

    private int calculateTotalPassenger(FlightCancellationWrapperViewModel cancellationViewModel) {
        List<String> uniquePassengers = new ArrayList<>();
        for (FlightCancellationViewModel viewModel : cancellationViewModel.getGetCancellations()) {
            for (FlightCancellationPassengerViewModel passengerViewModel : viewModel.getPassengerViewModelList()) {
                if (!uniquePassengers.contains(passengerViewModel.getPassengerId())) {
                    uniquePassengers.add(passengerViewModel.getPassengerId());
                }
            }
        }
        return uniquePassengers.size();
    }
}
