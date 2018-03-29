package com.tokopedia.flight.cancellation.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.domain.FlightCancellationGetCancelablePassengerUseCase;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationContract;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationPassengerViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.flight.common.util.FlightPassengerTitleType.NONA;
import static com.tokopedia.flight.common.util.FlightPassengerTitleType.NYONYA;
import static com.tokopedia.flight.common.util.FlightPassengerTitleType.TUAN;

/**
 * @author by furqan on 21/03/18.
 */

public class FlightCancellationPresenter extends BaseDaggerPresenter<FlightCancellationContract.View>
        implements FlightCancellationContract.Presenter {

    FlightCancellationGetCancelablePassengerUseCase flightCancellationGetCancelablePassengerUseCase;

    @Inject
    public FlightCancellationPresenter(FlightCancellationGetCancelablePassengerUseCase flightCancellationGetCancelablePassengerUseCase) {
        this.flightCancellationGetCancelablePassengerUseCase = flightCancellationGetCancelablePassengerUseCase;
    }

    @Override
    public void onViewCreated() {
        getCancelablePassenger();
    }

    @Override
    public void uncheckPassenger(FlightCancellationPassengerViewModel passengerViewModel, int position) {
        FlightCancellationViewModel flightCancellationViewModel = getView().getSelectedCancellationViewModel().get(position);
        flightCancellationViewModel.getPassengerViewModelList().remove(passengerViewModel);
    }

    private void getCancelablePassenger() {
        this.flightCancellationGetCancelablePassengerUseCase.execute(
                flightCancellationGetCancelablePassengerUseCase.generateRequestParams(
                        getView().getInvoiceId()
                ),
                new Subscriber<List<FlightCancellationViewModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onNext(List<FlightCancellationViewModel> flightCancellationViewModels) {
                        transformJourneyToCancellationViewModel(flightCancellationViewModels);
                    }
                }
        );
    }

    private void transformJourneyToCancellationViewModel(List<FlightCancellationViewModel> flightCancellationViewModelList) {
        int index = 0;
        List<FlightCancellationViewModel> selectedViewModel = new ArrayList<>();
        List<FlightCancellationViewModel> cancellationModelList = new ArrayList<>();

        for (FlightCancellationViewModel item : flightCancellationViewModelList) {
            if (getView().getFlightCancellationJourney().size() > index) {
                FlightCancellationViewModel flightCancellationViewModel = new FlightCancellationViewModel();
                flightCancellationViewModel.setFlightCancellationJourney(getView().getFlightCancellationJourney().get(index));
                flightCancellationViewModel.setPassengerViewModelList(transformPassengerList(item.getPassengerViewModelList()));
                cancellationModelList.add(flightCancellationViewModel);

                FlightCancellationViewModel cancellationForSelectedViewModelList = new FlightCancellationViewModel();
                cancellationForSelectedViewModelList.setFlightCancellationJourney(getView().getFlightCancellationJourney().get(index));
                cancellationForSelectedViewModelList.setPassengerViewModelList(new ArrayList<FlightCancellationPassengerViewModel>());
                selectedViewModel.add(cancellationForSelectedViewModelList);

                index++;
            }
        }

        getView().setSelectedCancellationViewModel(selectedViewModel);
        getView().setFlightCancellationViewModel(cancellationModelList);
        getView().renderCancelableList();
    }

    private List<FlightCancellationPassengerViewModel> transformPassengerList(List<FlightCancellationPassengerViewModel> passengerList) {
        for (FlightCancellationPassengerViewModel item : passengerList) {
            item.setTitleString(getTitleString(item.getTitle()));
        }

        return passengerList;
    }

    private String getTitleString(int typeId) {
        switch (typeId) {
            case TUAN:
                return getView().getString(R.string.mister);
            case NYONYA:
                return getView().getString(R.string.misiz);
            case NONA:
                return getView().getString(R.string.miss);
            default:
                return getView().getString(R.string.mister);
        }
    }
}
