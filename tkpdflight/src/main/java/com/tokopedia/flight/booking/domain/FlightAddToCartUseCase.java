package com.tokopedia.flight.booking.domain;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.data.cloud.requestbody.CartAirportRequest;
import com.tokopedia.flight.booking.data.cloud.requestbody.CartAttributesRequest;
import com.tokopedia.flight.booking.data.cloud.requestbody.FlightCartRequest;
import com.tokopedia.flight.booking.data.cloud.requestbody.FlightRequest;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.common.util.FlightRequestUtil;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;

/**
 * @author by alvarisi on 11/13/17.
 */

public class FlightAddToCartUseCase extends UseCase<CartEntity> {
    public static final String PARAM_ADULT = "adult";
    public static final String PARAM_CHILD = "child";
    public static final String PARAM_INFANT = "infant";
    public static final String PARAM_CLASS = "class";
    public static final String PARAM_COMBO_KEY = "combo_key";
    public static final String PARAM_FLIGHT_DEPARTURE = "departure";
    public static final String PARAM_FLIGHT_RETURN = "return";
    public static final String PARAM_ID_EMPOTENCY_KEY = "id_empotency_key";
    private static final String PARAM_DEFAULT_VALUE = "";
    private static final int PARAM_DEFAULT_VALUE_INT = 0;
    private static final String PARAM_CART_TYPE = "add_cart";
    private FlightRepository flightRepository;
    private FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase;

    @Inject
    public FlightAddToCartUseCase(FlightRepository flightRepository,
                                  FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase) {
        this.flightRepository = flightRepository;
        this.flightBookingGetSingleResultUseCase = flightBookingGetSingleResultUseCase;
    }

    @Override
    public Observable<CartEntity> createObservable(RequestParams requestParams) {
        return createRequest(requestParams)
                .flatMap(new Func1<FlightCartRequest, Observable<CartEntity>>() {
                    @Override
                    public Observable<CartEntity> call(FlightCartRequest request) {
                        return flightRepository.addCart(request, request.getIdEmpotencyKey());
                    }
                });
    }

    private Observable<FlightCartRequest> createRequest(RequestParams requestParams) {
        FlightCartRequest request = new FlightCartRequest();
        request.setType(PARAM_CART_TYPE);
        CartAttributesRequest attributesRequest = new CartAttributesRequest();
        attributesRequest.setDid(4);
        attributesRequest.setIpAddress(FlightRequestUtil.getLocalIpAddress());
        attributesRequest.setUserAgent(FlightRequestUtil.getUserAgentForApiCall());
        FlightRequest flightRequest = new FlightRequest();
        flightRequest.setAdult(requestParams.getInt(PARAM_ADULT, 0));
        flightRequest.setChild(requestParams.getInt(PARAM_CHILD, PARAM_DEFAULT_VALUE_INT));
        flightRequest.setInfant(requestParams.getInt(PARAM_INFANT, PARAM_DEFAULT_VALUE_INT));
        flightRequest.setClassFlight(requestParams.getInt(PARAM_CLASS, PARAM_DEFAULT_VALUE_INT));
        flightRequest.setComboKey(PARAM_DEFAULT_VALUE);
        List<CartAirportRequest> airportRequests = new ArrayList<>();
        flightRequest.setDestinations(airportRequests);
        attributesRequest.setFlight(flightRequest);
        request.setCartAttributes(attributesRequest);
        String departureId = requestParams.getString(PARAM_FLIGHT_DEPARTURE, PARAM_DEFAULT_VALUE);
        String arrivalId = requestParams.getString(PARAM_FLIGHT_RETURN, PARAM_DEFAULT_VALUE);

        request.setIdEmpotencyKey(requestParams.getString(PARAM_ID_EMPOTENCY_KEY, PARAM_DEFAULT_VALUE));

        if (!TextUtils.isEmpty(arrivalId)) {
            return Observable.zip(
                    flightBookingGetSingleResultUseCase.createObservable(flightBookingGetSingleResultUseCase.createRequestParam(false, departureId)),
                    flightBookingGetSingleResultUseCase.createObservable(flightBookingGetSingleResultUseCase.createRequestParam(true, arrivalId)),
                    Observable.just(request),
                    new Func3<FlightSearchViewModel, FlightSearchViewModel, FlightCartRequest, FlightCartRequest>() {
                        @Override
                        public FlightCartRequest call(FlightSearchViewModel departureViewModel, FlightSearchViewModel arrivalViewModel, FlightCartRequest flightCartRequest) {
                            CartAirportRequest departureAirport = getCartAirportRequest(departureViewModel);
                            CartAirportRequest arrivalAirport = getCartAirportRequest(arrivalViewModel);
                            List<CartAirportRequest> airportRequests = new ArrayList<>();
                            airportRequests.add(departureAirport);
                            airportRequests.add(arrivalAirport);
                            flightCartRequest.getCartAttributes().getFlight().setDestinations(airportRequests);
                            return flightCartRequest;
                        }
                    });
        } else {
            return Observable.zip(
                    flightBookingGetSingleResultUseCase.createObservable(flightBookingGetSingleResultUseCase.createRequestParam(false, departureId)),
                    Observable.just(request),
                    new Func2<FlightSearchViewModel, FlightCartRequest, FlightCartRequest>() {
                        @Override
                        public FlightCartRequest call(FlightSearchViewModel departureViewModel, FlightCartRequest flightCartRequest) {
                            CartAirportRequest departureAirport = getCartAirportRequest(departureViewModel);
                            List<CartAirportRequest> airportRequests = new ArrayList<>();
                            airportRequests.add(departureAirport);
                            flightCartRequest.getCartAttributes().getFlight().setDestinations(airportRequests);
                            return flightCartRequest;
                        }
                    });
        }
    }

    @NonNull
    private CartAirportRequest getCartAirportRequest(FlightSearchViewModel routeViewModel) {
        CartAirportRequest departureAirport = new CartAirportRequest();
        departureAirport.setJourneyId(routeViewModel.getId());
        departureAirport.setTerm(routeViewModel.getTerm());
        return departureAirport;
    }

    public RequestParams createRequestParam(int adult,
                                            int child,
                                            int infant,
                                            int flightClass,
                                            String departureId,
                                            String idEmpotencyKey) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(PARAM_ADULT, adult);
        requestParams.putInt(PARAM_CHILD, child);
        requestParams.putInt(PARAM_INFANT, infant);
        requestParams.putInt(PARAM_CLASS, flightClass);
        requestParams.putString(PARAM_FLIGHT_DEPARTURE, departureId);
        requestParams.putString(PARAM_ID_EMPOTENCY_KEY, idEmpotencyKey);
        return requestParams;
    }

    public RequestParams createRequestParam(int adult,
                                            int child,
                                            int infant,
                                            int flightClass,
                                            String departureId,
                                            String returnId,
                                            String idEmpotencyKey) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(PARAM_ADULT, adult);
        requestParams.putInt(PARAM_CHILD, child);
        requestParams.putInt(PARAM_INFANT, infant);
        requestParams.putInt(PARAM_CLASS, flightClass);
        requestParams.putString(PARAM_FLIGHT_DEPARTURE, departureId);
        requestParams.putString(PARAM_FLIGHT_RETURN, returnId);
        requestParams.putString(PARAM_ID_EMPOTENCY_KEY, idEmpotencyKey);
        return requestParams;
    }
}
