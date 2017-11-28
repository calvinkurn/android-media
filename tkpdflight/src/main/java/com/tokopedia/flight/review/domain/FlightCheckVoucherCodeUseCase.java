package com.tokopedia.flight.review.domain;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.review.data.model.AttributesVoucher;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 11/10/17.
 */

public class FlightCheckVoucherCodeUseCase extends UseCase<AttributesVoucher> {

    public static final String CART_ID = "cart_id";
    public static final String VOUCHER_CODE = "voucher_code";

    private final FlightRepository flightRepository;

    @Inject
    public FlightCheckVoucherCodeUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<AttributesVoucher> createObservable(RequestParams requestParams) {
        return flightRepository.checkVoucherCode(requestParams.getParamsAllValueInString());
    }

    public RequestParams createRequestParams(String cartId, String voucherCode){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(CART_ID, cartId);
        requestParams.putString(VOUCHER_CODE, voucherCode);
        return requestParams;
    }
}
