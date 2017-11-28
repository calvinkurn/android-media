package com.tokopedia.flight.review.data;

import com.tokopedia.flight.review.data.model.AttributesVoucher;
import com.tokopedia.usecase.utils.TKPDMapParam;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 11/27/17.
 */

public class FlightCheckVoucheCodeDataSource {

    private final FlightCheckVoucherCodeDataSourceCloud flightCheckVoucherCodeDataSourceCloud;

    @Inject
    public FlightCheckVoucheCodeDataSource(FlightCheckVoucherCodeDataSourceCloud flightCheckVoucherCodeDataSourceCloud) {
        this.flightCheckVoucherCodeDataSourceCloud = flightCheckVoucherCodeDataSourceCloud;
    }

    public Observable<AttributesVoucher> checkVoucherCode(TKPDMapParam<String, String> paramsAllValueInString) {
        return flightCheckVoucherCodeDataSourceCloud.checkVoucherCode(paramsAllValueInString);
    }
}
