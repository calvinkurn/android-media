package com.tokopedia.transaction.checkout.domain;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.transaction.checkout.domain.response.rates.RatesResponse;
import com.tokopedia.transaction.checkout.view.data.ShipmentDetailData;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Irfan Khoirul on 20/02/18.
 */

public class RatesRepository {
    private final RatesDataStore dataStore;
    private final ShipmentRatesDataMapper shipmentRatesDataMapper;

    @Inject
    public RatesRepository(RatesDataStore dataStore, ShipmentRatesDataMapper shipmentRatesDataMapper) {
        this.dataStore = dataStore;
        this.shipmentRatesDataMapper = shipmentRatesDataMapper;
    }

    public Observable<ShipmentDetailData> getRates(final ShipmentDetailData shipmentDetailData,
                                                   TKPDMapParam<String, String> parameters) {
        return dataStore.getRates(parameters).map(new Func1<RatesResponse, ShipmentDetailData>() {
            @Override
            public ShipmentDetailData call(RatesResponse ratesResponse) {
                return shipmentRatesDataMapper.getShipmentDetailData(shipmentDetailData, ratesResponse);
            }
        });
    }
}
