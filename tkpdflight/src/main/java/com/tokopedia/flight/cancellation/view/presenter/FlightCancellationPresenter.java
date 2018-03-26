package com.tokopedia.flight.cancellation.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.domain.FlightCancellationGetCancelablePassengerUseCase;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationContract;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationPassengerViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;

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

    private void transformJourneyToCancellationViewModel(List<FlightCancellationViewModel> flightCancellationViewModels) {
        int index = 0;

        for (FlightCancellationViewModel item : flightCancellationViewModels) {
            item.setFlightCancellationJourney(getView().getFlightCancellationJourney().get(index));
            item.setPassengerViewModelList(transformPassengerList(item.getPassengerViewModelList()));
            index++;
        }

        getView().setFlightCancellationViewModel(flightCancellationViewModels);
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
            case TUAN : return getView().getString(R.string.mister);
            case NYONYA : return getView().getString(R.string.misiz);
            case NONA : return getView().getString(R.string.miss);
            default: return getView().getString(R.string.mister);
        }
    }
}
