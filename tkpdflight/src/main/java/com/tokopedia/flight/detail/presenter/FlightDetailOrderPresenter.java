package com.tokopedia.flight.detail.presenter;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.constant.FlightBookingPassenger;
import com.tokopedia.flight.booking.domain.subscriber.model.ProfileInfo;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.common.constant.FlightErrorConstant;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.flight.common.data.model.FlightError;
import com.tokopedia.flight.common.data.model.FlightException;
import com.tokopedia.flight.common.util.FlightAmenityType;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.util.FlightPassengerTitleType;
import com.tokopedia.flight.common.util.FlightStatusOrderType;
import com.tokopedia.flight.orderlist.data.cloud.entity.ManualTransferEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.PaymentInfoEntity;
import com.tokopedia.flight.orderlist.domain.FlightGetOrderUseCase;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderPassengerViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailPassData;
import com.tokopedia.flight.review.view.model.FlightDetailPassenger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zulfikarrahman on 12/13/17.
 */

public class FlightDetailOrderPresenter extends BaseDaggerPresenter<FlightDetailOrderContract.View> implements FlightDetailOrderContract.Presenter {

    private static final String NEW_LINE = "\n";
    private final FlightGetOrderUseCase flightGetOrderUseCase;
    private UserSession userSession;
    private CompositeSubscription compositeSubscription;
    private int totalPrice = 0;
    private String userResendEmail = "";

    @Inject
    public FlightDetailOrderPresenter(UserSession userSession, FlightGetOrderUseCase flightGetOrderUseCase) {
        this.userSession = userSession;
        this.flightGetOrderUseCase = flightGetOrderUseCase;
        compositeSubscription = new CompositeSubscription();
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
        getView().navigateToWebview(FlightUrl.CONTACT_US);
    }

    @Override
    public void actionReorderButtonClicked() {
        getView().navigateToFlightHomePage();
    }

    @Override
    public void onDownloadETicketButtonClicked() {
        getView().navigateToInputEmailForm(userSession.getUserId(), userResendEmail);
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


                if (e instanceof FlightException) {
                    for (FlightError flightError : ((FlightException) e).getErrorList()) {
                        if (flightError.getId().equals(FlightErrorConstant.GET_RESULT) && flightError.getStatus().equals("500")) {
                            getView().navigateToFlightHomePage();
                        }
                    }
                }
            }

            @Override
            public void onNext(FlightOrder flightOrder) {
                getView().hideProgressDialog();
                getView().renderFlightOrder(flightOrder);
                List<FlightOrderJourney> flightOrderJourneyList = filterFlightJourneys(
                        flightOrder.getStatus(),
                        flightOrder.getJourneys(),
                        flightOrderDetailPassData
                );
                getView().updateFlightList(flightOrderJourneyList);
                getView().updatePassengerList(transformToListPassenger(flightOrder.getPassengerViewModels()));
                getView().updatePrice(transformToSimpleModelPrice(flightOrder), CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(totalPrice));
                getView().setTransactionDate(
                        FlightDateUtil.formatDateByUsersTimezone(FlightDateUtil.FORMAT_DATE_API_DETAIL,
                                FlightDateUtil.FORMAT_DATE_LOCAL_DETAIL_ORDER, flightOrder.getCreateTime())
                );
                getView().updateOrderData(
                        generateTicketLink(flightOrder.getId(), flightOrder.getPdf(), userSession.getUserId()),
                        generateInvoiceLink(flightOrder.getId()),
                        generateCancelMessage(flightOrderJourneyList, flightOrder.getPassengerViewModels())
                );
                generateStatus(flightOrder.getStatus(), flightOrder.getStatusString());
                renderPaymentInfo(flightOrder);
            }
        };
    }

    @Override
    public void onGetProfileData() {
        compositeSubscription.add(getView().getProfileObservable()
                .onBackpressureDrop()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProfileInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ProfileInfo profileInfo) {
                        if (profileInfo != null && isViewAttached()) {
                            userResendEmail = profileInfo.getEmail();
                        }
                    }
                })
        );
    }

    private void renderPaymentInfo(FlightOrder flightOrder) {
        if (flightOrder.getPayment() != null
                && flightOrder.getPayment().getGatewayName().length() > 0) {

            getView().showPaymentInfoLayout();
            if (flightOrder.getPayment().getManualTransfer() != null && flightOrder.getPayment().getManualTransfer().getAccountBankName().length() > 0) {
                getView().setPaymentLabel(R.string.flight_order_payment_manual_label);
                getView().setPaymentDescription(renderManualPaymentDescriptionText(flightOrder.getPayment().getManualTransfer()));
                getView().setTotalTransfer(flightOrder.getPayment().getManualTransfer().getTotal());
            } else {
                getView().setPaymentLabel(R.string.flight_order_payment_label);
                getView().setPaymentDescription(renderPaymentDescriptionText(flightOrder.getPayment()));
                getView().hideTotalTransfer();
            }

            if (flightOrder.getStatus() == FlightStatusOrderType.WAITING_FOR_THIRD_PARTY
                    || flightOrder.getStatus() == FlightStatusOrderType.WAITING_FOR_TRANSFER) {
                getView().setPaymentDueDate(FlightDateUtil.formatDate(FlightDateUtil.FORMAT_DATE_API, FlightDateUtil.DEFAULT_VIEW_TIME_FORMAT, flightOrder.getPayment().getExpireOn()));
            } else {
                getView().hidePaymentDueDate();
            }
        } else {
            getView().hidePaymentInfoLayout();
        }
    }

    private CharSequence renderPaymentDescriptionText(PaymentInfoEntity payment) {
        SpannableStringBuilder text = new SpannableStringBuilder();
        text.append(payment.getGatewayName().trim());
        makeBold(text);
        if (payment.getTransactionCode() != null && payment.getTransactionCode().length() > 0) {
            SpannableStringBuilder desc = new SpannableStringBuilder();
            desc.append(payment.getTransactionCode());
            makeSmall(desc);
            text.append(NEW_LINE);
            text.append(desc);
        }
        return text;
    }

    private CharSequence renderManualPaymentDescriptionText(ManualTransferEntity manualTransfer) {
        SpannableStringBuilder text = new SpannableStringBuilder();
        text.append(manualTransfer.getAccountBankName());
        makeBold(text);
        SpannableStringBuilder desc = new SpannableStringBuilder();
        String newLine = "\n";
        StringBuilder result = new StringBuilder();
        result.append(getView().getString(R.string.flight_order_a_n_prefix) + " " + manualTransfer.getAccountName() + newLine);
        result.append(getView().getString(R.string.flight_order_branch_prefix) + " " + manualTransfer.getAccountBranch() + newLine);
        result.append(manualTransfer.getAccountNo());
        makeSmall(desc.append(result.toString()));
        text.append("\n");
        text.append(desc);
        return text;
    }

    private SpannableStringBuilder makeBold(SpannableStringBuilder text) {
        if (TextUtils.isEmpty(text)) {
            return text;
        }
        text.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new RelativeSizeSpan(1.25f),
                0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

    private SpannableStringBuilder makeSmall(SpannableStringBuilder text) {
        if (TextUtils.isEmpty(text)) {
            return text;
        }
        text.setSpan(new RelativeSizeSpan(1.00f),
                0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

    private String generateCancelMessage(List<FlightOrderJourney> flightOrder, List<FlightOrderPassengerViewModel> passengerViewModels) {
        String newLine = "\n";
        StringBuilder result = new StringBuilder();
        result.append(newLine);
        for (FlightOrderJourney flightOrderJourney : flightOrder) {
            String item = flightOrderJourney.getDepartureAiportId() + "-" + flightOrderJourney.getArrivalAirportId() + " ";
            item += newLine;
            ArrayList<String> passengers = new ArrayList<>();
            for (FlightOrderPassengerViewModel flightOrderPassengerViewModel : passengerViewModels) {
                passengers.add(flightOrderPassengerViewModel.getPassengerFirstName() + " " + flightOrderPassengerViewModel.getPassengerLastName());
            }
            item += TextUtils.join(newLine, passengers);
            item += newLine;
            result.append(item);
        }
        return result.toString();
    }

    private String generateInvoiceLink(String orderId) {
        return FlightUrl.getUrlInvoice(orderId, userSession.getUserId());
    }

    private String generateTicketLink(String orderId, String pdf, String userId) {
        return FlightUrl.getUrlPdf(orderId, pdf, userId);
    }

    private void generateStatus(int status, String statusString) {
        switch (status) {
            case FlightStatusOrderType.EXPIRED:
                getView().updateViewStatus(statusString, R.color.deep_orange_500, false, false, false, true);
                break;
            case FlightStatusOrderType.CONFIRMED:
                getView().updateViewStatus(statusString, R.color.font_black_primary_70, true, false, true, false);
                break;
            case FlightStatusOrderType.FAILED:
                getView().updateViewStatus(statusString, R.color.font_black_primary_70, false, false, false, false);
                break;
            case FlightStatusOrderType.FINISHED:
                getView().updateViewStatus(statusString, R.color.font_black_primary_70, true, false, true, false);
                break;
            case FlightStatusOrderType.PROGRESS:
                getView().updateViewStatus(statusString, R.color.font_black_primary_70, false, false, false, false);
                break;
            case FlightStatusOrderType.READY_FOR_QUEUE:
                getView().updateViewStatus(statusString, R.color.font_black_primary_70, false, false, false, false);
                break;
            case FlightStatusOrderType.REFUNDED:
                getView().updateViewStatus(statusString, R.color.font_black_primary_70, false, false, false, false);
                break;
            case FlightStatusOrderType.WAITING_FOR_PAYMENT:
                getView().updateViewStatus(statusString, R.color.deep_orange_500, false, false, false, false);
                break;
            case FlightStatusOrderType.WAITING_FOR_THIRD_PARTY:
                getView().updateViewStatus(statusString, R.color.font_black_primary_70, false, false, false, false);
                break;
            case FlightStatusOrderType.WAITING_FOR_TRANSFER:
                getView().updateViewStatus(statusString, R.color.deep_orange_500, false, false, false, false);
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

    private List<SimpleViewModel> transformToSimpleModelPrice(FlightOrder flightOrder) {
        List<SimpleViewModel> simpleViewModelList = new ArrayList<>();

        Map<String, Integer> meals = new HashMap<>();
        Map<String, Integer> luggages = new HashMap<>();

        int passengerAdultCount = 0;
        int passengerChildCount = 0;
        int passengerInfantCount = 0;

        for (FlightOrderPassengerViewModel flightOrderPassengerViewModel : flightOrder.getPassengerViewModels()) {
            // add to passenger count
            switch (flightOrderPassengerViewModel.getType()) {
                case FlightBookingPassenger.ADULT:
                    passengerAdultCount++;
                    break;
                case FlightBookingPassenger.CHILDREN:
                    passengerChildCount++;
                    break;
                case FlightBookingPassenger.INFANT:
                    passengerInfantCount++;
                    break;
            }

            for (FlightBookingAmenityViewModel amenityViewModel : flightOrderPassengerViewModel.getAmenities()) {
                switch (Integer.toString(amenityViewModel.getAmenityType())) {
                    case FlightAmenityType.LUGGAGE:
                        String key = String.format("%s - %s", amenityViewModel.getDepartureId(), amenityViewModel.getArrivalId());
                        if (luggages.containsKey(key)) {
                            luggages.put(key, luggages.get(key) + amenityViewModel.getPriceNumeric());
                        } else {
                            luggages.put(key, amenityViewModel.getPriceNumeric());
                        }
                        break;
                    case FlightAmenityType.MEAL:
                        key = String.format("%s - %s", amenityViewModel.getDepartureId(), amenityViewModel.getArrivalId());
                        if (meals.containsKey(key)) {
                            meals.put(key, meals.get(key) + amenityViewModel.getPriceNumeric());
                        } else {
                            meals.put(key, amenityViewModel.getPriceNumeric());
                        }
                        break;
                }

                // add total price
                totalPrice += amenityViewModel.getPriceNumeric();
            }
        }

        // add total price
        totalPrice += flightOrder.getTotalAdultNumeric();
        totalPrice += flightOrder.getTotalChildNumeric();
        totalPrice += flightOrder.getTotalInfantNumeric();

        // add simpleViewModel price for adult passenger
        if (passengerAdultCount > 0)
            simpleViewModelList.add(formatPassengerFarePriceDetail(getView().getString(R.string.select_passenger_adult_title), passengerAdultCount, flightOrder.getTotalAdultNumeric()));

        // add simpleViewModel price for child passenger
        if (passengerChildCount > 0)
            simpleViewModelList.add(formatPassengerFarePriceDetail(getView().getString(R.string.select_passenger_children_title), passengerChildCount, flightOrder.getTotalChildNumeric()));

        // add simpleViewModel price for infant passenger
        if (passengerInfantCount > 0)
            simpleViewModelList.add(formatPassengerFarePriceDetail(getView().getString(R.string.select_passenger_infant_title), passengerInfantCount, flightOrder.getTotalInfantNumeric()));

        for (Map.Entry<String, Integer> entry : luggages.entrySet()) {
            simpleViewModelList.add(new SimpleViewModel(
                    String.format("%s %s", getView().getString(R.string.flight_price_detail_prefix_luggage_label),
                            entry.getKey()),
                    CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(entry.getValue())));
        }

        for (Map.Entry<String, Integer> entry : meals.entrySet()) {
            simpleViewModelList.add(new SimpleViewModel(
                    String.format("%s %s", getView().getString(R.string.flight_price_detail_prefixl_meal_label),
                            entry.getKey()),
                    CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(entry.getValue())));
        }

        return simpleViewModelList;
    }

    private SimpleViewModel formatPassengerFarePriceDetail(
            String label,
            int passengerCount,
            int price) {
        return new SimpleViewModel(
                String.format("%s x%d",
                        label,
                        passengerCount),
                CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(price));
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
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }
        super.detachView();
    }
}
