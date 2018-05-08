package com.tokopedia.flight.cancellation.view.presenter;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airline.domain.FlightAirlineUseCase;
import com.tokopedia.flight.cancellation.domain.model.AttachmentImageModel;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationReasonAndProofContract;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationPassengerViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonAndAttachmentViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperViewModel;
import com.tokopedia.imageuploader.domain.UploadImageUseCase;
import com.tokopedia.imageuploader.domain.model.ImageUploadDomainModel;
import com.tokopedia.usecase.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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
    private static final String DEFAULT_RESOLUTION = "100-square";
    private static final String RESOLUTION_300 = "300";
    private static final String PARAM_ID = "id";
    private static final String PARAM_WEB_SERVICE = "web_service";
    private static final String PARAM_RESOLUTION = "param_resolution";
    private static final String DEFAULT_UPLOAD_PATH = "/upload/attachment";
    private static final String DEFAULT_UPLOAD_TYPE = "fileToUpload\"; filename=\"image.jpg";

    private FlightAirlineUseCase flightAirlineUseCase;
    private UploadImageUseCase<AttachmentImageModel> uploadImageUseCase;
    private CompositeSubscription compositeSubscription;
    private UserSession userSession;
    private FlightModuleRouter flightModuleRouter;

    @Inject
    public FlightCancellationReasonAndProofPresenter(FlightAirlineUseCase flightAirlineUseCase,
                                                     UploadImageUseCase<AttachmentImageModel> uploadImageUseCase,
                                                     UserSession userSession,
                                                     FlightModuleRouter flightModuleRouter) {
        this.flightAirlineUseCase = flightAirlineUseCase;
        this.uploadImageUseCase = uploadImageUseCase;
        this.userSession = userSession;
        this.flightModuleRouter = flightModuleRouter;
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

    @Override
    public void onComeFromEstimateRefundScreen() {
        getView().hideLoading();
        getView().showFullPageContainer();
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
                                uploadImageUseCase.createObservable(
                                        createParam(attachmentViewModel.getFilepath())
                                ), new Func2<FlightCancellationAttachmentViewModel, ImageUploadDomainModel<AttachmentImageModel>, FlightCancellationAttachmentViewModel>() {
                                    @Override
                                    public FlightCancellationAttachmentViewModel call(FlightCancellationAttachmentViewModel attachmentViewModel, ImageUploadDomainModel<AttachmentImageModel> uploadDomainModel) {
                                        String url = uploadDomainModel.getDataResultImageUpload().getData().getPicSrc();
                                        if (url.contains(DEFAULT_RESOLUTION)) {
                                            url = url.replaceFirst(DEFAULT_RESOLUTION, RESOLUTION_300);
                                        }
                                        attachmentViewModel.setImageurl(url);
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

    private RequestParams createParam(String cameraLoc) {
        File photo = flightModuleRouter.writeImage(cameraLoc, 80);
        Map<String, RequestBody> maps = new HashMap<String, RequestBody>();
        RequestBody webService = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody resolution = RequestBody.create(MediaType.parse("text/plain"), RESOLUTION_300);
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), userSession.getUserId() + UUID.randomUUID() + System.currentTimeMillis());
        maps.put(PARAM_WEB_SERVICE, webService);
        maps.put(PARAM_ID, id);
        maps.put(PARAM_RESOLUTION, resolution);
        return uploadImageUseCase.createRequestParam(photo.getAbsolutePath(), DEFAULT_UPLOAD_PATH, DEFAULT_UPLOAD_TYPE, maps);
    }
}
