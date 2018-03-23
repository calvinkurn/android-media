package com.tokopedia.transaction.checkout.data.repository;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.transaction.checkout.data.entity.response.rates.RatesResponse;
import com.tokopedia.transaction.checkout.data.mapper.ShipmentRatesDataMapper;
import com.tokopedia.transaction.checkout.data.mapper.ShipmentRatesDataMapperV2;
import com.tokopedia.transaction.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Irfan Khoirul on 20/02/18.
 */

public class RatesRepository {
    private final RatesDataStore dataStore;
    private final RatesDataStoreV2 dataStoreV2;
    private final ShipmentRatesDataMapper shipmentRatesDataMapper;
    private final ShipmentRatesDataMapperV2 shipmentRatesDataMapperV2;

    @Inject
    public RatesRepository(RatesDataStore dataStore, RatesDataStoreV2 dataStoreV2,
                           ShipmentRatesDataMapper shipmentRatesDataMapper,
                           ShipmentRatesDataMapperV2 shipmentRatesDataMapperV2) {
        this.dataStore = dataStore;
        this.dataStoreV2 = dataStoreV2;
        this.shipmentRatesDataMapper = shipmentRatesDataMapper;
        this.shipmentRatesDataMapperV2 = shipmentRatesDataMapperV2;
    }

//    public Observable<ShipmentDetailData> getRates(final ShipmentDetailData shipmentDetailData,
//                                                   TKPDMapParam<String, String> parameters) {
//        return dataStore.getRates(parameters).map(new Func1<RatesResponse, ShipmentDetailData>() {
//            @Override
//            public ShipmentDetailData call(RatesResponse ratesResponse) {
//                return shipmentRatesDataMapper.getShipmentDetailData(shipmentDetailData, ratesResponse);
//            }
//        });
//    }

    public Observable<ShipmentDetailData> getRates(final ShipmentDetailData shipmentDetailData,
                                                   TKPDMapParam<String, String> parameters) {
        return dataStoreV2.getRates(parameters).map(new Func1<com.tokopedia.transaction.checkout.data.entity.response.ratesV2.RatesResponse, ShipmentDetailData>() {
            @Override
            public ShipmentDetailData call(com.tokopedia.transaction.checkout.data.entity.response.ratesV2.RatesResponse ratesResponse) {
                return shipmentRatesDataMapperV2.getShipmentDetailData(shipmentDetailData, ratesResponse);
            }
        });
    }
}
