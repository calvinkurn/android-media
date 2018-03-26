package com.tokopedia.flight.cancellation.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.cancellation.domain.FlightCancellationGetCancelablePassengerUseCase;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationContract;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationJourney;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

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
                        getView().setFlightCancellationViewModel(flightCancellationViewModels);
                        getView().renderCancelableList();
                    }
                }
        );
    }

    private void transformJourneyToCancellationViewModel(List<FlightCancellationViewModel> flightCancellationViewModels) {

    }
}
