package com.tokopedia.flight.detail.presenter;

import android.text.TextUtils;
import android.util.Base64;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.flight.common.util.FlightAmenityType;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.util.FlightPassengerTitleType;
import com.tokopedia.flight.common.util.FlightStatusOrderType;
import com.tokopedia.flight.orderlist.domain.FlightGetOrderUseCase;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderPassengerViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailPassData;
import com.tokopedia.flight.review.view.model.FlightDetailPassenger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 12/13/17.
 */

public class FlightDetailOrderPresenter extends BaseDaggerPresenter<FlightDetailOrderContract.View> implements FlightDetailOrderContract.Presenter {

    private final FlightGetOrderUseCase flightGetOrderUseCase;

    @Inject
    public FlightDetailOrderPresenter(FlightGetOrderUseCase flightGetOrderUseCase) {
        this.flightGetOrderUseCase = flightGetOrderUseCase;
    }

    public void getDetail(String orderId, FlightOrderDetailPassData flightOrderDetailPassData) {
        getView().showProgressDialog();
        flightGetOrderUseCase.execute(flightGetOrderUseCase.createRequestParams(orderId), getSubscriberGetDetailOrder(flightOrderDetailPassData));
    }

    @Override
    public void actionCancelOrderButtonClicked() {
        getView().navigateToContactUs(getView().getFlightOrder());
    }

    @Override
    public void onHelpButtonClicked() {
        String url = FlightUrl.CONTACT_US_FLIGHT_PREFIX + generateGeneralFlightContactUs();
        getView().navigateToWebview(url);
    }

    @Override
    public void actionReorderButtonClicked() {
        getView().navigateToFlightHomePage();
    }

    private String generateGeneralFlightContactUs() {
        return Base64.encodeToString(getView().getActivity().getString(R.string.flight_order_flight_default_contact_us).getBytes(), Base64.DEFAULT);
    }


    private Subscriber<FlightOrder> getSubscriberGetDetailOrder(final FlightOrderDetailPassData flightOrderDetailPassData) {
        return new Subscriber<FlightOrder>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideProgressDialog();
                    getView().onErrorGetOrderDetail(e);
                }
            }

            @Override
            public void onNext(FlightOrder flightOrder) {
                getView().hideProgressDialog();
                getView().renderFlightOrder(flightOrder);
                getView().updateFlightList(filterFlightJourneys(flightOrder.getStatus(), flightOrder.getJourneys(), flightOrderDetailPassData));
                getView().updatePassengerList(transformToListPassenger(flightOrder.getPassengerViewModels()));
                getView().updatePrice(transformToSimpleModelPrice(), countTotalPrice(flightOrder.getTotalAdultNumeric(),
                        flightOrder.getTotalChildNumeric(), flightOrder.getTotalInfantNumeric()));
                getView().updateOrderData(FlightDateUtil.formatDate(FlightDateUtil.FORMAT_DATE_API_DETAIL,
                        FlightDateUtil.FORMAT_DATE_LOCAL_DETAIL_ORDER, flightOrder.getCreateTime()),
                        generateTicketLink(flightOrder.getId()), generateInvoiceLink(flightOrder.getId()),
                        generateCancelMessage(flightOrder));
                generateStatus(flightOrder.getStatus());
            }
        };
    }

    private String generateCancelMessage(FlightOrder flightOrder) {
        String newLine = "\n";
        StringBuilder result = new StringBuilder();
        result.append(newLine);
        for (FlightOrderJourney flightOrderJourney : flightOrder.getJourneys()) {
            String item = flightOrderJourney.getDepartureAiportId() + "-" + flightOrderJourney.getArrivalAirportId() + " ";
            item += newLine;
            ArrayList<String> passengers = new ArrayList<>();
            for (FlightOrderPassengerViewModel flightOrderPassengerViewModel : flightOrder.getPassengerViewModels()) {
                passengers.add(flightOrderPassengerViewModel.getPassengerFirstName() + " " + flightOrderPassengerViewModel.getPassengerLastName());
            }
            item += TextUtils.join(newLine, passengers);
            result.append(item);
        }
        return result.toString();
    }

    private String generateInvoiceLink(String orderId) {
        return FlightUrl.getUrlPdf(orderId);
    }

    private String generateTicketLink(String orderId) {
        return FlightUrl.getUrlPdf(orderId);
    }

    private void generateStatus(int status) {
        switch (status) {
            case FlightStatusOrderType.EXPIRED:
                getView().updateViewExpired();
                break;
            case FlightStatusOrderType.CONFIRMED:
                getView().updateViewConfirmed();
                break;
            case FlightStatusOrderType.FAILED:
                getView().updateViewFailed();
                break;
            case FlightStatusOrderType.FINISHED:
                getView().updateViewFinished();
                break;
            case FlightStatusOrderType.PROGRESS:
                getView().updateViewProgress();
                break;
            case FlightStatusOrderType.READY_FOR_QUEUE:
                getView().updateViewReadyForQueue();
                break;
            case FlightStatusOrderType.REFUNDED:
                getView().updateViewRefunded();
                break;
            case FlightStatusOrderType.WAITING_FOR_PAYMENT:
                getView().updateViewWaitingForPayment();
                break;
            case FlightStatusOrderType.WAITING_FOR_THIRD_PARTY:
                getView().updateViewWaitingForThirdParty();
                break;
            case FlightStatusOrderType.WAITING_FOR_TRANSFER:
                getView().updateViewWaitingForTransfer();
                break;
            default:
                break;
        }
    }

    private List<FlightOrderJourney> filterFlightJourneys(int status, List<FlightOrderJourney> journeys, FlightOrderDetailPassData flightOrderDetailPassData) {
        List<FlightOrderJourney> journeyList;
        if (!TextUtils.isEmpty(flightOrderDetailPassData.getDepartureAiportId())) {
            journeyList = new ArrayList<>();
            for (FlightOrderJourney flightOrderJourney : journeys) {
                if (flightOrderJourney.getDepartureAiportId().equals(flightOrderDetailPassData.getDepartureAiportId()) &&
                        flightOrderJourney.getArrivalAirportId().equals(flightOrderDetailPassData.getArrivalAirportId()) &&
                        status == flightOrderDetailPassData.getStatus() &&
                        flightOrderJourney.getDepartureTime().equals(flightOrderDetailPassData.getDepartureTime())) {
                    journeyList.add(flightOrderJourney);
                }
            }
        } else {
            journeyList = journeys;
        }
        return journeyList;
    }

    private List<SimpleViewModel> transformToSimpleModelPrice() {
        return new ArrayList<>();
    }

    private String countTotalPrice(int totalAdultNumeric, int totalChildNumeric, int totalInfantNumeric) {
        return CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(totalAdultNumeric + totalChildNumeric + totalInfantNumeric);
    }

    private List<FlightDetailPassenger> transformToListPassenger(List<FlightOrderPassengerViewModel> passengerViewModels) {
        List<FlightDetailPassenger> flightDetailPassengers = new ArrayList<>();
        for (FlightOrderPassengerViewModel flightOrderPassengerViewModel : passengerViewModels) {
            FlightDetailPassenger flightDetailPassenger = new FlightDetailPassenger();
            flightDetailPassenger.setPassengerName(String.format("%s %s %s", generateSalutation(flightOrderPassengerViewModel.getPassengerTitleId()),
                    flightOrderPassengerViewModel.getPassengerFirstName(), flightOrderPassengerViewModel.getPassengerLastName()));
            flightDetailPassenger.setPassengerType(flightOrderPassengerViewModel.getType());
            flightDetailPassenger.setInfoPassengerList(transformToSimpleModelPassenger(flightOrderPassengerViewModel.getAmenities()));
            flightDetailPassengers.add(flightDetailPassenger);
        }
        return flightDetailPassengers;
    }

    private String generateSalutation(int passengerTitleId) {
        switch (passengerTitleId) {
            case FlightPassengerTitleType.TUAN:
                return getView().getString(R.string.mister);
            case FlightPassengerTitleType.NYONYA:
                return getView().getString(R.string.misiz);
            case FlightPassengerTitleType.NONA:
                return getView().getString(R.string.miss);
            default:
                return "";
        }
    }

    private List<SimpleViewModel> transformToSimpleModelPassenger(List<FlightBookingAmenityViewModel> amenities) {
        List<SimpleViewModel> simpleViewModels = new ArrayList<>();
        for (FlightBookingAmenityViewModel flightBookingAmenityViewModel : amenities) {
            SimpleViewModel simpleViewModel = new SimpleViewModel();
            simpleViewModel.setDescription(generateLabelPassenger(String.valueOf(flightBookingAmenityViewModel.getAmenityType()), flightBookingAmenityViewModel.getDepartureId(),
                    flightBookingAmenityViewModel.getArrivalId()));
            simpleViewModel.setLabel(flightBookingAmenityViewModel.getTitle());
            simpleViewModels.add(simpleViewModel);
        }
        return simpleViewModels;
    }

    private String generateLabelPassenger(String type, String departureId, String arrivalId) {
        switch (type) {
            case FlightAmenityType.LUGGAGE:
                return getView().getString(R.string.flight_luggage_detail_order, departureId, arrivalId);
            case FlightAmenityType.MEAL:
                return getView().getString(R.string.flight_meal_detail_order, departureId, arrivalId);
            default:
                return "";
        }
    }

    @Override
    public void detachView() {
        flightGetOrderUseCase.unsubscribe();
        super.detachView();
    }
}
