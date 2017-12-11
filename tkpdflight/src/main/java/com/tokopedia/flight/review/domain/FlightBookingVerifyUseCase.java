package com.tokopedia.flight.review.domain;

import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;
import com.tokopedia.flight.review.domain.verifybooking.model.request.AmenityPassenger;
import com.tokopedia.flight.review.domain.verifybooking.model.request.AttributesData;
import com.tokopedia.flight.review.domain.verifybooking.model.request.CartItem;
import com.tokopedia.flight.review.domain.verifybooking.model.request.Configuration;
import com.tokopedia.flight.review.domain.verifybooking.model.request.Data;
import com.tokopedia.flight.review.domain.verifybooking.model.request.MetaData;
import com.tokopedia.flight.review.domain.verifybooking.model.request.Passenger;
import com.tokopedia.flight.review.domain.verifybooking.model.request.VerifyRequest;
import com.tokopedia.flight.review.domain.verifybooking.model.response.DataResponseVerify;
import com.tokopedia.flight.review.view.model.FlightDetailPassenger;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 12/7/17.
 */

public class FlightBookingVerifyUseCase extends UseCase<DataResponseVerify> {

    public static final String VERIFY_CART = "verify_cart";
    public static final int PRODUCT_ID = 27;
    public static final int QUANTITY = 1;
    private static final String VERIFY_REQUEST = "VERIFY_REQUEST";
    private final FlightRepository flightRepository;

    @Inject
    public FlightBookingVerifyUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<DataResponseVerify> createObservable(RequestParams requestParams) {
        return flightRepository.verifyBooking((VerifyRequest) requestParams.getObject(VERIFY_REQUEST));
    }

    public RequestParams createRequestParams(String promoCode, int price, int adult, String cartId,
                                             List<FlightBookingPassengerViewModel> flightPassengerViewModels,
                                             String contactName, String country, String email, String phone) {
        RequestParams requestParams = RequestParams.create();
        VerifyRequest verifyRequest = new VerifyRequest();
        Data data = new Data();
        data.setType(VERIFY_CART);
        AttributesData attributesData = new AttributesData();
        attributesData.setPromocode(promoCode);
        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setProductId(PRODUCT_ID);
        cartItem.setQuantity(QUANTITY);
        Configuration configuration = new Configuration();
        configuration.setPrice(price);
        cartItem.setConfiguration(configuration);
        MetaData metaData = new MetaData();
        metaData.setContactName(contactName);
        metaData.setCountry(country);
        metaData.setEmail(email);
        metaData.setPhone(phone);
        metaData.setCartId(cartId);
        metaData.setPassengers(generatePassengers(flightPassengerViewModels));
        cartItem.setMetaData(metaData);
        cartItems.add(cartItem);
        attributesData.setCartItems(cartItems);
        data.setAttributesData(attributesData);
        verifyRequest.setData(data);
        requestParams.putObject(VERIFY_REQUEST, verifyRequest);
        return requestParams;
    }

    private List<Passenger> generatePassengers(List<FlightBookingPassengerViewModel> flightPassengerViewModels) {
        List<Passenger> passengers = new ArrayList<>();
        for(FlightBookingPassengerViewModel flightPassengerViewModel : flightPassengerViewModels){
            Passenger passenger = new Passenger();
            passenger.setDob(flightPassengerViewModel.getPassengerBirthdate());
            passenger.setFirstName(flightPassengerViewModel.getPassengerName());
            passenger.setLastName(flightPassengerViewModel.getPassengerName());
            passenger.setTitle(flightPassengerViewModel.getPassengerTitleId());
            passenger.setType(flightPassengerViewModel.getType());
            passenger.setAmenities(generateAmenities(flightPassengerViewModel.getFlightBookingLuggageMetaViewModels(), flightPassengerViewModel.getFlightBookingMealMetaViewModels()));
            passengers.add(passenger);
        }
        return passengers;
    }

    private List<AmenityPassenger> generateAmenities(List<FlightBookingAmenityMetaViewModel> flightBookingLuggageMetaViewModels, List<FlightBookingAmenityMetaViewModel> flightBookingMealMetaViewModels) {
        return null;
    }
}
