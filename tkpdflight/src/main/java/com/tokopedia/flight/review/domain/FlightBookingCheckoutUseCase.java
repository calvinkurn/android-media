package com.tokopedia.flight.review.domain;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.common.util.FlightRequestUtil;
import com.tokopedia.flight.review.data.model.FlightCheckoutEntity;
import com.tokopedia.flight.review.domain.checkout.FlightCheckoutAttributes;
import com.tokopedia.flight.review.domain.checkout.FlightCheckoutConfiguration;
import com.tokopedia.flight.review.domain.checkout.FlightCheckoutData;
import com.tokopedia.flight.review.domain.checkout.FlightCheckoutItem;
import com.tokopedia.flight.review.domain.checkout.FlightCheckoutMetaData;
import com.tokopedia.flight.review.domain.checkout.FlightCheckoutRequest;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by zulfikarrahman on 11/10/17.
 */

public class FlightBookingCheckoutUseCase extends UseCase<FlightCheckoutEntity> {
    public static final String PARAM_CART_ID = "cart_id";
    public static final String PARAM_FLIGHT_ID = "invoice_id";
    public static final String PARAM_PRICE = "price";
    public static final String PARAM_PROMOCODE = "promocode";
    private FlightRepository flightRepository;

    @Inject
    public FlightBookingCheckoutUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<FlightCheckoutEntity> createObservable(RequestParams requestParams) {
        return createRequest(requestParams).flatMap(new Func1<FlightCheckoutRequest, Observable<FlightCheckoutEntity>>() {
            @Override
            public Observable<FlightCheckoutEntity> call(FlightCheckoutRequest checkoutRequest) {
                return flightRepository.checkout(checkoutRequest);
            }
        });
    }

    private Observable<FlightCheckoutRequest> createRequest(RequestParams requestParams) {
        FlightCheckoutData data = new FlightCheckoutData();
        data.setType("checkout_cart");
        FlightCheckoutAttributes attributes = new FlightCheckoutAttributes();
        List<FlightCheckoutItem> items = new ArrayList<>();
        FlightCheckoutItem item = new FlightCheckoutItem();
        item.setQuantity(1);
        item.setProductId(27);
        FlightCheckoutConfiguration configuration = new FlightCheckoutConfiguration();
        configuration.setPrice(requestParams.getInt(PARAM_PRICE, 0));
        item.setConfiguration(configuration);
        FlightCheckoutMetaData metaData = new FlightCheckoutMetaData();
        metaData.setDid(5);
        metaData.setFlightId(requestParams.getString(PARAM_FLIGHT_ID, ""));
        metaData.setIpAddress(FlightRequestUtil.getLocalIpAddress());
        metaData.setUserAgent(FlightRequestUtil.getUserAgentForApiCall());
        metaData.setCartId(requestParams.getString(PARAM_CART_ID, ""));
        item.setMetaData(metaData);
        items.add(item);
        attributes.setItems(items);
        if (requestParams.getString(PARAM_PROMOCODE, null) != null) {
            attributes.setPromocode(requestParams.getString(PARAM_PROMOCODE, ""));
        }
        data.setAttributes(attributes);
        FlightCheckoutRequest checkoutRequest = new FlightCheckoutRequest();
        checkoutRequest.setData(data);
        return Observable.just(checkoutRequest);
    }

    public RequestParams createRequestParam(String cartId, String flightId, int price, String promoCode) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(PARAM_PRICE, price);
        requestParams.putString(PARAM_CART_ID, cartId);
        requestParams.putString(PARAM_FLIGHT_ID, flightId);
        requestParams.putString(PARAM_PROMOCODE, promoCode);
        return requestParams;
    }

    public RequestParams createRequestParam(String cartId, String flightId, int price) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(PARAM_PRICE, price);
        requestParams.putString(PARAM_CART_ID, cartId);
        requestParams.putString(PARAM_FLIGHT_ID, flightId);
        return requestParams;
    }
}
