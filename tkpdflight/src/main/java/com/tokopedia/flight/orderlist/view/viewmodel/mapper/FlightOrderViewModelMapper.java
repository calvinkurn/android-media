package com.tokopedia.flight.orderlist.view.viewmodel.mapper;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.R;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderFailedViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderInProcessViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderRefundViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderSuccessViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderWaitingForPaymentViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by alvarisi on 12/14/17.
 */

public class FlightOrderViewModelMapper {
    @Inject
    public FlightOrderViewModelMapper() {
    }

    public List<Visitable> transform(List<FlightOrder> flightOrders) {
        List<Visitable> visitables = new ArrayList<>();
        for (FlightOrder flightOrder : flightOrders) {
            switch (flightOrder.getStatus()) {
                case 700:
                case 800:
                    for (FlightOrderJourney journey : flightOrder.getJourneys()) {
                        FlightOrderSuccessViewModel successViewModel = new FlightOrderSuccessViewModel();
                        successViewModel.setCreateTime(flightOrder.getCreateTime());
                        successViewModel.setId(flightOrder.getId());
                        successViewModel.setOrderJourney(journey);
                        successViewModel.setTitle(R.string.flight_order_success_title);
                        successViewModel.setStatus(flightOrder.getStatus());
                        visitables.add(successViewModel);
                    }
                    break;
                case 600:
                    FlightOrderFailedViewModel failedViewModel = new FlightOrderFailedViewModel();
                    failedViewModel.setCreateTime(flightOrder.getCreateTime());
                    failedViewModel.setId(flightOrder.getId());
                    failedViewModel.setOrderJourney(flightOrder.getJourneys());
                    failedViewModel.setStatus(flightOrder.getStatus());
                    failedViewModel.setTitle(R.string.flight_order_failed_title);
                    visitables.add(failedViewModel);
                    break;
                case 0:
                    FlightOrderFailedViewModel expired = new FlightOrderFailedViewModel();
                    expired.setCreateTime(flightOrder.getCreateTime());
                    expired.setId(flightOrder.getId());
                    expired.setOrderJourney(flightOrder.getJourneys());
                    expired.setStatus(flightOrder.getStatus());
                    expired.setTitle(R.string.flight_order_expire_title);
                    visitables.add(expired);
                    break;
                case 101:
                case 200:
                case 300:
                    FlightOrderInProcessViewModel inProcessViewModel = new FlightOrderInProcessViewModel();
                    inProcessViewModel.setCreateTime(flightOrder.getCreateTime());
                    inProcessViewModel.setId(flightOrder.getId());
                    inProcessViewModel.setOrderJourney(flightOrder.getJourneys());
                    inProcessViewModel.setStatus(flightOrder.getStatus());
                    switch (flightOrder.getStatus()) {
                        case 101:
                        case 200:
                            inProcessViewModel.setTitle(R.string.flight_order_waiting_for_confirmation_title);
                            break;
                        case 300:
                            inProcessViewModel.setTitle(R.string.flight_order_in_progress_title);
                            break;
                    }
                    visitables.add(inProcessViewModel);
                    break;
                case 100:
                case 102:
                    FlightOrderWaitingForPaymentViewModel waitingForPaymentViewModel = new FlightOrderWaitingForPaymentViewModel();
                    waitingForPaymentViewModel.setCreateTime(flightOrder.getCreateTime());
                    waitingForPaymentViewModel.setId(flightOrder.getId());
                    waitingForPaymentViewModel.setOrderJourney(flightOrder.getJourneys());
                    waitingForPaymentViewModel.setStatus(flightOrder.getStatus());
                    waitingForPaymentViewModel.setTitle(R.string.flight_order_waiting_for_payment_title);
                    visitables.add(waitingForPaymentViewModel);
                    break;
                case 650:
                    FlightOrderRefundViewModel refundViewModel = new FlightOrderRefundViewModel();
                    refundViewModel.setCreateTime(flightOrder.getCreateTime());
                    refundViewModel.setId(flightOrder.getId());
                    refundViewModel.setOrderJourney(flightOrder.getJourneys());
                    refundViewModel.setStatus(flightOrder.getStatus());
                    refundViewModel.setTitle(R.string.flight_order_waiting_for_payment_title);
                    visitables.add(refundViewModel);
                    break;
            }
        }
        return visitables;
    }
}
